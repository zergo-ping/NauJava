package ru.Nikita.NauJava.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;



import java.util.ArrayList;

import jakarta.persistence.*;

import lombok.EqualsAndHashCode;

import java.util.List;

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

    @OneToMany
    private List<StorageEntity> storages = new ArrayList<>();

    @OneToMany
    private List<LinkEntity> createdLinks = new ArrayList<>();

    @OneToOne
    private StorageQuotaEntity quota;
}