package com.example.bookstore.service;

import com.example.bookstore.enums.AuthorRole;
import com.example.bookstore.enums.Format;
import com.example.bookstore.persistance.entity.*;
import com.example.bookstore.persistance.entity.Character;
import com.example.bookstore.persistance.repository.*;
import com.example.bookstore.service.dto.*;
import com.example.bookstore.service.exception.UnsupportedFormatException;
import com.example.bookstore.service.mapper.*;
import com.example.bookstore.service.parser.*;
import com.example.bookstore.service.registry.EntityMapRegistry;
import com.example.bookstore.service.registry.MapperRegistry;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class CSVUploadService {

    private final AwardRepository awardRepository;
    private final SettingRepository settingRepository;
    private final SeriesRepository seriesRepository;
    private final LanguageRepository languageRepository;
    private final CharacterRepository characterRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final MapperRegistry mapperRegistry;
    private final StarRepository starRepository;
    //private final RatingByStarsRepository ratingByStarsRepository;
    private final BookRepository bookRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookSeriesRepository bookSeriesRepository;
    private final BookAwardRepository bookAwardRepository;
    private final BookGenreRepository bookGenreRepository;
    private final ReviewsRepository reviewsRepository;
    private final BookSettingRepository bookSettingRepository;
    private final BookCharacterRepository bookCharacterRepository;
    private final EntityMapRegistry entityMapRegistry;
    private final FileInformationRepository fileInformationRepository;
    private final CoverImageService coverImageService;

    @Value("${app.image.processing.enabled}")
    private boolean imageProcessingEnabled;

    /*@Value("${app.image.root.path}")
    private static String IMAGES_ROOT;*/

    private static final int CHUNK_SIZE = 2500;

    @SuppressWarnings("unchecked")
    public boolean uploadCSV(MultipartFile file) throws IOException {
        int numberOfAvailableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService =
                Executors.newFixedThreadPool(numberOfAvailableProcessors);

        File tempFile = File.createTempFile("upload", ".csv");
        file.transferTo(tempFile);
        List<String> bookIds = null;
        List<FileInformation> urls = null;
        try (Reader reader = new BufferedReader(new InputStreamReader(BOMInputStream.builder().setFile(tempFile).get(), StandardCharsets.UTF_8));
            CSVParser parser = CSVFormat.EXCEL.builder()
                    .setHeader()
                    .get()
                    .parse(reader)){
            List<CSVRecord> rows = parser.getRecords();

            Set<String> existingBookIds = bookRepository.findAllBookId();

            ConcurrentMap<String, Genre> savedGenresMap = (ConcurrentMap<String, Genre>) entityMapRegistry.entityMap(Genre.class);
            ConcurrentMap<String, Author> savedAuthorsMap = (ConcurrentMap<String, Author>) entityMapRegistry.entityMap(Author.class);
            ConcurrentMap<String, Character> savedCharactersMap = (ConcurrentMap<String, Character>) entityMapRegistry.entityMap(Character.class);
            ConcurrentMap<String, Publisher> savedPublishersMap = (ConcurrentMap<String, Publisher>) entityMapRegistry.entityMap(Publisher.class);
            ConcurrentMap<String, Language> savedLanguagesMap = (ConcurrentMap<String, Language>) entityMapRegistry.entityMap(Language.class);
            ConcurrentMap<String, Setting> savedSettingsMap = (ConcurrentMap<String, Setting>) entityMapRegistry.entityMap(Setting.class);
            ConcurrentMap<String, Series> savedSeriesMap = (ConcurrentMap<String, Series>) entityMapRegistry.entityMap(Series.class);
            ConcurrentMap<String, Award> savedAwardsMap = (ConcurrentMap<String, Award>) entityMapRegistry.entityMap(Award.class);
            ConcurrentMap<String, Star> savedStarsMap = (ConcurrentMap<String, Star>) entityMapRegistry.entityMap(Star.class);

            Map<String, Genre> existingGenres= new HashMap<>();
            List<Genre> existingGenresList = genreRepository.findAll();
            for (Genre genre : existingGenresList) {
                existingGenres.put(genre.getName(), genre);
            }
            Map<String, Author> existingAuthors = new HashMap<>();
            List<Author> existingAuthorsList = authorRepository.findAll();
            for (Author author : existingAuthorsList) {
                existingAuthors.put(author.getFullName(), author);
            }
            Map<String, Setting> existingSettings = new HashMap<>();
            List<Setting> existingSettingsList = settingRepository.findAll();
            for (Setting setting : existingSettingsList) {
                existingSettings.put(setting.getName(), setting);
            }
            Map<String, Character> existingCharacters = new HashMap<>();
            List<Character> existingCharactersList = characterRepository.findAll();
            for (Character character : existingCharactersList) {
                existingCharacters.put(character.getFullName() + character.getComment(), character);
            }
            Map<String, Publisher> existingPublishers = new HashMap<>();
            List<Publisher> existingPublishersList = publisherRepository.findAll();
            for (Publisher publisher : existingPublishersList) {
                existingPublishers.put(publisher.getName(), publisher);
            }
            Map<String, Language> existingLanguages = new HashMap<>();
            List<Language> existingLanguagesList = languageRepository.findAll();
            for (Language language : existingLanguagesList) {
                existingLanguages.put(language.getLanguage(), language);
            }
            Map<String, Series> existingSeries = new HashMap<>();
            List<Series> existingSeriesList = seriesRepository.findAll();
            for (Series series : existingSeriesList) {
                existingSeries.put(series.getName(), series);
            }
            Map<String, Award> existingAwards = new HashMap<>();
            List<Award> existingAwardsList = awardRepository.findAll();
            for (Award award : existingAwardsList) {
                existingAwards.put(award.getName() + award.getYear(), award);
            }

            saveStars(savedStarsMap, starRepository);
            bookIds = new ArrayList<>();
            urls = new ArrayList<>();

            for (int k = 0; k < rows.size(); k++) {
                CSVRecord record = rows.get(k);
                String bookId= record.get("bookId").trim();
                if(existingBookIds.contains(bookId)) {
                    rows.remove(record);
                    k--;
                    continue;
                }
                //populating the lists for image saving
                bookIds.add(bookId);
                urls.add(FileInformation.of(record.get("coverImg").trim()));

                //saving all the genres
                String genres = record.get("genres");
                List<GenreDTO> genreDTOS = (new GenresParser()).parseGenres(genres);
                for(GenreDTO genreDto : genreDTOS) {
                    if(!existingGenres.containsKey(genreDto.getName())) {
                        Genre genre = mapperRegistry.getMapper(GenreMapper.class).dtoToEntity(genreDto);
                        savedGenresMap.put(genre.getName(), genre);
                    }
                }
                //saving all the authors (roles to be handled separately)
                String authors = record.get("author");
                Set<AuthorDTO> authorDTOS = (new AuthorParser()).parseAuthors(authors);
                for(AuthorDTO authorDto : authorDTOS) {
                    if(!existingAuthors.containsKey(authorDto.getFullName())) {
                        Author author = mapperRegistry.getMapper(AuthorMapper.class).dtoToEntity(authorDto);
                        savedAuthorsMap.put(author.getFullName(), author);
                    }
                }
                //saving all the settings
                String settings = record.get("setting");
                Set<SettingDTO> settingDTOS = (new SettingsParser()).parseSettings(settings);
                for(SettingDTO settingDto : settingDTOS) {
                    if(!existingSettings.containsKey(settingDto.getName())) {
                        Setting setting = mapperRegistry.getMapper(SettingMapper.class).dtoToEntity(settingDto);
                        savedSettingsMap.put(setting.getName(), setting);
                    }
                }
                //saving all the characters
                String characters = record.get("characters");
                Set<CharacterDTO> characterDTOS = (new CharactersParser()).parseCharacter(characters);
                for(CharacterDTO characterDto : characterDTOS) {
                    String characterKey = characterDto.getFullName() + characterDto.getComment();
                    if(!existingCharacters.containsKey(characterKey)) {
                        Character character = mapperRegistry.getMapper(CharacterMapper.class).dtoToEntity(characterDto);
                        savedCharactersMap.put(characterKey, character);
                    }
                }
                //saving all the publishers
                String publisherString = record.get("publisher");
                PublisherDTO publisherDto = (new PublisherParser()).parsePublisher(publisherString);
                if(publisherDto != null) {
                    if(!existingPublishers.containsKey(publisherDto.getName())) {
                        Publisher publisher = mapperRegistry.getMapper(PublisherMapper.class).dtoToEntity(publisherDto);
                        savedPublishersMap.put(publisher.getName(), publisher);
                    }
                }
                //saving all the languages
                String languageString = record.get("language");
                LanguageDTO languageDto = (new LanguageParser()).parseLanguage(languageString);
                if(languageDto != null) {
                    if(!existingLanguages.containsKey(languageDto.getLanguage())) {
                        Language language = mapperRegistry.getMapper(LanguageMapper.class).dtoToEntity(languageDto);
                        savedLanguagesMap.put(language.getLanguage(), language);
                    }
                }
                //saving all the series
                String seriesString = record.get("series");
                SeriesDTO seriesDto = (new SeriesParser()).parseSeries(seriesString);
                if(seriesDto != null) {
                    if(!existingSeries.containsKey(seriesDto.getName())) {
                        Series series = mapperRegistry.getMapper(SeriesMapper.class).dtoToEntity(seriesDto);
                        savedSeriesMap.put(series.getName(), series);
                    }
                }
                //saving all the awards
                String awards = record.get("awards");
                Set<AwardDTO> awardDTOS = (new AwardsParser()).parseAwards(awards);
                for(AwardDTO awardDto : awardDTOS) {
                    if (!existingAwards.containsKey(awardDto.getName() + awardDto.getYear())){
                        Award award = mapperRegistry.getMapper(AwardMapper.class).dtoToEntity(awardDto);
                        savedAwardsMap.put(award.getName() + award.getYear(), award);
                    }
                }
            }
            entityMapRegistry.saveAll();
            fileInformationRepository.saveAll(urls);
            for(Genre genre: existingGenresList)
                savedGenresMap.put(genre.getName(), genre);
            for(Author author: existingAuthorsList)
                savedAuthorsMap.put(author.getFullName(), author);
            for(Character character: existingCharactersList)
                savedCharactersMap.put(character.getFullName() + character.getComment(), character);
            for(Publisher publisher: existingPublishersList)
                savedPublishersMap.put(publisher.getName(), publisher);
            for(Language language: existingLanguagesList)
                savedLanguagesMap.put(language.getLanguage(), language);
            for(Series series: existingSeriesList)
                savedSeriesMap.put(series.getName(), series);
            for(Setting setting: existingSettingsList)
                savedSettingsMap.put(setting.getName(), setting);
            for(Award award: existingAwardsList)
                savedAwardsMap.put(award.getName() + award.getYear(), award);

            if(!rows.isEmpty()) {
                submitTasks(rows, entityMapRegistry.getEntityMap(), executorService);
            }
            else{
                executorService.shutdown();
                return false;
            }
        }
        catch (IOException e){
            System.out.println("Error processing non-book entities: " + e.getMessage());
        }

        executorService.shutdown();
        try {
            // Wait until all tasks have finished or timeout occurs.
            if (!executorService.awaitTermination(300, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // force shutdown if necessary
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        if(tempFile.exists()) {
            tempFile.delete();
        }

        if(imageProcessingEnabled) {
            List<FileInformation> finalUrls = urls;
            List<String> finalBookIds = bookIds;
            CompletableFuture.runAsync(() -> coverImageService.saveImages(finalUrls, finalBookIds,
                    (ConcurrentMap<String, Book>) entityMapRegistry.entityMap(Book.class), 0,200));
        }
        return true;
    }

    private void saveStars(Map<String, Star> savedStarsMap, StarRepository starRepository) {

        List<Star> stars = starRepository.findAll();
        if(stars.isEmpty()){
            for(int k = 1; k <= 5; k++)
            {
                Star star = new Star();
                star.setStarNumber(k);
                stars.add(star);
                savedStarsMap.put(String.valueOf(k), star);
            }
            starRepository.saveAll(stars);
        }else {
            for(Star star : stars)
            {
                savedStarsMap.put(star.getStarNumber().toString(),star);
            }
        }

    }


    private void submitTasks(List<CSVRecord> rows, ConcurrentMap<Class<?>, ConcurrentMap<String, ?>> maps, ExecutorService executorService )
    {
        for(int i = 0; i < rows.size(); i += CHUNK_SIZE) {
            int finalI = i;
            executorService.submit(() -> processRecord(rows, maps, finalI));
        }
    }


    @SuppressWarnings({"unchecked", "DataFlowIssue"})
    private void processRecord(List<CSVRecord> rows, ConcurrentMap<Class<?>, ConcurrentMap<String, ?>> maps, int startingFrom) {
        ConcurrentMap<String, Book> savedBooks = (ConcurrentMap<String, Book>) maps.get(Book.class);
        List<Book> booksToSave = new ArrayList<>(CHUNK_SIZE);
        List<BookSetting> bookSettingsToSave =new ArrayList<>(CHUNK_SIZE*2);
        List<BookAuthor> bookAuthorsToSave = new ArrayList<>(CHUNK_SIZE*2);
        List<BookSeries> bookSeriesToSave = new ArrayList<>(CHUNK_SIZE*2);
        List<BookAward> bookAwardsToSave = new ArrayList<>(CHUNK_SIZE*10);
        List<Reviews> reviewsToSave = new ArrayList<>(CHUNK_SIZE);
        //List<RatingByStars> ratingByStarsToSave = new ArrayList<>(5*CHUNK_SIZE);
        List<BookGenre> bookGenresToSave = new ArrayList<>(CHUNK_SIZE*3);
        List<BookCharacter> bookCharactersToSave = new ArrayList<>(CHUNK_SIZE*10);


        for(int i = startingFrom; i < startingFrom + CHUNK_SIZE && i < rows.size(); i++) {
            try {
                Map<String, AuthorRole> authors = null;
                Set<String> settings = null;
                Set<String> characters = null;
                Set<String> genres = null;
                Book book = null;
                Set<String> awards = null;
                Map<String, Set<String>> bookRelatedData=null;

                try {
                    bookRelatedData = (new BookParser()).parseBook(rows.get(i));
                    String bookId = bookRelatedData.get("bookId").iterator().next();
                    if(savedBooks.containsKey(bookId)) {
                        continue;
                    }
                    settings = bookRelatedData.get("setting");
                    genres = bookRelatedData.get("genres");
                    characters = bookRelatedData.get("characters");
                    authors = (new AuthorParser()).parseAuthorsString(rows.get(i).get("author").trim());
                    Integer pageNumber=null;
                    String pageNumberString = bookRelatedData.get("pageNumber").iterator().next();
                    try {
                        if(pageNumberString != null && !pageNumberString.isEmpty())
                            pageNumber = Integer.parseInt(rows.get(i).get("pages").trim());
                    }catch (NumberFormatException e){
                        System.out.println("Page number '" + pageNumberString + "' is not supported. It must be an integer.");
                    }
                    LocalDate publishDate = null;
                    LocalDate firstPublishDate = null;
                    String publishDateString = bookRelatedData.get("publishDate").iterator().next();
                    String firstPublishDateString = bookRelatedData.get("firstPublishDate").iterator().next();
                    try {
                        if(publishDateString != null && !publishDateString.trim().isEmpty())
                            publishDate = (new DateParser()).parseDate(publishDateString.trim());
                    }
                    catch (Exception e){
                        System.out.println("Error parsing PublishDate in record:\n" + rows.get(i) + "\nPotential date: '" + publishDateString +
                                "'\nError message:\n"+ e.getMessage());
                    }
                    try {
                        if(firstPublishDateString != null && !firstPublishDateString.trim().isEmpty())
                            firstPublishDate = (new DateParser()).parseDate(firstPublishDateString.trim());
                    }catch (Exception e){
                        System.out.println("Error parsing firstPublishDate in record:\n" + rows.get(i) + "\nPotential date: '" + firstPublishDateString +
                                "'\nError message:\n"+ e.getMessage());
                    }
                    BigDecimal price = null;
                    String priceString = bookRelatedData.get("price").iterator().next();
                    try {
                        if(priceString != null && !priceString.trim().isEmpty())
                            price = BigDecimal.valueOf(Double.parseDouble(priceString.trim()));
                    }
                    catch (NumberFormatException e){
                        System.out.println("Price '" + priceString + "' is not supported. It must be a valid decimal number.");
                    }
                    awards = (new AwardsParser()).parseAwardsString(rows.get(i).get("awards"));
                    Format format = (new FormatParser()).parseFormat(rows.get(i).get("bookFormat").trim());
                    book = createBook(maps, bookRelatedData, bookId, format, price,
                            publishDate, firstPublishDate, pageNumber);
                    booksToSave.add(book);
                    savedBooks.put(bookId, book);
                }
                catch (NumberFormatException ne)
                {
                    System.out.println("Error with numbers in record:\n" + rows.get(i) + "\nError message:\n" + ne.getMessage());
                }
                catch (Exception e)
                {
                    System.out.println("Error saving book in record: \n" + rows.get(i) + "\nError message:\n" + e.getMessage());
                    continue;
                }
                ReviewsDTO reviewsDto = (new ReviewsParser().parseReviews(rows.get(i).get("rating"), rows.get(i).get("likedPercent"),
                        rows.get(i).get("bbeScore"), rows.get(i).get("bbeVotes")));
                Reviews reviews = mapperRegistry.getMapper(ReviewsMapper.class).dtoToEntity(reviewsDto);
                reviews.setBook(book);
                reviewsToSave.add(reviews);
                String seriesInput = bookRelatedData.get("series").iterator().next();
                if (!seriesInput.isEmpty()) {
                    try {
                        String[] seriesInformation = seriesInput.split("#");
                        if (seriesInformation.length > 2) {
                            throw new UnsupportedFormatException("Series of type " + seriesInput + " is not supported");
                        }
                        String seriesName = seriesInformation[0].trim();
                        String seriesNumber = (new SeriesParser()).parseSeriesNumber(seriesInput);
                        BookSeries bookSeries = new BookSeries();
                        bookSeries.setBook(book);
                        Series series = ((Map<String, Series>) maps.get(Series.class)).get(seriesName);
                        bookSeries.setSeries(series);
                        bookSeries.setSeriesNumber(seriesNumber);
                        //series.addBookSeries(bookSeries);
                        bookSeriesToSave.add(bookSeries);
                    }catch (UnsupportedFormatException e){
                        System.out.println(e.getMessage());
                    }
                }
                Integer totalStarRatings = null;
                String totalStarRatingsString = rows.get(i).get("numRatings");
                try {
                    totalStarRatings = Integer.parseInt(totalStarRatingsString.trim());
                }catch (NumberFormatException e){
                    System.out.println("Error parsing totalStarRatings in record:\n" + rows.get(i) + "\n Potential Number: '" + totalStarRatingsString +
                            "'\nError message:\n"+ e.getMessage());
                }
                if (totalStarRatings != null && totalStarRatings != 0) {
                    String ratingByStars = rows.get(i).get("ratingsByStars").trim();
                    List<Integer> starRatings = (new RatingByStarsParser()).parseRatingByStars(ratingByStars);
                    int n = 5;
                    for (Integer starRating : starRatings) {
                        /*RatingByStars ratingByStar = new RatingByStars();
                        ratingByStar.setBook(book);
                        ratingByStar.setStar(((Map<String, Star>) maps.get(Star.class)).get(String.valueOf(n--)));
                        ratingByStar.setNumberOfRatings(starRating);
                        ratingByStar.setTotalNumberOfRatings(totalStarRatings);
                        ratingByStarsToSave.add(ratingByStar);*/
                        assert book != null;
                        book.addRatingByStars(((Map<String, Star>) maps.get(Star.class)).get(String.valueOf(n--)),starRating,  totalStarRatings);

                    }
                }
                if(characters != null) {
                    for (String characterKey : characters) {
                        BookCharacter bookCharacter = new BookCharacter();
                        bookCharacter.setBook(book);
                        Character character = ((Map<String, Character>) maps.get(Character.class)).get(characterKey);
                        bookCharacter.setCharacter(character);
                        //character.addBookCharacter(bookCharacter);
                        bookCharactersToSave.add(bookCharacter);
                    }
                }
                Set<String> authorNames = authors.keySet();
                Iterator<String> authorIterator = authorNames.iterator();
                for (int j = 0; j < authors.size(); j++) {
                    String currentName = authorIterator.next();
                    BookAuthor bookAuthor = new BookAuthor();
                    bookAuthor.setBook(book);
                    Author author = ((Map<String, Author>) maps.get(Author.class)).get(currentName);
                    bookAuthor.setAuthor(author);
                    bookAuthor.setAuthorRole(authors.get(currentName));
                    //author.addBookAuthor(bookAuthor);
                    bookAuthorsToSave.add(bookAuthor);
                }
                assert awards != null;
                for (String awardString : awards) {
                    BookAward bookAward = new BookAward();
                    bookAward.setBook(book);
                    Award award = ((Map<String, Award>) maps.get(Award.class)).get(awardString);
                    bookAward.setAward(award);
                    //award.addBookAward(bookAward);
                    bookAwardsToSave.add(bookAward);
                }
                for (String genreString : genres) {
                    BookGenre bookGenre = new BookGenre();
                    bookGenre.setBook(book);
                    Genre genre = ((Map<String, Genre>) maps.get(Genre.class)).get(genreString);
                    bookGenre.setGenre(genre);
                    //genre.addBookGenre(bookGenre);
                    bookGenresToSave.add(bookGenre);
                }
                for (String settingString : settings) {
                    BookSetting bookSetting = new BookSetting();
                    bookSetting.setBook(book);
                    Setting setting = ((Map<String, Setting>) maps.get(Setting.class)).get(settingString);
                    bookSetting.setSetting(setting);
                    //setting.addBookSetting(bookSetting);
                    bookSettingsToSave.add( bookSetting);
                }

                if(i == startingFrom+200)
                    System.out.println("Thread finished successfully");
            } catch (Exception e) {
                System.out.println("Error from threading. Record:\n" + rows.get(i) + "\nError message:\n" + e.getMessage());
            }

        }
        bookRepository.saveAll(booksToSave);
        reviewsRepository.saveAll(reviewsToSave);
        bookSeriesRepository.saveAll(bookSeriesToSave);
        //ratingByStarsRepository.saveAll(ratingByStarsToSave);
        bookCharacterRepository.saveAll(bookCharactersToSave);
        bookAuthorRepository.saveAll(bookAuthorsToSave);
        bookAwardRepository.saveAll(bookAwardsToSave);
        bookGenreRepository.saveAll(bookGenresToSave);
        bookSettingRepository.saveAll(bookSettingsToSave);

        System.out.println("Thread finished");
    }

    @SuppressWarnings("unchecked")
    private Book createBook(ConcurrentMap<Class<?>, ConcurrentMap<String, ?>> maps, Map<String, Set<String>> bookRelatedData, String bookId, Format format,
                            BigDecimal price, LocalDate publishDate, LocalDate firstPublishDate, Integer pageNumber) {
        Book book = new Book();
        book.setBookId(bookId);
        book.setTitle(bookRelatedData.get("title").iterator().next());
        book.setFormat(format);
        book.setLanguage(((ConcurrentMap<String, Language>) maps.get(Language.class)).get(bookRelatedData.get("language").iterator().next()));
        book.setIsbn(bookRelatedData.get("isbn").iterator().next());
        book.setPrice(price);
        book.setEdition(bookRelatedData.get("edition").iterator().next());
        Publisher publisher = ((ConcurrentMap<String, Publisher>) maps.get(Publisher.class)).get(bookRelatedData.get("publisherName").iterator().next());
        book.setPublisher(publisher);
        if(publisher != null)
            publisher.addBook(book);
        book.setPublishDate(publishDate);
        book.setFirstPublishDate(firstPublishDate);
        book.setDescription(bookRelatedData.get("description").iterator().next());
        book.setPageNumber(pageNumber);
        return book;
    }

}
