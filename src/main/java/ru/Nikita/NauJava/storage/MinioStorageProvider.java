package ru.Nikita.NauJava.storage;

import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Реализация {@link StorageProvider} на базе MinIO (S3-совместимое хранилище).
 */
@Component
public class MinioStorageProvider implements StorageProvider {

    private static final Logger logger = LoggerFactory.getLogger(MinioStorageProvider.class);

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;

    public MinioStorageProvider(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @PostConstruct
    public void init() {
        try {
            boolean exists = minioClient.bucketExists(
                    BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!exists) {
                minioClient.makeBucket(
                        MakeBucketArgs.builder().bucket(bucketName).build()
                );
                logger.info("Бакет '{}' создан", bucketName);
            }
            logger.info("MinIO хранилище инициализировано, бакет: {}", bucketName);
        } catch (Exception e) {
            throw new RuntimeException("Не удалось инициализировать бакет MinIO: " + bucketName, e);
        }
    }

    @Override
    public void store(String relativePath, InputStream inputStream, long size) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(relativePath)
                            .stream(inputStream, size, -1)
                            .build()
            );
            logger.debug("Файл сохранён в MinIO: {} ({} байт)", relativePath, size);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка сохранения файла в MinIO: " + relativePath, e);
        }
    }

    @Override
    public Resource loadAsResource(String relativePath) {
        try {
            InputStream stream = minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(relativePath)
                            .build()
            );
            return new InputStreamResource(stream);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка загрузки файла из MinIO: " + relativePath, e);
        }
    }

    @Override
    public boolean delete(String relativePath) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(relativePath)
                            .build()
            );
            logger.debug("Файл удалён из MinIO: {}", relativePath);
            return true;
        } catch (Exception e) {
            logger.warn("Не удалось удалить файл из MinIO: {}", relativePath, e);
            return false;
        }
    }

    @Override
    public boolean exists(String relativePath) {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(relativePath)
                            .build()
            );
            return true;
        } catch (ErrorResponseException e) {
            return false;
        } catch (Exception e) {
            logger.warn("Ошибка проверки существования файла в MinIO: {}", relativePath, e);
            return false;
        }
    }
}
