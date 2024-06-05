# j-service. Тестовый OAuth2 Resource Server.

Данное приложение предназначено для демонстрации взаимодействия OAuth2 Resource Server, настроенного с
использованием `spring-boot-starter-oauth2-resource-server`, с сервером авторизации `j-sso`.

За более детальной информацией обращайтесь к [этой статье (Раздел 2.1)](https://habr.com/ru/articles/746698/).

## Сборка и запуск

j-service представляет собой самое простейшее Spring Boot приложение.

Сборка производиться следующей командой

```shell
mvn clean install -DskipTests
```

Для запуска можете использовать собранный jar архив или использовать подготовленную конфигурацию запуска для IntelliJ
IDEA [ResourceApplication.xml](../.idea/runConfigurations/ResourceApplication.xml).