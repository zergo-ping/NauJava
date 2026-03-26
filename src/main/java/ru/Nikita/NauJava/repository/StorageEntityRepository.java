package ru.Nikita.NauJava.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import ru.Nikita.NauJava.entity.StorageEntity;
import ru.Nikita.NauJava.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с хранилищами.
 */
public interface StorageEntityRepository extends CrudRepository<StorageEntity, Long> {

    /**
     * Поиск всех хранилищ конкретного пользователя
     * 
     * @param user пользователь
     * @return список хранилищ пользователя
     */
    List<StorageEntity> findByUser(UserEntity user);

    List<StorageEntity> findByUserAndUploadedAtBetween(UserEntity user,
                                                 LocalDateTime startDate,
                                                 LocalDateTime endDate);

    /**
     * Поиск хранилищ по MIME-типу
     * 
     * @param mimeType MIME-тип содержимого
     * @return список хранилищ с указанным MIME-типом
     */
    List<StorageEntity> findByMimeTypeContaining(String mimeType);

    /**
     * Поиск хранилищ по электронной почте пользователя
     * 
     * @param email электронная почта пользователя
     * @return список хранилищ пользователя с указанной почтой
     */
    @Query("SELECT s FROM StorageEntity s WHERE s.user.email = :email")
    List<StorageEntity> findStoragesByUserEmail(String email);

}