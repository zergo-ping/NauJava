package ru.Nikita.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.Nikita.NauJava.entity.StorageQuotaEntity;
import ru.Nikita.NauJava.entity.UserEntity;

import java.util.Optional;

public interface QuotaRepository extends CrudRepository<StorageQuotaEntity, Long> {

    StorageQuotaEntity findByUser(UserEntity user);

    StorageQuotaEntity findByUserId(Long userId);


}