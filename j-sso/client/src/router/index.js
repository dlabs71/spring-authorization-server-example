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
        component: HomeView
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

        // если пользователь не авторизован и мы переходим на любую страницу кроме "login" и "registration",
        // то перенаправлять нас на страницу "login"
        if (!store.getters.isAuth && !["login", "registration"].includes(to.name)) {
            router.replace({name: 'login'});
            return;
        }
        next();
    }
});

export default router;
