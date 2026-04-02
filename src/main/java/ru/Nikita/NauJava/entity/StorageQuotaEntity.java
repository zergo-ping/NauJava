package ru.Nikita.NauJava.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Сущность, представляющая квоту дискового пространства пользователя.
 */
@Entity
@Table(name = "storage_quota")
@Data
public class StorageQuotaEntity {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

    @Column(name = "max_bytes", nullable = false)
    private Long maxBytes = 1073741824L;

    @Column(name = "used_bytes", nullable = false)
    private Long usedBytes = 0L;

    @UpdateTimestamp
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    /**
     * Проверяет, достаточно ли свободного пространства для файла указанного размера
     * 
     * @param fileSize размер файла в байтах
     * @return true, если достаточно свободного места; false в противном случае
     */
    public boolean hasAvailableSpace(Long fileSize) {
        return (usedBytes + fileSize) <= maxBytes;
    }

    /**
     * Увеличивает количество используемого пространства
     * 
     * @param bytes количество байтов, которое нужно добавить
     */
    public void addUsedBytes(Long bytes) {
        this.usedBytes += bytes;
    }

    /**
     * Уменьшает количество используемого пространства
     *
     * @param bytes количество байтов, которое нужно вычесть
     */
    public void removeUsedBytes(Long bytes) {
        this.usedBytes = Math.max(0, this.usedBytes - bytes);
    }
}
