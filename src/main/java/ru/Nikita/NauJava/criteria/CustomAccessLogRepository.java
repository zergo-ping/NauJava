package ru.Nikita.NauJava.criteria;

import ru.Nikita.NauJava.entity.FileAccessLogEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomAccessLogRepository {

    List<FileAccessLogEntity> findLogsByUserAndDateRange(Long userId,
                                                         LocalDateTime startDate,
                                                         LocalDateTime endDate);

    // Поиск логов по IP и мин. кол-ву
    List<FileAccessLogEntity> findLogsByIpWithMinCount(String ip, int minCount);
}