package ru.Nikita.NauJava.criteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.Nikita.NauJava.entity.FileAccessLogEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Реализация интерфейса CustomAccessLogRepository с использованием Criteria API
 */
@Repository
public class CustomAccessLogRepositoryImpl implements CustomAccessLogRepository {

    /**
     * Entity Manager для работы с JPA и Criteria API
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Поиск логов доступа по пользователю и диапазону дат с использованием Criteria API
     * 
     * @param userId идентификатор пользователя
     * @param startDate начальная дата (может быть null)
     * @param endDate конечная дата (может быть null)
     * @return список логов доступа, отсортированные по дате в убывающем порядке
     */
    @Override
    public List<FileAccessLogEntity> findLogsByUserAndDateRange(Long userId,
                                                                LocalDateTime startDate,
                                                                LocalDateTime endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileAccessLogEntity> query = cb.createQuery(FileAccessLogEntity.class);
        Root<FileAccessLogEntity> log = query.from(FileAccessLogEntity.class);

        Join<Object, Object> linkJoin = log.join("link");
        Join<Object, Object> storageJoin = linkJoin.join("storage");
        Join<Object, Object> userJoin = storageJoin.join("user");


        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(userJoin.get("id"), userId));

        if (startDate != null && endDate != null) {
            predicates.add(cb.between(log.get("accessedAt"), startDate, endDate));
        } else if (startDate != null) {
            predicates.add(cb.greaterThanOrEqualTo(log.get("accessedAt"), startDate));
        } else if (endDate != null) {
            predicates.add(cb.lessThanOrEqualTo(log.get("accessedAt"), endDate));
        }

        query.where(predicates.toArray(new Predicate[0]));

        query.orderBy(cb.desc(log.get("accessedAt")));

        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Поиск логов доступа по IP-адресу с использованием Criteria API.
     * 
     * @param ip IP-адрес для фильтрации
     * @param minCount параметр не используется в текущей реализации
     * @return список логов доступа с указанного IP-адреса
     */
    @Override
    public List<FileAccessLogEntity> findLogsByIpWithMinCount(String ip, int minCount) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<FileAccessLogEntity> query = cb.createQuery(FileAccessLogEntity.class);
        Root<FileAccessLogEntity> log = query.from(FileAccessLogEntity.class);


        Predicate ipCondition = cb.equal(log.get("accessedByIp"), ip);


        query.groupBy(log.get("id"));
        query.where(ipCondition);
        query.orderBy(cb.desc(log.get("accessedAt")));

        return entityManager.createQuery(query).getResultList();
    }
}
