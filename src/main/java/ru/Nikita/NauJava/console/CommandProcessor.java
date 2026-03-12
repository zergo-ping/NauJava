package ru.Nikita.NauJava.console;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.Nikita.NauJava.model.Storage;
import ru.Nikita.NauJava.service.StorageServiceImpl;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class CommandProcessor
{
    private final StorageServiceImpl storageService;

    @Autowired
    public CommandProcessor( StorageServiceImpl storageService)
    {
        this.storageService = storageService;

    }

    public void processCommand(String input)
    {
        String[] cmd = input.split(" ");
        switch (cmd[0])
        {
            case "create" ->
            {
                storageService.createStorage(Long.valueOf(cmd[1]), cmd[2],
                        Arrays.stream(cmd[3].split(","))
                                .map(String::trim)
                                .filter(word -> !word.isEmpty())
                                .collect(Collectors.toList()));
                System.out.println("Хранилище добавлено");
            }

            case "read" ->
            {
                Storage storage = storageService.findById(Long.valueOf(cmd[1]));

                System.out.println(storage.getNameOfStorage() + storage.getListOfFiles().toString());
            }
            case "updateName" ->
            {
                storageService.updateNameOfStorage(Long.valueOf(cmd[1]),String.valueOf(cmd[2]));
                System.out.println("Хранилище удалено");

            }

            case "updateList" ->
            {
                storageService.updateListOfFiles(Long.valueOf(cmd[1]),
                        Arrays.stream(cmd[2].split(","))
                            .map(String::trim)
                            .filter(word -> !word.isEmpty())
                            .collect(Collectors.toList()));
                System.out.println("Хранилище обновлено");

            }

            case "delete" ->
            {
                storageService.deleteById(Long.valueOf(cmd[1]));
                System.out.println("Хранилище удалено");
            }

            default -> System.out.println("Введена неизвестная команда...");
        }
    }
}