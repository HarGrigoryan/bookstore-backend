package com.example.bookstore.service;

import com.example.bookstore.enums.AuthorRole;
import com.example.bookstore.enums.PictureSize;
import com.example.bookstore.exception.EntityAlreadyExistsException;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.exception.UnsupportedFormatException;
import com.example.bookstore.persistance.entity.*;
import com.example.bookstore.persistance.entity.Character;
import com.example.bookstore.persistance.repository.*;
import com.example.bookstore.service.criteria.BookSearchCriteria;
import com.example.bookstore.service.dto.*;
import com.example.bookstore.service.mapper.BookCreateResponseDtoMapper;
import com.example.bookstore.service.dto.AuthorResponseDTO;
import com.example.bookstore.service.dto.BookCreateDTO;
import com.example.bookstore.service.dto.BookUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;


@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ReviewsRepository reviewsRepository;
    private final PublisherRepository publisherRepository;
    private final LanguageRepository languageRepository;
    private final AuthorRepository authorRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final SettingRepository settingRepository;
    private final BookSettingRepository bookSettingRepository;
    private final GenreRepository genreRepository;
    private final BookGenreRepository bookGenreRepository;
    private final CharacterRepository characterRepository;
    private final BookCharacterRepository bookCharacterRepository;
    private final SeriesRepository seriesRepository;
    private final BookSeriesRepository bookSeriesRepository;
    private final AwardRepository awardRepository;
    private final BookAwardRepository bookAwardRepository;
    private final CoverImageService coverImageService;
    private final BookCoverImageRepository bookCoverImageRepository;
    private FileInformationRepository fileInformationRepository;


    public BookSearchResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null)
            throw new EntityNotFoundException("Book", id);
        return BookSearchResponseDTO.mapToDTO(book);
    }


    public BookSearchResponseDTO getBookByBookId(String bookId) {
        Book book = bookRepository.findByBookId(bookId);
        if(book == null)
            throw new EntityNotFoundException("Book with book_id " + bookId + " was not found.");
        return BookSearchResponseDTO.mapToDTO(book);
    }

    public BookSearchResponseDTO updateBook(BookUpdateRequestDTO bookUpdateRequestDTO, Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null)
            throw new EntityNotFoundException("Book", id);
        book.setIsbn(bookUpdateRequestDTO.getIsbn());
        Publisher publisher = publisherRepository.findById(bookUpdateRequestDTO.getPublisherId()).orElse(null);
        if(publisher == null)
            throw new EntityNotFoundException("Publisher", bookUpdateRequestDTO.getPublisherId());
        book.setPublisher(publisher);
        book.setPrice(bookUpdateRequestDTO.getPrice());
        book.setBookId(book.getBookId());
        Language language = languageRepository.findById(bookUpdateRequestDTO.getLanguageId()).orElse(null);
        if(language == null)
            throw new EntityNotFoundException("Language", bookUpdateRequestDTO.getLanguageId());
        book.setLanguage(language);
        book.setDescription(bookUpdateRequestDTO.getDescription());
        book.setTitle(bookUpdateRequestDTO.getTitle());
        book.setFormat(bookUpdateRequestDTO.getFormat());
        book.setFirstPublishDate(bookUpdateRequestDTO.getFirstPublishDate());
        book.setPublishDate(bookUpdateRequestDTO.getPublishDate());
        book.setEdition(bookUpdateRequestDTO.getEdition());
        return BookSearchResponseDTO.mapToDTO(bookRepository.save(book));
    }

    public void deleteBookById(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null)
            throw new EntityNotFoundException("Book", id);
        bookRepository.delete(book);
    }

    public BookCreateResponseDto createBook(@Valid BookCreateDTO bookCreateDto) {
        String bookId = bookCreateDto.getBookId();
        Book book = bookRepository.findByBookId(bookId);
        if(book != null)
            throw new EntityAlreadyExistsException("Book", "bookId [%s]".formatted(bookId));
        book = new Book();
        book.setBookId(bookId);
        book.setIsbn(bookCreateDto.getIsbn());
        book.setDescription(bookCreateDto.getDescription());
        book.setTitle(bookCreateDto.getTitle());
        book.setFormat(bookCreateDto.getFormat());
        book.setFirstPublishDate(bookCreateDto.getFirstPublishDate());
        book.setPublishDate(bookCreateDto.getPublishDate());
        book.setEdition(bookCreateDto.getEdition());
        book.setPrice(bookCreateDto.getPrice());
        book.setPageNumber(bookCreateDto.getPageNumber());
        Language language = languageRepository.findById(bookCreateDto.getLanguageId()).orElse(null);
        if(language == null)
            throw new EntityNotFoundException("Language", bookCreateDto.getLanguageId());
        book.setLanguage(language);
        Publisher publisher = publisherRepository.findById(bookCreateDto.getPublisherId()).orElse(null);
        if(publisher == null)
            throw new EntityNotFoundException("Publisher", bookCreateDto.getPublisherId());
        book.setPublisher(publisher);
        bookRepository.save(book);
        saveBookAuthors(bookCreateDto.getAuthorIds(), bookCreateDto.getAuthorRoles(), book);
        saveBookSettings(bookCreateDto.getSettingIds(), book);
        saveBookGenres(bookCreateDto.getGenreIds(), book);
        saveBookCharacters(bookCreateDto.getCharacterIds(), book);
        saveBookSeries(bookCreateDto.getSeriesId(), bookCreateDto.getSeriesNumber(), book);
        saveBookAwards(bookCreateDto.getAwardIds(), book);
        FileInformation fileInformation = FileInformation.of(bookCreateDto.getCoverImageURL());
        fileInformationRepository.save(fileInformation);
        BookCoverImage bookCoverImage = new BookCoverImage();
        bookCoverImage.setBook(book);
        bookCoverImage.setFileInformation(fileInformation);
        bookCoverImage.setPictureSize(PictureSize.ORIGINAL);
        bookCoverImageRepository.save(bookCoverImage);
        coverImageService.saveImages(Collections.singletonList(bookCoverImage),0, 1);
        return (new BookCreateResponseDtoMapper()).map(book);
    }

    private void saveBookAwards(List<Long> awardIds, Book book) {
        List<Award> awards = awardRepository.findAllById(awardIds);
        if(awards.size() != awardIds.size())
            throw new EntityNotFoundException("One or more of the provided award ids do not match any existing awards");
        List<BookAward> bookAwards = new ArrayList<>(awards.size());
        for(Award award : awards) {
            BookAward bookAward = new BookAward();
            bookAward.setBook(book);
            bookAward.setAward(award);
            bookAwards.add(bookAward);
        }
        bookAwardRepository.saveAll(bookAwards);
    }


    private void saveBookSeries(Long seriesId, String seriesNumber, Book book) {
        Series series = seriesRepository.findById(seriesId).orElse(null);
        if(series == null)
            throw new EntityNotFoundException("Series", seriesId);
        BookSeries bookSeries = new BookSeries();
        bookSeries.setBook(book);
        bookSeries.setSeries(series);
        bookSeries.setSeriesNumber(seriesNumber);
        bookSeriesRepository.save(bookSeries);
    }

    private void saveBookCharacters(List<Long> characterIds, Book book) {
        List<Character> characters = characterRepository.findAllById(characterIds);
        if(characters.size() != characterIds.size())
            throw new EntityNotFoundException("One or more of the provided character ids do not match any existing characters");
        List<BookCharacter> bookCharacters = new ArrayList<>();
        for (Character character : characters) {
            BookCharacter bookCharacter = new BookCharacter();
            bookCharacter.setBook(book);
            bookCharacter.setCharacter(character);
            bookCharacters.add(bookCharacter);
        }
        bookCharacterRepository.saveAll(bookCharacters);
    }

    private void saveBookGenres(List<Long> genreIds, Book book) {
        List<Genre> genres =  genreRepository.findAllById(genreIds);
        if(genreIds.size() != genres.size())
            throw new EntityNotFoundException("One or more of the provided genre ids do not match any existing genres");
        List<BookGenre> bookGenres = new ArrayList<>();
        for(Genre genre : genres) {
            BookGenre bookGenre = new BookGenre();
            bookGenre.setBook(book);
            bookGenre.setGenre(genre);
            bookGenres.add(bookGenre);
        }
        bookGenreRepository.saveAll(bookGenres);
    }

    private void saveBookSettings(List<Long> settingIds, Book book) {
        List<Setting> settings = settingRepository.findAllById(settingIds);
        if(settings.size() != settingIds.size())
            throw new EntityNotFoundException("One or more of the provided setting ids do not match any existing settings");
        List<BookSetting> bookSettings = new ArrayList<>();
        for(Setting setting : settings)
        {
            BookSetting bookSetting = new BookSetting();
            bookSetting.setBook(book);
            bookSetting.setSetting(setting);
            bookSettings.add(bookSetting);
        }
        bookSettingRepository.saveAll(bookSettings);
    }

    private void saveBookAuthors(List<Long> authorIds, List<AuthorRole> authorRoles, Book book) {
        if(authorRoles.size() != authorIds.size())
            throw new UnsupportedFormatException("Author roles and author ids do not match.");
        List<BookAuthor> bookAuthors = new ArrayList<>();
        int i = 0;
        for(Long authorId : authorIds)
        {
            Author author = authorRepository.findById(authorId).orElse(null);
            if(author == null)
                throw new EntityNotFoundException("Author", authorId);
            BookAuthor bookAuthor = new BookAuthor();
            bookAuthor.setBook(book);
            bookAuthor.setAuthor(author);
            bookAuthor.setAuthorRole(authorRoles.get(i++));
            bookAuthors.add(bookAuthor);
        }
        bookAuthorRepository.saveAll(bookAuthors);

    }

    public List<AuthorResponseDTO> getAuthors(Long id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null)
            throw new EntityNotFoundException("Book", id);
        return bookRepository.findAuthors(id);
    }

    public BookReviewResponseDTO reviewBook(Long id, BookReviewDTO bookReviewDto) {
        Book book = bookRepository.findById(id).orElse(null);
        if(book == null)
            throw new EntityNotFoundException("Book", id);
        Reviews reviews = reviewsRepository.findByBook(book);
        if(reviews == null)
        {
            reviews = new Reviews();
        }
        reviews.setBook(book);
        reviews.setBbeVotes(bookReviewDto.getBbeVotes());
        reviews.setBbeScore(bookReviewDto.getBbeScore());
        reviews.setLikedPercent(bookReviewDto.getLikedPercent());
        reviews.setRating(bookReviewDto.getRating());
        reviewsRepository.save(reviews);
        return BookReviewResponseDTO.mapToDTO(reviews);
    }

    public PageResponseDTO<BookSearchResponseDTO> getAll(BookSearchCriteria criteria) {
        Page<BookSearchResponseDTO> page = bookRepository.findAll(
                criteria,
                criteria.buildPageRequest()
        );

        return PageResponseDTO.from(page);
    }

    public void deleteBooks(List<Long> bookIds) {
        for(Long bookId : bookIds)
        {
            this.deleteBookById(bookId);
        }
    }

    public List<CharacterDTO> getCharacters(Long id) {
       if (!bookRepository.existsById(id))
            throw new EntityNotFoundException("Book", id);
        return bookRepository.findCharacters(id).stream()
                .sorted(Comparator.comparing(CharacterDTO::getFullName))
                .toList();
    }

    public List<GenreDTO> getGenres(Long id) {
        if (!bookRepository.existsById(id))
            throw new EntityNotFoundException("Book", id);
        return bookRepository.findGenres(id).stream()
                .sorted(Comparator.comparing(GenreDTO::getName))
                .toList();
    }
}
