package ru.Nikita.NauJava.criteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.Nikita.NauJava.entity.StorageEntity;

import java.util.List;

/**
 * Реализация интерфейса CustomStorageRepository с использованием Criteria API
 * 
 * Предоставляет кастомные методы поиска хранилищ через динамическое построение запросов.
 */
@Repository
public class CustomStorageRepositoryImpl implements CustomStorageRepository {

    /**
     * Entity Manager для работы с JPA и Criteria API.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Поиск хранилищ пользователя по MIME-типу с использованием Criteria API
     * 
     * @param userId идентификатор пользователя
     * @param mimeType MIME-тип для фильтрации
     * @return список хранилищ, отсортированные по дате загрузки в убывающем порядке
     */
    @Override
    public List<StorageEntity> findStoragesByUserAndMimeType(Long userId, String mimeType) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<StorageEntity> query = cb.createQuery(StorageEntity.class);
        Root<StorageEntity> storage = query.from(StorageEntity.class);
        Join<Object, Object> userJoin = storage.join("user");


        Predicate userCondition = cb.equal(userJoin.get("id"), userId);
        Predicate mimeCondition = cb.like(storage.get("mimeType"), "%" + mimeType + "%");


        query.where(cb.and(userCondition, mimeCondition));
        query.orderBy(cb.desc(storage.get("uploadedAt")));

        return entityManager.createQuery(query).getResultList();
    }

    /**
     * Поиск хранилищ с минимальным количеством файлов с использованием Criteria API
     * 
     * @param minFilesCount минимальное количество файлов
     * @return список хранилищ, отсортированные по количеству файлов в убывающем порядке
     */
    @Override
    public List<StorageEntity> findStoragesWithMinFiles(int minFilesCount) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<StorageEntity> query = cb.createQuery(StorageEntity.class);
        Root<StorageEntity> storage = query.from(StorageEntity.class);


        Join<Object, Object> filesJoin = storage.join("files", JoinType.LEFT);
        query.groupBy(storage.get("id"));
        query.having(cb.ge(cb.count(filesJoin), minFilesCount));
        query.orderBy(cb.desc(cb.count(filesJoin)));

        return entityManager.createQuery(query).getResultList();
    }
}