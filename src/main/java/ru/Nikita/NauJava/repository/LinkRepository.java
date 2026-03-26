package ru.Nikita.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import ru.Nikita.NauJava.entity.LinkEntity;
import ru.Nikita.NauJava.entity.StorageEntity;
import ru.Nikita.NauJava.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы со ссылками для совместного доступа
 * 
 * Предоставляет методы для поиска и управления ссылками,
 * используемыми для совместного доступа к файлам.
 */
public interface LinkRepository extends CrudRepository<LinkEntity, Long> {

    /**
     * Поиск ссылки по уникальному токену
     * 
     * @param token уникальный токен ссылки
     * @return Optional с ссылкой, если найдена
     */
    Optional<LinkEntity> findByToken(String token);

    /**
     * Поиск всех ссылок для конкретного хранилища
     * 
     * @param storage хранилище
     * @return список ссылок для хранилища
     */
    List<LinkEntity> findByStorage(StorageEntity storage);

    /**
     * Поиск всех ссылок, созданных конкретным пользователем
     * 
     * @param user пользователь
     * @return список ссылок, созданных пользователем
     */
    List<LinkEntity> findByCreatedBy(UserEntity user);

    /**
     * Поиск ссылок в указанном диапазоне дат с минимальным количеством загруженных скачиваний
     * 
     * @param startDate начальная дата
     * @param endDate конечная дата
     * @param minDownloads минимальное количество скачиваний
     * @return список ссылок, соответствующих критериям
     */
    List<LinkEntity> findByCreatedAtBetweenAndMaxDownloadsGreaterThanEqual(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer minDownloads);

    /**
     * Поиск всех ссылок, созданных для хранилищ конкретного пользователя
     * 
     * @param userId идентификатор пользователя
     * @return список всех ссылок пользователя
     */
    @Query("SELECT l FROM LinkEntity l WHERE l.storage.user.id = :userId")
    List<LinkEntity> findAllLinksByUserId(Long userId);
}
