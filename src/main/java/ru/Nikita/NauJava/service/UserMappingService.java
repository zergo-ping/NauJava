package ru.Nikita.NauJava.service;

import org.springframework.stereotype.Service;
import ru.Nikita.NauJava.dto.FileDTO;
import ru.Nikita.NauJava.dto.LinkDTO;
import ru.Nikita.NauJava.dto.StorageDTO;
import ru.Nikita.NauJava.dto.UserDTO;
import ru.Nikita.NauJava.entity.FileEntity;
import ru.Nikita.NauJava.entity.LinkEntity;
import ru.Nikita.NauJava.entity.StorageEntity;
import ru.Nikita.NauJava.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для преобразования сущностей в DTO
 */
@Service
public class UserMappingService {

    /**
     * Преобразует сущность пользователя в DTO со всеми связанными данными
     */
    public UserDTO mapUserToDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole()
        );

        if (user.getStorages() != null) {
            List<StorageDTO> storageDTOs = user.getStorages().stream()
                    .map(this::mapStorageToDTO)
                    .collect(Collectors.toList());
            userDTO.setStorages(storageDTOs);
        }

        if (user.getCreatedLinks() != null) {
            List<LinkDTO> linkDTOs = user.getCreatedLinks().stream()
                    .map(this::mapLinkToDTO)
                    .collect(Collectors.toList());
            userDTO.setCreatedLinks(linkDTOs);
        }

        return userDTO;
    }

    /**
     * Преобразует сущность хранилища в DTO
     */
    private StorageDTO mapStorageToDTO(StorageEntity storage) {
        StorageDTO storageDTO = new StorageDTO(
                storage.getId(),
                storage.getUploadedAt(),
                storage.getMimeType()
        );

        if (storage.getFiles() != null) {
            List<FileDTO> fileDTOs = storage.getFiles().stream()
                    .map(this::mapFileToDTO)
                    .collect(Collectors.toList());
            storageDTO.setFiles(fileDTOs);
        }

        return storageDTO;
    }

    /**
     * Преобразует сущность файла в DTO
     */
    private FileDTO mapFileToDTO(FileEntity file) {
        return new FileDTO(
                file.getId(),
                file.getFileName(),
                file.getFileSize(),
                file.getMimeType(),
                file.getUploadedAt()
        );
    }

    /**
     * Преобразует сущность ссылки в DTO
     */
    private LinkDTO mapLinkToDTO(LinkEntity link) {
        String fileName = (link.getFile() != null) ? link.getFile().getFileName() : null;
        boolean isValid = link.isValid();

        return new LinkDTO(
                link.getId(),
                link.getToken(),
                link.getMaxDownloads(),
                link.getCurrentDownloads(),
                link.getExpiresAt(),
                link.getCreatedAt(),
                fileName,
                isValid
        );
    }
}