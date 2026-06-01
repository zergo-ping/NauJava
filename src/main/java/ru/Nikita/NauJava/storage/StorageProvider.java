package ru.Nikita.NauJava.storage;

import org.springframework.core.io.Resource;

import java.io.InputStream;

/**
 * Абстракция провайдера файлового хранилища.
 *
 * <p>Позволяет прозрачно переключаться между локальным хранением,
 * S3 и другими реализациями без изменения бизнес-логики.</p>
 */
public interface StorageProvider {

    /**
     * Сохраняет файл в хранилище.
     *
     * @param relativePath относительный путь внутри хранилища
     * @param inputStream  поток данных файла
     * @param size         размер файла в байтах
     */
    void store(String relativePath, InputStream inputStream, long size);

    /**
     * Загружает файл из хранилища как {@link Resource}.
     *
     * @param relativePath относительный путь
     * @return ресурс для чтения
     */
    Resource loadAsResource(String relativePath);

    /**
     * Удаляет файл из хранилища.
     *
     * @param relativePath относительный путь
     * @return true, если файл был удалён
     */
    boolean delete(String relativePath);

    /**
     * Проверяет существование файла.
     *
     * @param relativePath относительный путь
     * @return true, если файл существует
     */
    boolean exists(String relativePath);
}
