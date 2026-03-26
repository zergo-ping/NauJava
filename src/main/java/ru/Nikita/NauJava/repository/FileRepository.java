package ru.Nikita.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import ru.Nikita.NauJava.entity.FileEntity;
import ru.Nikita.NauJava.entity.StorageEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с файлами в хранилище
 */
public interface FileRepository extends CrudRepository<FileEntity, Long> {

    /**
     * Поиск всех файлов в указанном хранилище
     * 
     * @param storage хранилище
     * @return список файлов в хранилище
     */
    List<FileEntity> findByStorage(StorageEntity storage);

    /**
     * Поиск файлов по диапазону размеров и минимальной дате загрузки
     * 
     * @param minSize минимальный размер в байтах
     * @param maxSize максимальный размер в байтах
     * @param date минимальная дата загрузки
     * @return список файлов, соответствующих критериям
     */
    List<FileEntity> findByFileSizeBetweenAndUploadedAtAfter(Long minSize,
                                                       Long maxSize,
                                                       LocalDateTime date);
    
    /**
     * Поиск файлов по частичному совпадению имени (без учёта регистра)
     * 
     * @param fileNamePart часть имени файла для поиска
     * @return список файлов, имя которых содержит указанную часть
     */
    List<FileEntity> findByFileNameContainingIgnoreCase(String fileNamePart);

    /**
     * Подсчёт количества файлов в хранилище
     * 
     * @param storage хранилище
     * @return количество файлов
     */
    long countByStorage(StorageEntity storage);
}