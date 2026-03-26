package ru.Nikita.NauJava.criteria;

import ru.Nikita.NauJava.entity.StorageEntity;

import java.util.List;

/**
 * Интерфейс для кастомных запросов к хранилищам с использованием Criteria API
 * 
 * Предоставляет методы для сложных поисков с использованием Criteria Query
 * вместо простых методов репозитория
 */
public interface CustomStorageRepository {

    /**
     * Поиск хранилищ пользователя по MIME-типу
     * 
     * Использует Criteria API для создания динамического запроса
     * 
     * @param userId идентификатор пользователя
     * @param mimeType MIME-тип для фильтрации
     * @return список хранилищ пользователя, соответствующих MIME-типу,
     *         отсортированные по дате загрузки в убывающем порядке
     */
    List<StorageEntity> findStoragesByUserAndMimeType(Long userId, String mimeType);

    /**
     * Поиск хранилищ с минимальным количеством файлов
     * 
     * Использует группировку и агрегацию для подсчёта файлов в каждом хранилище
     * 
     * @param minFilesCount минимальное количество файлов в хранилище
     * @return список хранилищ с количеством файлов не менее указанного,
     *         отсортированные по количеству файлов в убывающем порядке
     */
    List<StorageEntity> findStoragesWithMinFiles(int minFilesCount);
}
