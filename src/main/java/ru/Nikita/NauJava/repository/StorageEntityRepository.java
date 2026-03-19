package ru.Nikita.NauJava.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import ru.Nikita.NauJava.entity.StorageEntity;
import ru.Nikita.NauJava.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface StorageEntityRepository extends CrudRepository<StorageEntity, Long> {

    List<StorageEntity> findByUser(UserEntity user);

    List<StorageEntity> findByUserAndUploadedAtBetween(UserEntity user,
                                                 LocalDateTime startDate,
                                                 LocalDateTime endDate);

    List<StorageEntity> findByMimeTypeContaining(String mimeType);

    @Query("SELECT s FROM StorageEntity s WHERE s.user.email = :email")
    List<StorageEntity> findStoragesByUserEmail(String email);

}