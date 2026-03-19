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

@Service
public class TransactionService {

    private final PlatformTransactionManager transactionManager;
    private final UserRepository userRepository;
    private final StorageEntityRepository storageRepository;
    private final FileRepository fileRepository;

    public TransactionService(PlatformTransactionManager transactionManager,
                                     UserRepository userRepository,
                                     StorageEntityRepository storageRepository,
                                     FileRepository fileRepository) {
        this.transactionManager = transactionManager;
        this.userRepository = userRepository;
        this.storageRepository = storageRepository;
        this.fileRepository = fileRepository;
    }

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

    //транзакция с ошибкой
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

            //  ошибка
            int x = 10 / 0;

            transactionManager.commit(status);

        } catch (Exception e) {
            transactionManager.rollback(status);
            throw new RuntimeException("Ошибка при создании", e);
        }
    }

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