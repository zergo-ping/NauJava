package ru.Nikita.NauJava.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @ManyToOne
    @JoinColumn(name = "created_by_user_id", nullable = false)
    private UserEntity createdBy;

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



    public boolean isValid() {
        if (expiresAt != null && expiresAt.isBefore(LocalDateTime.now())) return false;
        if (maxDownloads != null && currentDownloads >= maxDownloads) return false;
        return true;
    }
}
