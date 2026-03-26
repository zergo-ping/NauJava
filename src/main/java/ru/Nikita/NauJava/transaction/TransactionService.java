package ru.Nikita.NauJava.transaction;


import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.Nikita.NauJava.entity.FileEntity;
import ru.Nikita.NauJava.entity.StorageEntity;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.FileRepository;
import ru.Nikita.NauJava.repository.StorageEntityRepository;
import ru.Nikita.NauJava.repository.UserRepository;

import java.time.LocalDateTime;

/**
 * Сервис для работы с транзакциями при создании хранилищ и файлов
 * 
 * Демонстрирует различные сценарии работы с программными транзакциями,
 * включая успешные операции, обработку ошибок и откат транзакций
 */
@Service
public class TransactionService {

    private final PlatformTransactionManager transactionManager;
    private final UserRepository userRepository;
    private final StorageEntityRepository storageRepository;
    private final FileRepository fileRepository;

    /**
     * Конструктор сервиса транзакций
     * 
     * @param transactionManager менеджер транзакций
     * @param userRepository репозиторий пользователей
     * @param storageRepository репозиторий хранилищ
     * @param fileRepository репозиторий файлов
     */
    public TransactionService(PlatformTransactionManager transactionManager,
                                     UserRepository userRepository,
                                     StorageEntityRepository storageRepository,
                                     FileRepository fileRepository) {
        this.transactionManager = transactionManager;
        this.userRepository = userRepository;
        this.storageRepository = storageRepository;
        this.fileRepository = fileRepository;
    }

    /**
     * Создаёт новое хранилище для пользователя и добавляет в него файл
     * При успехе обе операции фиксируются в БД, при ошибке - откатываются
     * 
     * @param userId идентификатор пользователя
     * @param fileName имя файла
     * @throws RuntimeException если пользователь не найден или произойдёт ошибка при сохранении
     */
    public void createStorageAndFileSuccess(Long userId, String fileName) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("createStorageAndFileTransaction");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            StorageEntity storage = new StorageEntity();
            storage.setUser(user);
            storage.setMimeType("image/jpeg");
            storage.setUploadedAt(LocalDateTime.now());
            storageRepository.save(storage);

            FileEntity file = new FileEntity();
            file.setStorage(storage);
            file.setFileName(fileName);
            file.setFileSize(1024L);
            file.setMimeType("image/jpeg");
            file.setPath("/uploads/" + fileName + "_" + System.currentTimeMillis());
            file.setUploadedAt(LocalDateTime.now());
            fileRepository.save(file);

            transactionManager.commit(status);

        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new RuntimeException("Ошибка при создании хранилища и файла", e);
        }
    }

    /**
     * Демонстрирует откат транзакции при возникновении исключения
     * Хранилище и файл не будут сохранены в БД
     * 
     * @param userId идентификатор пользователя
     * @param fileName имя файла
     * @throws RuntimeException всегда выбрасывает исключение для демонстрации отката
     */
    public void createStorageAndFileWithError(Long userId, String fileName) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("createStorageAndFileWithErrorTransaction");

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            StorageEntity storage = new StorageEntity();
            storage.setUser(user);
            storage.setMimeType("image/jpeg");
            storage.setUploadedAt(LocalDateTime.now());
            storageRepository.save(storage);

            FileEntity file = new FileEntity();
            file.setStorage(storage);
            file.setFileName(fileName);
            file.setFileSize(1024L);
            file.setMimeType("image/jpeg");
            file.setPath("/uploads/" + fileName);
            file.setUploadedAt(LocalDateTime.now());
            fileRepository.save(file);

            //  ошибка для демонстрации отката
            int x = 10 / 0;

            transactionManager.commit(status);

        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new RuntimeException("Ошибка при создании", e);
        }
    }

    /**
     * Демонстрирует откат транзакции при попытке работать с несуществующим пользователем
     * 
     * @param userId идентификатор пользователя
     * @param fileName имя файла (не используется)
     * @throws RuntimeException всегда выбрасывает исключение, так как пользователь не существует
     */
    public void createStorageWithInvalidUser(Long userId, String fileName) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("createWithInvalidUserTransaction");

        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            UserEntity user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

            StorageEntity storage = new StorageEntity();
            storage.setUser(user);
            storage.setMimeType("image/jpeg");
            storage.setUploadedAt(LocalDateTime.now());
            storageRepository.save(storage);

            transactionManager.commit(status);

        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new RuntimeException("Ошибка: пользователь не существует", e);
        }
    }
}