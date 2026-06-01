package ru.Nikita.NauJava.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность, представляющая хранилище файлов пользователя.
 * 
 * Хранилище содержит файлы пользователя.
 * Каждое хранилище принадлежит конкретному пользователю.
 */
@Entity
@Table(name = "storages")
@Data
public class StorageEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @OneToMany(mappedBy = "storage", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileEntity> files = new ArrayList<>();
}
