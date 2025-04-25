package com.example.bookstore.persistance.entity;

import com.example.bookstore.enums.FileDownloadStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="file_information")
@Setter
@Getter
public class FileInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_information_id_seq")
    @SequenceGenerator(name = "file_information_id_seq", sequenceName = "file_information_id_seq", allocationSize = 50)
    private Long id;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_format")
    private String fileFormat;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private FileDownloadStatus status;

    @Column(name = "error_message")
    private String errorMessage;


    public static FileInformation of(String url)
    {
        FileInformation fileInformation = new FileInformation();
        fileInformation.setFileUrl(url);
        fileInformation.setStatus(FileDownloadStatus.PENDING);
        return fileInformation;
    }
}
