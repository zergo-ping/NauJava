package ru.Nikita.NauJava.dto;

import java.time.LocalDateTime;

/**
 * DTO для представления ссылки для совместного использования файлов
 */
public class LinkDTO {

    private Long id;
    private String token;
    private Integer maxDownloads;
    private Integer currentDownloads;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
    private String fileName;
    private boolean valid;

    public LinkDTO() {
    }

    public LinkDTO(Long id, String token, Integer maxDownloads, Integer currentDownloads,
                   LocalDateTime expiresAt, LocalDateTime createdAt, String fileName, boolean valid) {
        this.id = id;
        this.token = token;
        this.maxDownloads = maxDownloads;
        this.currentDownloads = currentDownloads;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
        this.fileName = fileName;
        this.valid = valid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getMaxDownloads() {
        return maxDownloads;
    }

    public void setMaxDownloads(Integer maxDownloads) {
        this.maxDownloads = maxDownloads;
    }

    public Integer getCurrentDownloads() {
        return currentDownloads;
    }

    public void setCurrentDownloads(Integer currentDownloads) {
        this.currentDownloads = currentDownloads;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Возвращает количество оставшихся скачиваний
     */
    public Integer getRemainingDownloads() {
        if (maxDownloads == null) {
            return null;
        }
        return maxDownloads - currentDownloads;
    }
}