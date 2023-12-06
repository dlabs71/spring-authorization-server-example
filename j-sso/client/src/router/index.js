import {createRouter, createWebHistory} from 'vue-router';
import SignView from '../views/sign-view/sign-view';
import LoginForm from '../views/sign-view/login/login';
import HomeView from '../views/home/home.vue';
import RegistrationForm from '../views/sign-view/registration/registration.vue'
import store from "@/store";

const routes = [
    {
        path: '/auth',
        component: SignView,
        children: [
            {
                path: 'login',
                name: 'login',
                component: LoginForm,
            },
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
    history: createWebHistory("/client"),
    routes,
});

router.beforeEach((to, from, next) => {
    if (to.name && to.path) {
        if (!store.getters.isAuth && !["login"].includes(to.name)) {
            router.replace({name: 'login'});
            return;
        }
        next();
    }
});

export default router;
