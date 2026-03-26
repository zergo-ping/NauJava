package ru.Nikita.NauJava.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Сущность, представляющая лог доступа к файлам через ссылку
 */
@Entity
@Table(name = "file_access_log")
@Data
public class FileAccessLogEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "link_id", nullable = false)
    private LinkEntity link;

    @ManyToOne
    @JoinColumn(name = "storage_id", nullable = false)
    private StorageEntity storage;

    @Column(name = "accessed_by_ip", columnDefinition = "inet")
    private String accessedByIp;

    @Column(name = "user_agent", columnDefinition = "text")
    private String userAgent;

    @CreationTimestamp
    @Column(name = "accessed_at", updatable = false)
    private LocalDateTime accessedAt;
}
