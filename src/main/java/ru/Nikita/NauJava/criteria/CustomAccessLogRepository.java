package ru.Nikita.NauJava.criteria;

import ru.Nikita.NauJava.entity.FileAccessLogEntity;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Интерфейс для кастомных запросов к логам доступа с использованием Criteria API.
 * 
 * Предоставляет методы для сложных поисков логов доступа с использованием Criteria Query.
 */
public interface CustomAccessLogRepository {

    /**
     * Поиск логов доступа по пользователю в указанном диапазоне дат.
     * 
     * Использует Criteria API для создания динамического запроса с гибкой фильтрацией по датам.
     * Начальная и конечная даты являются опциональными.
     * 
     * @param userId идентификатор пользователя
     * @param startDate начальная дата (может быть null)
     * @param endDate конечная дата (может быть null)
     * @return список логов доступа, отсортированные по дате в убывающем порядке
     */
    List<FileAccessLogEntity> findLogsByUserAndDateRange(Long userId,
                                                         LocalDateTime startDate,
                                                         LocalDateTime endDate);

    /**
     * Поиск логов доступа по IP-адресу с минимальным количеством обращений.
     * 
     * Использует Criteria API для фильтрации логов по IP-адресу.
     * 
     * @param ip IP-адрес для фильтрации
     * @param minCount минимальное количество обращений (в данной реализации не используется)
     * @return список логов доступа с указанного IP-адреса
     */
    List<FileAccessLogEntity> findLogsByIpWithMinCount(String ip, int minCount);
}