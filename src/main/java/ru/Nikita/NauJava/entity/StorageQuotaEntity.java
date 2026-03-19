package ru.Nikita.NauJava.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

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

    public boolean hasAvailableSpace(Long fileSize) {
        return (usedBytes + fileSize) <= maxBytes;
    }

    public void addUsedBytes(Long bytes) {
        this.usedBytes += bytes;
    }

    public void removeUsedBytes(Long bytes) {
        this.usedBytes = Math.max(0, this.usedBytes - bytes);
    }
}
