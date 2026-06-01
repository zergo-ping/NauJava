package ru.Nikita.NauJava.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO для представления хранилища файлов
 */
public class StorageDTO {

    private Long id;
    private LocalDateTime uploadedAt;
    private String mimeType;
    private List<FileDTO> files = new ArrayList<>();

    public StorageDTO() {
    }

    public StorageDTO(Long id, LocalDateTime uploadedAt, String mimeType) {
        this.id = id;
        this.uploadedAt = uploadedAt;
        this.mimeType = mimeType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    public void setFiles(List<FileDTO> files) {
        this.files = files;
    }
}