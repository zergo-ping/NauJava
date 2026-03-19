package ru.Nikita.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.Nikita.NauJava.entity.FileEntity;
import ru.Nikita.NauJava.entity.StorageEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface FileRepository extends CrudRepository<FileEntity, Long> {

    List<FileEntity> findByStorage(StorageEntity storage);

    List<FileEntity> findByFileSizeBetweenAndUploadedAtAfter(Long minSize,
                                                       Long maxSize,
                                                       LocalDateTime date);
    List<FileEntity> findByFileNameContainingIgnoreCase(String fileNamePart);

    long countByStorage(StorageEntity storage);
}