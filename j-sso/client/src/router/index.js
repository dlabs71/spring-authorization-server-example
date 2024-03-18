import {createRouter, createWebHistory} from 'vue-router';
import SignView from '../views/sign-view/sign-view';
import LoginForm from '../views/sign-view/login/login';
import HomeView from '../views/home/home.vue';
import RegistrationForm from '../views/sign-view/registration/registration.vue'
import store from "@/store";

const routes = [
    {
        // всё что касается авторизации / регистрации будет находиться по пути '/auth'
        path: '/auth',
        component: SignView,
        children: [
            {
                path: 'login',
                name: 'login',
                component: LoginForm,
            },

            // добавляем путь для формы регистрации
            {
                path: 'registration',
                name: 'registration',
                component: RegistrationForm,
            },
        ]
    },
    {
        path: '/home',
        name: 'home',
        component: HomeView
    },
    {
        path: '/',
        redirect: '/home'
    }
];

const router = createRouter({

    // Добавим "контекст" для клиента
    history: createWebHistory("/client"),
    routes,
});

// Добавим хук, который будет выполняться при каждом переходе и проверять авторизован ли пользователь.
// Если нет, то будет совершать переход на страницу входа.
router.beforeEach((to, from, next) => {
    if (to.name && to.path) {

        // Если пользователь не авторизован и мы переходим на любую страницу кроме "login" и "registration",
        // то перенаправлять нас на страницу "login"
        //
        // home - нужен так как в сценарии, когда мы просто хотим зайти в SSO через строний сервис (например yandex),
        // success handler перенаправляет нас на страницу /home. Но так как, данное приложение не загрузило ещё
        // текущего пользователя, то оно нас просто перенаправит на страницу логина.
        // Поэтому, обработка неавторизованной сессии в home вынесена в сам компонент home.vue.
        if (!store.getters.isAuth && !["login", "registration", "home"].includes(to.name)) {
            router.replace({name: 'login'});
            return;
        }
        next();
    }
});

export default router;
