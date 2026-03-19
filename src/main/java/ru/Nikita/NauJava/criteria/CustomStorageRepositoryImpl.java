package ru.Nikita.NauJava.criteria;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import ru.Nikita.NauJava.entity.StorageEntity;

import java.util.List;

@Repository
public class CustomStorageRepositoryImpl implements CustomStorageRepository {

    @PersistenceContext
    private EntityManager entityManager;

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