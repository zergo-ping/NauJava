package ru.Nikita.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.Nikita.NauJava.criteria.CustomAccessLogRepository;
import ru.Nikita.NauJava.criteria.CustomStorageRepository;
import ru.Nikita.NauJava.entity.FileAccessLogEntity;
import ru.Nikita.NauJava.entity.StorageEntity;

import java.util.List;

/**
 * REST контроллер для работы с пользовательским хранилищем и логами доступа.
 * Предоставляет эндпоинты для поиска хранилищ и логов доступа по различным критериям.
 */
@RestController
@RequestMapping("/custom")
public class CustomController {

    private final CustomStorageRepository customStorageRepository;

    private final CustomAccessLogRepository customAccessLogRepository;


    @Autowired
    public CustomController(CustomStorageRepository customStorageRepository,CustomAccessLogRepository customAccessLogRepository)
    {
        this.customAccessLogRepository = customAccessLogRepository;
        this.customStorageRepository = customStorageRepository;

    }


    @GetMapping("/storage/findStorages")
    public List<StorageEntity> findStorages(@RequestParam Long userId, @RequestParam String mimeType)
    {
        return customStorageRepository.findStoragesByUserAndMimeType(userId,mimeType);
    }


    @GetMapping("/accessLog/findLogsByIpWithMinCount")
    public List<FileAccessLogEntity> findLogsByIpWithMinCount(@RequestParam String ip, @RequestParam int minCount)
    {
        return customAccessLogRepository.findLogsByIpWithMinCount(ip,minCount);
    }



}
