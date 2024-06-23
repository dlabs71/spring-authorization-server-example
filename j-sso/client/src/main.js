import {createApp} from 'vue';
import App from './App.vue';
import router from './router';
import store from './store';
import GlobalLib from './global/global-lib-plugin';

import '@/assets/scss/index.scss';

createApp(App)
    .use(router)
    .use(store)
    .use(GlobalLib, {
        store: store,
        router: router
    })
    .mount('#app');
