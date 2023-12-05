import {createRouter, createWebHistory} from 'vue-router';
import SignView from '../views/sign-view/sign-view';
import LoginForm from '../views/sign-view/login/login';
import HomeView from '../views/home/home.vue';

const routes = [
    {
        path: '/',
        component: SignView,
        children: [
            {
                path: 'login',
                name: 'login',
                component: LoginForm,
            }
        ]
    },
    {
        path: '/home',
        component: HomeView
    }
];

const router = createRouter({
    history: createWebHistory("/"),
    routes,
});

export default router;
