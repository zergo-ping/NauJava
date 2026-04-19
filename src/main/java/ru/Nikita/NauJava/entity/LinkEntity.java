package ru.Nikita.NauJava.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Сущность, представляющая ссылку для совместного использования файлов
 */
@Entity
@Table(name = "links")
@Data
public class LinkEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "storage_id", nullable = false)
    private StorageEntity storage;


    @Column(unique = true, nullable = false)
    private String token;

    @Column(name = "max_downloads")
    private Integer maxDownloads;

    @Column(name = "current_downloads")
    private Integer currentDownloads = 0;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;



    /**
     * Проверяет, является ли ссылка действительной
     * 
     * Ссылка считается недействительной, если
     * прошла дата истечения, или достигнут максимальный лимит скачиваний
     * 
     * @return true, если ссылка действительна; false в противном случае
     */
    public boolean isValid() {
        if (expiresAt != null && expiresAt.isBefore(LocalDateTime.now())) return false;
        if (maxDownloads != null && currentDownloads >= maxDownloads) return false;
        return true;
    }
}
