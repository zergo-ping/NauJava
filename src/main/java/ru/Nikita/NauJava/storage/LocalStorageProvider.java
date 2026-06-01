package ru.Nikita.NauJava.storage;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Локальная реализация {@link StorageProvider}.
 *
 * <p>Файлы хранятся в директории файловой системы, задаваемой
 * свойством {@code storage.local.path}. При запуске директория
 * создаётся автоматически, если отсутствует.</p>
 *
 * <p>Не используется как Spring-бин по умолчанию — вместо неё активен
 * {@link MinioStorageProvider}.</p>
 */
public class LocalStorageProvider implements StorageProvider {

    private static final Logger logger = LoggerFactory.getLogger(LocalStorageProvider.class);

    @Value("${storage.local.path:/app/uploads}")
    private String storagePath;

    private Path root;

    @PostConstruct
    public void init() {
        this.root = Paths.get(storagePath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(root);
            logger.info("Локальное хранилище инициализировано: {}", root);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию хранилища: " + root, e);
        }
    }

    @Override
    public void store(String relativePath, InputStream inputStream, long size) {
        Path target = resolve(relativePath);
        try {
            Files.createDirectories(target.getParent());
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            logger.debug("Файл сохранён: {} ({} байт)", target, size);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка сохранения файла: " + relativePath, e);
        }
    }

    @Override
    public Resource loadAsResource(String relativePath) {
        Path file = resolve(relativePath);
        return new FileSystemResource(file.toFile());
    }

    @Override
    public boolean delete(String relativePath) {
        Path file = resolve(relativePath);
        try {
            return Files.deleteIfExists(file);
        } catch (IOException e) {
            logger.warn("Не удалось удалить файл: {}", file, e);
            return false;
        }
    }

    @Override
    public boolean exists(String relativePath) {
        return Files.exists(resolve(relativePath));
    }

    private Path resolve(String relativePath) {
        Path resolved = root.resolve(relativePath).normalize();
        if (!resolved.startsWith(root)) {
            throw new SecurityException("Попытка доступа за пределы хранилища: " + relativePath);
        }
        return resolved;
    }
}
