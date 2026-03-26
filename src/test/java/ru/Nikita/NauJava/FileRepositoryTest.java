package ru.Nikita.NauJava;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.Nikita.NauJava.entity.FileEntity;
import ru.Nikita.NauJava.entity.StorageEntity;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.FileRepository;
import ru.Nikita.NauJava.repository.StorageEntityRepository;
import ru.Nikita.NauJava.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FileRepositoryTest {
    private final FileRepository fileRepository;

    private final StorageEntityRepository storageRepository;

    private final UserRepository userRepository;

    private StorageEntity storage;
    private FileEntity file1;
    private FileEntity file2;

    @Autowired
    public FileRepositoryTest(FileRepository fileRepository,
                              StorageEntityRepository storageRepository,
                              UserRepository userRepository)
    {
        this.fileRepository = fileRepository;
        this.storageRepository = storageRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        fileRepository.deleteAll();
        storageRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setEmail("user@example.com");
        user.setPasswordHash("hash");
        user.setFullName("Test User");
        user = userRepository.save(user);

        storage = new StorageEntity();
        storage.setUser(user);
        storage.setMimeType("image/jpeg");
        storage.setUploadedAt(LocalDateTime.now());
        storage = storageRepository.save(storage);

        file1 = new FileEntity();
        file1.setStorage(storage);
        file1.setFileName("photo1.jpg");
        file1.setFileSize(1024L);
        file1.setMimeType("image/jpeg");
        file1.setPath("/path/1.jpg");
        file1.setUploadedAt(LocalDateTime.now().minusDays(5));
        file1 = fileRepository.save(file1);

        file2 = new FileEntity();
        file2.setStorage(storage);
        file2.setFileName("photo2.jpg");
        file2.setFileSize(2048L);
        file2.setMimeType("image/jpeg");
        file2.setPath("/path/2.jpg");
        file2.setUploadedAt(LocalDateTime.now());
        file2 = fileRepository.save(file2);
    }

    @Test
    void testFindByStorage() {
        List<FileEntity> files = fileRepository.findByStorage(storage);
        assertEquals(2, files.size());
    }

    @Test
    void testFindByFileSizeBetweenAndUploadedAtAfter() {
        List<FileEntity> files = fileRepository.findByFileSizeBetweenAndUploadedAtAfter(
                1000L, 3000L, LocalDateTime.now().minusDays(10));
        assertEquals(2, files.size());

        List<FileEntity> empty = fileRepository.findByFileSizeBetweenAndUploadedAtAfter(
                5000L, 10000L, LocalDateTime.now().minusDays(10));
        assertEquals(0, empty.size());
    }

    @Test
    void testFindByFileNameContainingIgnoreCase() {
        List<FileEntity> files = fileRepository.findByFileNameContainingIgnoreCase("PHOTO");
        assertEquals(2, files.size());

        List<FileEntity> empty = fileRepository.findByFileNameContainingIgnoreCase("document");
        assertEquals(0, empty.size());
    }

    @Test
    void testCountByStorage() {
        long count = fileRepository.countByStorage(storage);
        assertEquals(2, count);
    }


}