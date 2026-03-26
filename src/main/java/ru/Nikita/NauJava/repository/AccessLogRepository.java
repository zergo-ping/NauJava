package ru.Nikita.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.Nikita.NauJava.entity.FileAccessLogEntity;
import ru.Nikita.NauJava.entity.LinkEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Репозиторий для работы с логами доступа к файлам
 */
public interface AccessLogRepository extends CrudRepository<FileAccessLogEntity, Long> {

    /**
     * Поиск всех логов доступа по ссылке
     * 
     * @param link ссылка, к которой был осуществлён доступ
     * @return список логов доступа для этой ссылки
     */
    List<FileAccessLogEntity> findByLink(LinkEntity link);


    List<FileAccessLogEntity> findByAccessedByIpAndAccessedAtBetween(String ip,
                                                           LocalDateTime start,
                                                           LocalDateTime end);

    @Query("SELECT a FROM FileAccessLogEntity a WHERE a.link.storage.user.id = :userId")
    List<FileAccessLogEntity> findLogsByUserId(Long userId);

}
