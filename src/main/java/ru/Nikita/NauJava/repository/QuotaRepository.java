package ru.Nikita.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import ru.Nikita.NauJava.entity.StorageQuotaEntity;
import ru.Nikita.NauJava.entity.UserEntity;

import java.util.Optional;

/**
 * Репозиторий для работы с квотами дискового пространства.
 */
public interface QuotaRepository extends CrudRepository<StorageQuotaEntity, Long> {

    /**
     * Поиск квоты по пользователю
     * 
     * @param user пользователь
     * @return квота пользователя
     */
    StorageQuotaEntity findByUser(UserEntity user);

    /**
     * Поиск квоты по идентификатору пользователя
     * 
     * @param userId идентификатор пользователя
     * @return квота пользователя
     */
    StorageQuotaEntity findByUserId(Long userId);


}