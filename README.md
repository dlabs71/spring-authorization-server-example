# Строим свой SSO сервер используя Spring Authorization Server

<p align="center">
    <img src="https://raw.githubusercontent.com/dlabs71/spring-authorization-server-example/chapter-1/article-images/poster.png" alt="Постер"/>
</p>

На днях я решил сделать под все свои pet-проекты собственный SSO сервис, дабы не заморачиваться каждый раз с
авторизацией и аутентификацией. Возиться с этим особо долго мне не хотелось. Все таки это для pet-проектов. Поэтому
выбор пал на `Spring Security`. Мне давно уже было интересно посмотреть в действии как на `Spring Boot 3`, так и новый
`Spring Authorization Server` версии `1.x.x`. В данной статье речь пойдёт о проблемах и их решениях при построении
собственного SSO. А также я поставил себе ряд интересных требований, с которыми я постараюсь справиться и рассказать о
своём опыте.

### Часть 1: [Строим свой SSO сервер используя Spring Authorization Server](https://habr.com/ru/articles/737548/)
### Часть 2: [Строим свой SSO. PostgreSQL и ролевая модель](https://habr.com/ru/articles/746698/)
### Часть 3: [Строим свой SSO. Часть 3: Redis, Swagger, Vue.js](https://habr.com/ru/articles/748584/)
### Часть 4: [Строим свой SSO. Часть 4: Vue.js, Регистрация, Сброс пароля](https://habr.com/ru/articles/784552/)