# Frontend приложения j-sso.

Структура приложения рассматривалась в [этой статье (Раздел 4.1)](https://habr.com/ru/articles/784552/).

URLs:

- http://localhost:8181/client/home - домашняя страница
- http://localhost:8181/client/auth/login - страница входа

## Сборка frontend приложения вручную

Для сборки frontend приложения вручную вам потребуется Node 16 и npm 8. Перейдите в директорию `client` и выполните
следующие команды.

```shell
npm install
npm run build
```

Также для сборки под разные среды можно использовать следующие команды:

- `build-prod` - сборка под продуктивную среду. Использует файл параметров [.env.production](.env.production).
- `build-test` - сборка под тестовую среду. Использует файл параметров [.env.testing](.env.testing).
- `build-dev-java` - сборка под среду разработки для работы в составе приложения `j-sso`. Использует файл
  параметров [.env.dev-java](.env.dev-java).
- `build` - сборка под среду разработки для запуска через devServer. Использует файл
  параметров [.env.development](.env.development).

## Env параметры

| Параметр                    | Описание                                                                                                                                                 |
|-----------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------|
| VUE_APP_NODE_ENV            | Имя среды                                                                                                                                                |
| VUE_APP_SERVER              | local или remote. remote означает использование параметра VUE_APP_HOST для создания запросов к backend. local - берёт значение из `window.location.host` |
| VUE_APP_PROTOCOL            | http или https. Протокол взаимодействия с backend.                                                                                                       |
| VUE_APP_HOST                | Хост/порт для взаимодействия с backend.                                                                                                                  |
| VUE_APP_CONTEXT             | Контекст запросов к backend. В application.yml параметр: `server.servlet.context-path`.                                                                  |
| VUE_APP_SSO_LOCATION_HEADER | Спец. заголовок для процесса аутентификации. В application.yml параметр: `spring.security.oauth2.authorizationserver.custom-handler-header-name`.        |

## Запуск frontend приложения вручную

Для запуска frontend приложения вручную используйте следующую команду:

```shell
npm run serve
```

Обратите внимание, что frontend приложение будет работать на порту 8181. Порт можно поменять
в [vue.config.js](client/vue.config.js).

Если вы хотите отдельно запустить frontend приложение и отдельно backend приложение, то не забудьте поменять следующие
значения в [application-security.yml](../src/main/resources/application-security.yml).

```yaml
authentication-success-url: http://localhost:8181${server.servlet.context-path}/client/oauth/continue
```


