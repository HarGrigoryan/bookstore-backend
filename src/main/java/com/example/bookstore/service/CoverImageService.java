package com.example.bookstore.service;

import com.example.bookstore.enums.FileDownloadStatus;
import com.example.bookstore.enums.PictureSize;
import com.example.bookstore.persistance.entity.Book;
import com.example.bookstore.persistance.entity.BookCoverImage;
import com.example.bookstore.persistance.entity.FileInformation;
import com.example.bookstore.persistance.repository.BookCoverImageRepository;
import com.example.bookstore.persistance.repository.FileInformationRepository;
import com.example.bookstore.util.Utilities;
import jakarta.transaction.Transactional;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CoverImageService {

    private final FileInformationRepository fileInformationRepository;

    @Value("${app.image.root.path}")
    private String IMAGES_ROOT;

    @Value("${app.image.medium.size}")
    private Integer MEDIUM_SIZE;

    @Value("${app.image.thumbnail.size}")
    private Integer THUMBNAIL_SIZE;

    @Value("${cover.images.count.per.process}")
    private Integer COUNT_PER_PROCESS;

    private final BookCoverImageRepository bookCoverImageRepository;

    @Autowired
    public CoverImageService(FileInformationRepository fileInformationRepository, BookCoverImageRepository bookCoverImageRepository) {
        this.fileInformationRepository = fileInformationRepository;
        this.bookCoverImageRepository = bookCoverImageRepository;
    }

    public void saveImages(List<BookCoverImage> bookCoverImages, int from, int upTo)
    {
        int size = upTo - from + 1;
        List<FileInformation> coverImageFilesToSave = new ArrayList<>(size * 3);
        List<BookCoverImage> bookCoverImagesToSave = new ArrayList<>(size * 2);
        for (int i = from; i < upTo && i < bookCoverImages.size(); i++) {
            Book book = bookCoverImages.get(i).getBook();
            String bookId = book.getBookId();
            String path = IMAGES_ROOT;
            String title = book.getTitle();
            FileInformation coverImageInformation = bookCoverImages.get(i).getFileInformation();
            //bookCoverImagesToSave.add(createBookCoverImage(book, coverImageInformation, PictureSize.ORIGINAL));
            String url = coverImageInformation.getFileUrl();
            FileInformation smallCoverImageInformation = FileInformation.of(url);
            bookCoverImagesToSave.add(createBookCoverImage(book, smallCoverImageInformation, PictureSize.THUMBNAIL));
            FileInformation mediumCoverImageInformation = FileInformation.of(url);
            bookCoverImagesToSave.add(createBookCoverImage(book, mediumCoverImageInformation, PictureSize.MEDIUM));
            char firstTitleChar = title.charAt(0);
            if (Utilities.isLetter(firstTitleChar))
                path += "/" + String.valueOf(firstTitleChar).toLowerCase();
            if (title.length() > 1 && Utilities.isLetter(title.charAt(1))) {
                path += "/" + String.valueOf(title.charAt(1)).toLowerCase();
            }
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String cleanBookId = bookId.replaceAll("[^a-zA-Z0-9]", "-");
            String fileName =  cleanBookId + ".jpg";
            File coverImageDestination = new File(dir, fileName);
            try {
                coverImageInformation.setStatus(FileDownloadStatus.DOWNLOADING);
                URLConnection connection = new URL(url).openConnection();
                connection.setConnectTimeout(1000);
                connection.setReadTimeout(1000);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                FileUtils.copyInputStreamToFile(inputStream, coverImageDestination);
                completeFileInformation(coverImageInformation, fileName, coverImageDestination.getAbsolutePath(), Files.probeContentType(coverImageDestination.toPath()));
            } catch (Exception e) {
                coverImageInformation.setStatus(FileDownloadStatus.FAILED);
                coverImageInformation.setErrorMessage(e.getMessage());
            }
            finally {
                coverImageFilesToSave.add(coverImageInformation);
            }
            try{
                mediumCoverImageInformation.setStatus(FileDownloadStatus.DOWNLOADING);
                String mediumFileName = cleanBookId + "-Medium.jpg";
                String pathForMedium = dir.getPath()+ "\\" + mediumFileName;
                Thumbnails.of(coverImageDestination.getPath()).size(MEDIUM_SIZE,MEDIUM_SIZE).toFile(pathForMedium);
                Path mediumCoverIamgePath = Paths.get(pathForMedium);
                completeFileInformation(mediumCoverImageInformation, mediumFileName, mediumCoverIamgePath.toAbsolutePath().toString(), Files.probeContentType(mediumCoverIamgePath));
            }catch (Exception e){
                mediumCoverImageInformation.setStatus(FileDownloadStatus.FAILED);
                mediumCoverImageInformation.setErrorMessage(e.getMessage());
            }finally {
                coverImageFilesToSave.add(mediumCoverImageInformation);
            }
            try{
                smallCoverImageInformation.setStatus(FileDownloadStatus.DOWNLOADING);
                String thumbnailFileName = cleanBookId + "-Thumbnail.jpg";
                String pathForThumbnail = dir.getPath() + "\\" + thumbnailFileName;
                Thumbnails.of(coverImageDestination.getPath()).size(THUMBNAIL_SIZE, THUMBNAIL_SIZE).toFile(pathForThumbnail);
                Path smallCoverImagePath = Paths.get(pathForThumbnail);
                completeFileInformation(smallCoverImageInformation, thumbnailFileName, smallCoverImagePath.toAbsolutePath().toString(), Files.probeContentType(smallCoverImagePath));
            }catch (Exception e){
                smallCoverImageInformation.setStatus(FileDownloadStatus.FAILED);
                smallCoverImageInformation.setErrorMessage(e.getMessage());
            }
            finally {
                coverImageFilesToSave.add(smallCoverImageInformation);
            }
            System.out.println("Executing save image: " + i +". File name: " + fileName);
        }
        System.out.println("Finished executions");
        fileInformationRepository.saveAll(coverImageFilesToSave);
        bookCoverImageRepository.saveAll(bookCoverImagesToSave);
        System.out.println("Finished saving images");


    }

    @Scheduled(cron = "${scheduler.daily.cron}", zone = "${scheduler.daily.zone}")
    @Transactional
    public void downloadImages(){
        List<BookCoverImage> bookCoverImages = bookCoverImageRepository.findPendingTop(COUNT_PER_PROCESS);
        saveImages(bookCoverImages, 0, COUNT_PER_PROCESS);
    }

    private static void completeFileInformation(FileInformation coverImageInformation, String fileName, String absolutePath, String fileFormat) {
        coverImageInformation.setFileName(fileName);
        coverImageInformation.setFilePath(absolutePath);
        coverImageInformation.setFileFormat(fileFormat);
        coverImageInformation.setStatus(FileDownloadStatus.COMPLETED);
    }

    private static BookCoverImage createBookCoverImage(Book book, FileInformation coverImageInformation, PictureSize pictureSize) {
        BookCoverImage bookCoverImage = new BookCoverImage();
        bookCoverImage.setBook(book);
        bookCoverImage.setFileInformation(coverImageInformation);
        bookCoverImage.setPictureSize(pictureSize);
        return bookCoverImage;
    }


    public String getImage(Long bookId, PictureSize pictureSize) {
        return bookCoverImageRepository.getImage(bookId, pictureSize);
    }
}

