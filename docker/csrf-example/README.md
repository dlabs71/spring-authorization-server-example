# HTML страница демонстрирующая атаку CSRF

Цель: выполнить CSRF атаку сделав администратором j-sso аккаунт с email злоумышленника.

Пусть имеется сайт злоумышленника на котором существует скрытый вызов endpoint-а назначения прав администратора на
аккаунт с email злоумышленника (evil_mail@example.com). Также, предполагается что в j-sso уже существует аккаунт с
email - evil_mail@example.com (соответствующие скрипты создания данного пользователя
добавлены [users-data.sql](../../j-sso/database/release-1.0.0/data/dev/users-data.sql)).

```html

<form action="http://localhost:7777/admin-user/assign-admin?email=evil_mail%40example.com"
      method="post"
      enctype="text/plain">
    <button type="submit">Press button</button>
</form>
```

Предположим, что пользователь уже авторизовался в j-sso. Это означает, что установленны куки SESSION. Далее,
пользователь переходит на сайт злоумышленника (http://localhost:9090/index.html) и ничего не подозревая нажимает на
кнопку "Press button". Происходит вызов endpoint-а j-sso и пользователь, сам не зная того, назначил права администратора
пользователю с email - evil_mail@example.com. Таким образом демонстрируется CSRF атака.