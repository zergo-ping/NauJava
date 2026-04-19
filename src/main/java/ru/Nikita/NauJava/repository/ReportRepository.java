package ru.Nikita.NauJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.Nikita.NauJava.entity.ReportEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
}
