package ru.Nikita.NauJava.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import ru.Nikita.NauJava.Config;
import ru.Nikita.NauJava.dao.StorageRepository;
import ru.Nikita.NauJava.model.Storage;

import java.util.List;

@Service
public class StorageServiceImpl implements StorageService{

    private final StorageRepository storageRepository;

    private final Config appConfig;

    @Autowired
    public StorageServiceImpl(StorageRepository storageRepository, Config appConfig) {
        this.storageRepository = storageRepository;
        this.appConfig = appConfig;
    }

    @Override
    public void createStorage(Long id, String name, List<String> list) {
        Storage newStorage = new Storage();
        newStorage.setId(id);
        newStorage.setNameOfStorage(name);
        newStorage.setListOfFiles(list);
        storageRepository.create(newStorage);
    }

    @Override
    public Storage findById(Long id) {
        return storageRepository.read(id);
    }

    @Override
    public void deleteById(Long id) {
        storageRepository.delete(id);

    }

    @Override
    public void updateNameOfStorage(Long id, String newName) {
        Storage newStorage = new Storage();
        newStorage.setId(id);
        newStorage.setNameOfStorage(newName);
        storageRepository.update(newStorage);

    }

    @Override
    public void updateListOfFiles(Long id, List<String> newList) {
        Storage newStorage = new Storage();
        newStorage.setId(id);
        newStorage.setListOfFiles(newList);
        storageRepository.update(newStorage);

    }

    @PostConstruct
    public void init() {
        System.out.println("\n=== ИНФОРМАЦИЯ О ПРИЛОЖЕНИИ ===");
        System.out.println("Имя приложения: " + appConfig.getAppName());
        System.out.println("Версия: " + appConfig.getAppVersion());
        System.out.println("===================================\n");


    }


}
