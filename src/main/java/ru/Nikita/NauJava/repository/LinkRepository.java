package ru.Nikita.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import ru.Nikita.NauJava.entity.LinkEntity;
import ru.Nikita.NauJava.entity.StorageEntity;
import ru.Nikita.NauJava.entity.UserEntity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkRepository extends CrudRepository<LinkEntity, Long> {

    Optional<LinkEntity> findByToken(String token);

    List<LinkEntity> findByStorage(StorageEntity storage);

    List<LinkEntity> findByCreatedBy(UserEntity user);

    List<LinkEntity> findByCreatedAtBetweenAndMaxDownloadsGreaterThanEqual(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer minDownloads);

    @Query("SELECT l FROM LinkEntity l WHERE l.storage.user.id = :userId")
    List<LinkEntity> findAllLinksByUserId(Long userId);
}
