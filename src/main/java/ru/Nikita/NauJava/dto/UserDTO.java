package ru.Nikita.NauJava.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO для представления пользователя со всеми связанными данными
 */
public class UserDTO {

    private Long id;
    private String email;
    private String fullName;
    private String role;
    private List<StorageDTO> storages = new ArrayList<>();
    private List<LinkDTO> createdLinks = new ArrayList<>();

    public UserDTO() {
    }

    public UserDTO(Long id, String email, String fullName, String role) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<StorageDTO> getStorages() {
        return storages;
    }

    public void setStorages(List<StorageDTO> storages) {
        this.storages = storages;
    }

    public List<LinkDTO> getCreatedLinks() {
        return createdLinks;
    }

    public void setCreatedLinks(List<LinkDTO> createdLinks) {
        this.createdLinks = createdLinks;
    }
}