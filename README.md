# Sharing

Файлообменник с публичными ссылками, квотами хранилища и админ-панелью.

## Быстрый старт (Docker)
Заполните свои данные в .env_example, создайте файл .env и перенесите данные из файла .env_example в .env

```bash
docker compose up --build
```

Приложение будет доступно по адресу: http://localhost:8080

## Сервисы

| Сервис   | Адрес                     | Описание                  |
|----------|---------------------------|---------------------------|
| App      | http://localhost:8080     | Spring Boot приложение    |
| PostgreSQL | localhost:5432          | База данных               |
| MinIO    | http://localhost:9000     | S3-совместимое хранилище  |
| MinIO Console | http://localhost:9001 | Web-интерфейс MinIO       |

## Учётные записи по умолчанию

| Роль  | Email             | Пароль |
|-------|-------------------|--------|
| Admin | admin@example.com | admin  |
| User  | user@example.com  | user   |

## Локальный запуск (без Docker)

Требования: Java 21, Maven, PostgreSQL, MinIO

```bash
mvn spring-boot:run
```

Настройки подключения в `src/main/resources/application.properties`.

## Схема БД

https://dbdiagram.io/d/Sharing-69bbe28e78c6c4bc7a212f6d
