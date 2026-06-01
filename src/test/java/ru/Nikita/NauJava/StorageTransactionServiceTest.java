package ru.Nikita.NauJava;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.FileRepository;
import ru.Nikita.NauJava.repository.StorageEntityRepository;
import ru.Nikita.NauJava.repository.UserRepository;
import ru.Nikita.NauJava.transaction.TransactionService;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StorageTransactionServiceTest {

	private  final TransactionService transactionService;

	private final UserRepository userRepository;

	private final StorageEntityRepository storageRepository;

	private final FileRepository fileRepository;

	private UserEntity testUser;

	@Autowired
    public StorageTransactionServiceTest(TransactionService transactionService, UserRepository userRepository, StorageEntityRepository storageRepository, FileRepository fileRepository) {
        this.transactionService = transactionService;
        this.userRepository = userRepository;
        this.storageRepository = storageRepository;
        this.fileRepository = fileRepository;
    }

    @BeforeEach
	void setUp() {
		fileRepository.deleteAll();
		storageRepository.deleteAll();
		userRepository.deleteAll();

		testUser = new UserEntity();
		testUser.setEmail("test@example.com");
		testUser.setPasswordHash("hash123");
		testUser.setFullName("Test User");
		testUser = userRepository.save(testUser);
	}

	@Test
	void testSuccessTransaction() {
		long storageCountBefore = storageRepository.count();
		long fileCountBefore = fileRepository.count();

		transactionService.createStorageAndFileSuccess(testUser.getId(), "test.jpg");

		long storageCountAfter = storageRepository.count();
		long fileCountAfter = fileRepository.count();

		assertEquals(storageCountBefore + 1, storageCountAfter);
		assertEquals(fileCountBefore + 1, fileCountAfter);
	}

	@Test
	void testTransactionWithError() {
		long storageCountBefore = storageRepository.count();
		long fileCountBefore = fileRepository.count();

		assertThrows(RuntimeException.class, () -> {
			transactionService.createStorageAndFileWithError(testUser.getId(), "error.jpg");
		});

		long storageCountAfter = storageRepository.count();
		long fileCountAfter = fileRepository.count();

		assertEquals(storageCountBefore, storageCountAfter);
		assertEquals(fileCountBefore, fileCountAfter);
	}

	@Test
	void testTransactionWithInvalidUser() {
		long storageCountBefore = storageRepository.count();
		long fileCountBefore = fileRepository.count();

		assertThrows(RuntimeException.class, () -> {
			transactionService.createStorageWithInvalidUser(9999L, "bad.jpg");
		});

		long storageCountAfter = storageRepository.count();
		long fileCountAfter = fileRepository.count();

		assertEquals(storageCountBefore, storageCountAfter);
		assertEquals(fileCountBefore, fileCountAfter);
	}
}
