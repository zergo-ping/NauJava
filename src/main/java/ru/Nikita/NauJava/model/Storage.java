package ru.Nikita.NauJava.model;

import java.util.ArrayList;
import java.util.List;

public class Storage {
    private Long id;
    private String nameOfStorage;

    private List<String> listOfFiles = new ArrayList<>();
    public void setId(Long id)
    {
        this.id = id;
    }

    public void setNameOfStorage(String nameOfStorage)
    {
        this.nameOfStorage = nameOfStorage;
    }

    public void addToListOfFiles(String file)
    {
        this.listOfFiles.add(file);
    }

    public Long getId()
    {
        return id;
    }

    public String getNameOfStorage()
    {
        return nameOfStorage;
    }

    public List<String> getListOfFiles()
    {
        return listOfFiles;
    }

    public void setListOfFiles(List<String> list)
    {
        listOfFiles.clear();
        listOfFiles.addAll(list);
    }
}
