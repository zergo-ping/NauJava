package ru.Nikita.NauJava.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Сущность, представляющая пользователя системы.
 */
@Entity
@Table(name = "users")
@Data
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "role", nullable = false)
    private String role = "USER";

    @OneToMany
    private List<StorageEntity> storages = new ArrayList<>();

    @OneToMany
    private List<LinkEntity> createdLinks = new ArrayList<>();

    @OneToOne
    private StorageQuotaEntity quota;
}