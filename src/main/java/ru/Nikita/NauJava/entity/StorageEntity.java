package ru.Nikita.NauJava.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany
    private List<FileEntity> files = new ArrayList<>();

    @OneToMany
    private List<LinkEntity> links = new ArrayList<>();


}
