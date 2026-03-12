package ru.Nikita.NauJava.service;

import ru.Nikita.NauJava.model.Storage;

import java.util.List;

public interface StorageService {

    void createStorage(Long id, String name, List<String> list);

    Storage findById(Long id);

    void deleteById(Long id);

    void updateNameOfStorage(Long id, String newLogin);

    void updateListOfFiles(Long id, List<String> newList);
}
