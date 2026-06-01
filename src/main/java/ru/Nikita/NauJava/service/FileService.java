package ru.Nikita.NauJava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.Nikita.NauJava.dto.FileUploadResult;
import ru.Nikita.NauJava.entity.FileEntity;
import ru.Nikita.NauJava.entity.LinkEntity;
import ru.Nikita.NauJava.entity.StorageEntity;
import ru.Nikita.NauJava.entity.StorageQuotaEntity;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.FileRepository;
import ru.Nikita.NauJava.repository.LinkRepository;
import ru.Nikita.NauJava.repository.QuotaRepository;
import ru.Nikita.NauJava.repository.StorageEntityRepository;
import ru.Nikita.NauJava.storage.StorageProvider;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для управления файлами пользователей.
 *
 * <p>Содержит бизнес-логику загрузки, скачивания, удаления файлов
 * с учётом квот хранилища. Транзакционность обеспечивается на уровне сервиса.</p>
 */
@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final FileRepository fileRepository;
    private final StorageEntityRepository storageRepository;
    private final QuotaRepository quotaRepository;
    private final StorageProvider storageProvider;
    private final LinkRepository linkRepository;

    @Autowired
    public FileService(FileRepository fileRepository,
                       StorageEntityRepository storageRepository,
                       QuotaRepository quotaRepository,
                       StorageProvider storageProvider,
                       LinkRepository linkRepository) {
        this.fileRepository = fileRepository;
        this.storageRepository = storageRepository;
        this.quotaRepository = quotaRepository;
        this.storageProvider = storageProvider;
        this.linkRepository = linkRepository;
    }

    /**
     * Возвращает список файлов пользователя.
     *
     * @param user пользователь
     * @return список файлов
     */
    @Transactional(readOnly = true)
    public List<FileEntity> getUserFiles(UserEntity user) {
        List<StorageEntity> storages = storageRepository.findByUser(user);
        return storages.stream()
                .flatMap(s -> fileRepository.findByStorage(s).stream())
                .toList();
    }

    /**
     * Загружает файл в хранилище пользователя и создаёт публичную ссылку на него.
     *
     * @param user     пользователь
     * @param file     multipart-файл
     * @param fileName желаемое имя файла
     * @return результат загрузки (сохранённый файл и токен ссылки)
     * @throws IllegalStateException если превышена квота
     */
    @Transactional
    public FileUploadResult uploadFile(UserEntity user, MultipartFile file, String fileName) {
        long fileSize = file.getSize();

        StorageQuotaEntity quota = quotaRepository.findByUser(user);
        if (quota == null) {
            quota = createDefaultQuota(user);
        }

        if (!quota.hasAvailableSpace(fileSize)) {
            throw new IllegalStateException("Превышена квота хранилища. Доступно: "
                    + (quota.getMaxBytes() - quota.getUsedBytes()) + " байт");
        }

        StorageEntity storage = getOrCreateUserStorage(user);

        String relativePath = user.getId() + "/" + UUID.randomUUID() + "_" + fileName;

        try (InputStream is = file.getInputStream()) {
            storageProvider.store(relativePath, is, fileSize);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка чтения файла", e);
        }

        FileEntity entity = new FileEntity();
        entity.setStorage(storage);
        entity.setFileName(fileName);
        entity.setFileSize(fileSize);
        entity.setMimeType(file.getContentType());
        entity.setPath(relativePath);
        entity.setUploadedAt(LocalDateTime.now());

        FileEntity saved = fileRepository.save(entity);

        LinkEntity link = new LinkEntity();
        link.setFile(saved);
        link.setCreatedBy(user);
        link.setToken(UUID.randomUUID().toString());
        linkRepository.save(link);

        quota.addUsedBytes(fileSize);
        quotaRepository.save(quota);

        logger.info("Пользователь {} загрузил файл '{}' ({} байт) и создал ссылку {}",
                user.getEmail(), fileName, fileSize, link.getToken());
        return new FileUploadResult(saved, link.getToken());
    }

    /**
     * Возвращает файл как {@link Resource} для скачивания.
     *
     * @param fileId идентификатор файла
     * @return ресурс файла
     */
    @Transactional(readOnly = true)
    public Resource downloadFile(Long fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Файл не найден: " + fileId));
        return storageProvider.loadAsResource(file.getPath());
    }

    /**
     * Возвращает сущность файла по идентификатору.
     *
     * @param fileId идентификатор
     * @return Optional с сущностью
     */
    @Transactional(readOnly = true)
    public Optional<FileEntity> findById(Long fileId) {
        return fileRepository.findById(fileId);
    }

    /**
     * Удаляет файл пользователя и освобождает квоту.
     *
     * @param user   пользователь-владелец
     * @param fileId идентификатор файла
     */
    @Transactional
    public void deleteFile(UserEntity currentUser, Long fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Файл не найден: " + fileId));

        UserEntity owner = file.getStorage().getUser();
        if (!owner.getId().equals(currentUser.getId()) && !"ADMIN".equals(currentUser.getRole())) {
            throw new SecurityException("Нет прав на удаление файла");
        }

        storageProvider.delete(file.getPath());
        fileRepository.delete(file);

        StorageQuotaEntity quota = quotaRepository.findByUser(owner);
        if (quota != null) {
            quota.removeUsedBytes(file.getFileSize());
            quotaRepository.save(quota);
        }

        logger.info("Пользователь {} удалил файл '{}' (владелец: {})",
                currentUser.getEmail(), file.getFileName(), owner.getEmail());
    }

    private StorageEntity getOrCreateUserStorage(UserEntity user) {
        List<StorageEntity> storages = storageRepository.findByUser(user);
        if (!storages.isEmpty()) {
            return storages.get(0);
        }
        StorageEntity storage = new StorageEntity();
        storage.setUser(user);
        storage.setMimeType("application/octet-stream");
        storage.setUploadedAt(LocalDateTime.now());
        return storageRepository.save(storage);
    }

    private StorageQuotaEntity createDefaultQuota(UserEntity user) {
        StorageQuotaEntity quota = new StorageQuotaEntity();
        quota.setUser(user);
        quota.setMaxBytes(1073741824L);
        quota.setUsedBytes(0L);
        return quotaRepository.save(quota);
    }
}
