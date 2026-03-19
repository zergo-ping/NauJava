package ru.Nikita.NauJava.criteria;

import ru.Nikita.NauJava.entity.StorageEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomStorageRepository {

    // Поиск хранилищ по пользователю и MIME-типу
    List<StorageEntity> findStoragesByUserAndMimeType(Long userId, String mimeType);

    // Поиск хранилищ с минимальным количеством файлов
    List<StorageEntity> findStoragesWithMinFiles(int minFilesCount);
}
