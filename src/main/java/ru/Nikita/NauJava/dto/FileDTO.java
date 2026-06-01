package ru.Nikita.NauJava.dto;

import java.time.LocalDateTime;

/**
 * DTO для представления файла
 */
public class FileDTO {

    private Long id;
    private String fileName;
    private Long fileSize;
    private String mimeType;
    private LocalDateTime uploadedAt;


    public FileDTO(Long id, String fileName, Long fileSize, String mimeType, LocalDateTime uploadedAt) {
        this.id = id;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.mimeType = mimeType;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }
}