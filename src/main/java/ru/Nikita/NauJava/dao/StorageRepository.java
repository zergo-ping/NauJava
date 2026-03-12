package ru.Nikita.NauJava.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Nikita.NauJava.model.Storage;

import java.util.List;
import java.util.Objects;

@Component
public class StorageRepository implements CrudRepository<Storage, Long>{

    private final List<Storage> storageContainer;

    @Autowired
    public StorageRepository(List<Storage> storageContainer) {
        this.storageContainer = storageContainer;
    }

    @Override
    public void create(Storage entity)
    {
        if(entity != null)
        {
            storageContainer.add(entity);
        }
    }

    @Override
    public Storage read(Long aLong) {
        for (Storage s : storageContainer)
        {
         if (Objects.equals(s.getId(), aLong))
         {
             return s;
         }
        }
        return null;
    }

    @Override
    public void update(Storage entity) {

        Long entityId = entity.getId();

        String entityName = entity.getNameOfStorage();

        List<String> entityListOfFiles = entity.getListOfFiles();

        for (Storage storage : storageContainer)
        {
            if(Objects.equals(storage.getId(),entity.getId()))
            {
                if(entityName != null)
                {
                    storage.setNameOfStorage(entityName);
                }

                if(!entityListOfFiles.isEmpty())
                {
                    storage.setListOfFiles(entityListOfFiles);
                }
            }
        }


    }

    @Override
    public void delete(Long aLong) {
        for (Storage s : storageContainer)
        {
            if (Objects.equals(s.getId(), aLong))
            {
                storageContainer.remove(aLong);
            }
        }


    }
}
