# Сервис SSO.

## Структура модуля

1. [`client`](client/README.md) - frontend приложение.
2. `database` - скрипты раската схемы БД и основных данных для запуска и работы j-sso.
3. `src` - backend приложение.

## Сборка

В качестве системы автосборки используется Maven. Соответственно, для запуска сборки модуля можно использовать следующую
команду.

```shell
mvn clean install -DskipTests -P dev,client-build-and-copy
```

Существует несколько profile сборки:

- `client-build-and-copy` - при сборке модуля также собирает и frontend приложение. После чего копирует собранные файлы
  в ресурсы приложения, а именно в `/target/classes/templates` и в `/target/classes/static`.
- `client-only-copy` - при сборке пытается только скопировать сборку frontend приложения в ресурсы.
- `dev` - по умолчанию включен. Сборка производиться для среды разработки. Для сборки frontend приложения применяется
  команда `npm run build-dev-java`
- `prod` - Сборка производиться для продуктивной среды. Для сборки frontend приложения применяется
  команда `npm run build-prod`
- `test` - Сборка производиться для среды тестирования. Для сборки frontend приложения применяется
  команда `npm run build-test`

За более подробной информацией о работе автосборки обращайтесь
к [данной статье (Раздел 3.3)](https://habr.com/ru/articles/748584/).

## Раскат БД

Для запуска БД в среде разработки используйте `docker-compose.yml`. В нём определён сервис `j-postgres`. В проекте
добавлена готовая конфигурация запуска для IntelliJ IDEA. См.
в `[run_database.xml](../.idea/runConfigurations/run_database.xml)`.

Раскат схемы БД выполняется строго из модуля `j-sso` следующей командой:

```shell
mvn liquibase:update -Dliquibase.searchPath=./
```

Информацию о настройке liquibase читайте в [этой статье (Раздел 2.2)](https://habr.com/ru/articles/746698/).

## Запуск приложения в среде разработки

Для запуска приложения j-sso достаточно собрать и запустить его как обычное приложение. Также в проект добавлена готовая
конфигурация запуска для IntelliJ IDEA [SSOApplication.xml](../.idea/runConfigurations/SSOApplication.xml).

Обратите внимание, что для корректной работы приложения необходимо указать Environment variables при запуске приложения.
Все Environment variables описаны в разделе ниже.

Порт приложения по умолчанию настроен на значение 7777. Поменять его можно
в [application.yml](src/main/resources/application.yml). Если вы поменяли порт, то не забудьте актуализировать значения
в файле [application-security.yml](src/main/resources/application-security.yml). Также не забудьте актуализировать
значение в frontend приложении [.env.development](client/.env.development).

## Environment variables для запуска приложения

| Наименование              | Значение по умолчанию | Описание                                  |
|---------------------------|-----------------------|-------------------------------------------|
| JSSO_GITHUB_CLIENT_ID     |                       | Параметр client_id для сервиса Github     |
| JSSO_GITHUB_CLIENT_SECRET |                       | Параметр client_secret для сервиса Github |
| JSSO_GOOGLE_CLIENT_ID     |                       | Параметр client_id для сервиса Google     |
| JSSO_GOOGLE_CLIENT_SECRET |                       | Параметр client_secret для сервиса Google |
| JSSO_YANDEX_CLIENT_ID     |                       | Параметр client_id для сервиса Yandex     |
| JSSO_YANDEX_CLIENT_SECRET |                       | Параметр client_secret для сервиса Yandex |
| JSSO_SMTP_EMAIL           |                       | Email адрес служебного почтового сервиса  |
| JSSO_SMTP_EMAIL_PASSWORD  |                       | Пароль для служебного почтового ящика     |
| JSSO_REDIS_DB             | 0                     | Номер БД Redis                            |
| JSSO_REDIS_HOST           | localhost             | Хост подключения к Redis                  |
| JSSO_REDIS_PORT           | 6379                  | Порт подключения к Redis                  |
| JSSO_REDIS_PASSWORD       | qwerty12345678        | Пароль подключения к Redis                |
| JSSO_PG_HOST              | localhost             | Хост подключения к Postgresql             |
| JSSO_PG_PORT              | 5435                  | Порт подключения к Postgresql             |
| JSSO_PG_DB                | j-sso                 | Имя подключения к Postgresql              |
| JSSO_PG_USER              | user                  | Имя пользователя подключения к Postgresql |
| JSSO_PG_PASSWORD          | user                  | Пароль подключения к Postgresql           |
| JSSO_ANSI_ENABLED         | ALWAYS                | Включение подсветки логов                 |
| JSSO_PATH_STORAGE         | ~/j-sso-file-store    | Путь до файлового хранилища               |