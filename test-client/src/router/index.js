import {createRouter, createWebHistory} from 'vue-router'
import Home from '../views/home.vue'
import Login from '../views/login.vue'
import LoginService from "@/services/login-service";

const routes = [
    {
        path: '/',
        name: 'home',
        component: Home
    },
    {
        path: '/login',
        name: 'login',
        component: Login
    }
]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
});


router.beforeEach((to, from, next) => {
    if (to.path === '/code' && to.query.code != null) {
        LoginService.getTokens(to.query.code).then(() => {
            next({name: 'home'});
        });
    } else {
        next()
    }
});

export default router
