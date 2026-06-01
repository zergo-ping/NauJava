package ru.Nikita.NauJava.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Nikita.NauJava.entity.FileEntity;
import ru.Nikita.NauJava.entity.LinkEntity;
import ru.Nikita.NauJava.entity.UserEntity;
import ru.Nikita.NauJava.repository.FileRepository;
import ru.Nikita.NauJava.repository.LinkRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для управления ссылками совместного доступа.
 *
 * <p>Предоставляет бизнес-логику получения, удаления и проверки ссылок.</p>
 */
@Service
public class LinkService {

    private static final Logger logger = LoggerFactory.getLogger(LinkService.class);

    private final LinkRepository linkRepository;
    private final FileRepository fileRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository, FileRepository fileRepository) {
        this.linkRepository = linkRepository;
        this.fileRepository = fileRepository;
    }

    /**
     * Возвращает список ссылок, созданных пользователем.
     *
     * @param user пользователь
     * @return список ссылок
     */
    @Transactional(readOnly = true)
    public List<LinkEntity> getUserLinks(UserEntity user) {
        return linkRepository.findByCreatedBy(user);
    }

    /**
     * Ищет ссылку по токену.
     *
     * @param token уникальный токен
     * @return Optional с ссылкой
     */
    @Transactional(readOnly = true)
    public Optional<LinkEntity> findByToken(String token) {
        return linkRepository.findByToken(token);
    }

    /**
     * Удаляет ссылку. Пользователь может удалять только свои ссылки,
     * администратор — любые.
     *
     * @param user   текущий пользователь
     * @param linkId идентификатор ссылки
     */
    @Transactional
    public void deleteLink(UserEntity user, Long linkId) {
        LinkEntity link = linkRepository.findById(linkId)
                .orElseThrow(() -> new IllegalArgumentException("Ссылка не найдена: " + linkId));

        boolean isOwner = link.getCreatedBy().getId().equals(user.getId());
        boolean isAdmin = "ADMIN".equals(user.getRole());

        if (!isOwner && !isAdmin) {
            throw new SecurityException("Нет прав на удаление ссылки");
        }

        linkRepository.delete(link);
        logger.info("Пользователь {} удалил ссылку {}", user.getEmail(), linkId);
    }

    /**
     * Создаёт новую ссылку на существующий файл.
     *
     * @param user   текущий пользователь
     * @param fileId идентификатор файла
     * @return созданная ссылка
     */
    @Transactional
    public LinkEntity createLinkForFile(UserEntity user, Long fileId) {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("Файл не найден: " + fileId));

        boolean isOwner = file.getStorage().getUser().getId().equals(user.getId());
        boolean isAdmin = "ADMIN".equals(user.getRole());

        if (!isOwner && !isAdmin) {
            throw new SecurityException("Нет прав на создание ссылки для этого файла");
        }

        LinkEntity link = new LinkEntity();
        link.setFile(file);
        link.setCreatedBy(user);
        link.setToken(UUID.randomUUID().toString());
        linkRepository.save(link);

        logger.info("Пользователь {} создал ссылку {} для файла {}",
                user.getEmail(), link.getToken(), fileId);
        return link;
    }

    /**
     * Увеличивает счётчик скачиваний ссылки.
     *
     * @param link сущность ссылки
     */
    @Transactional
    public void incrementDownloads(LinkEntity link) {
        link.setCurrentDownloads(link.getCurrentDownloads() + 1);
        linkRepository.save(link);
    }

    /**
     * Возвращает файл, связанный со ссылкой.
     *
     * @param link ссылка
     * @return Optional с файлом
     */
    @Transactional(readOnly = true)
    public Optional<FileEntity> getLinkedFile(LinkEntity link) {
        return Optional.ofNullable(link.getFile());
    }
}
