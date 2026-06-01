package ru.Nikita.NauJava.dto;

import ru.Nikita.NauJava.entity.FileEntity;

/**
 * Результат загрузки файла, содержащий сохранённый файл и токен публичной ссылки.
 */
public record FileUploadResult(FileEntity file, String linkToken) {
}
