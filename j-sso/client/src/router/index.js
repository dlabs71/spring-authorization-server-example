import {createRouter, createWebHistory} from 'vue-router';
import SignView from '../views/sign-view/sign-view';
import LoginForm from '../views/sign-view/login/login';
import HomeView from '../views/main/home/home.vue';
import MainView from '../views/main/main-view.vue';
import RegistrationForm from '../views/sign-view/registration/registration.vue';
import ResetPasswordForm from '@/views/sign-view/reset-password/reset-password.vue';
import store from "@/store";
import UserEventsView from '@/views/main/user-events/user-events-view.vue';
import UserTokensView from '@/views/main/user-tokens/user-tokens-view.vue';
import AdminOauthClientsView from "../views/main/admin-oauth-clients/admin-oauth-clients-view.vue";
import AdminOauthClientView from "../views/main/admin-oauth-clients/admin-oauth-client.vue";
import AdminUserView from "../views/main/admin-user/admin-user-view.vue";
import LoginService from "@/views/sign-view/login/service/login-service";

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
            {
                path: 'reset-password',
                name: 'reset-password',
                component: ResetPasswordForm,
            }
        ]
    },
    {
        path: "/",
        component: MainView,
        redirect: '/home',
        children: [
            {
                path: 'home',
                name: 'home',
                component: HomeView
            },
            {
                path: 'events',
                name: 'events',
                component: UserEventsView
            },
            {
                path: 'tokens',
                name: 'tokens',
                component: UserTokensView
            },
            {
                path: 'admin-oauth-clients',
                redirect: '/admin-oauth-clients/list',
                children: [
                    {
                        path: 'list',
                        name: 'admin-oauth-clients',
                        component: AdminOauthClientsView,
                    },
                    {
                        path: ':clientId?',
                        name: 'admin-oauth-client',
                        component: AdminOauthClientView,
                    },
                    {
                        path: '/new/:clientId',
                        name: 'admin-oauth-client-new',
                        component: AdminOauthClientView,
                    }
                ]
            },
            {
                path: 'admin-users',
                name: 'admin-users',
                component: AdminUserView
            }
        ]
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
    if (to.path === '/oauth/continue') {
        LoginService.afterOauthLoginSuccess().then(()=>{
            router.replace({name: 'home'});
        });
        return;
    }

    if (to.name && to.path) {

        // Если пользователь не авторизован и мы переходим на любую страницу кроме "login" и "registration",
        // то перенаправлять нас на страницу "login"
        //
        // home - нужен так как в сценарии, когда мы просто хотим зайти в SSO через строний сервис (например yandex),
        // success handler перенаправляет нас на страницу /home. Но так как, данное приложение не загрузило ещё
        // текущего пользователя, то оно нас просто перенаправит на страницу логина.
        // Поэтому, обработка неавторизованной сессии в home вынесена в сам компонент home.vue.
        if (!store.getters.isAuth && !["login", "registration", "reset-password"].includes(to.name)) {
            router.replace({name: 'login'});
            return;
        }
        next();
    }
});

export default router;
