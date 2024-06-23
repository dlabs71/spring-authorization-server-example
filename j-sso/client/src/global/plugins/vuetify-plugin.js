// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/_styles.scss'

import {createVuetify} from 'vuetify/lib/framework'
import {aliases, md} from 'vuetify/lib/iconsets/md';
import {aliases as aliasesMdi, mdi} from 'vuetify/lib/iconsets/mdi';
import {en, ru} from 'vuetify/lib/locale';

export default {
    install(Vue, opts) {
        Vue.use(createVuetify({
            locale: {
                locale: 'ru',
                fallback: 'en',
                messages: {ru, en},
            },
            icons: {
                defaultSet: 'md',
                aliases,
                aliasesMdi,
                sets: {
                    md,
                    mdi
                }
            },
            theme: {
                cspNonce: 'dQw4w9WgXcQ',
                defaultTheme: 'appTheme',
                themes: {
                    appTheme: {
                        dark: false,
                        colors: {
                            background: '#FFFFFF',
                            surface: '#FFFFFF',
                            'surface-bright': '#FFFFFF',
                            'surface-light': '#EEEEEE',
                            'surface-variant': '#424242',
                            'on-surface-variant': '#EEEEEE',
                            primary: '#8A2BE2FF',
                            'primary-darken-1': '#1F5592',
                            secondary: '#48A9A6',
                            'secondary-darken-1': '#018786',
                            error: '#B00020',
                            info: '#2196F3',
                            success: '#4CAF50',
                            warning: '#FB8C00'
                        },
                        variables: {
                            'border-color': '#000000',
                            'border-opacity': 0.12,
                            'high-emphasis-opacity': 0.87,
                            'medium-emphasis-opacity': 0.60,
                            'disabled-opacity': 0.38,
                            'idle-opacity': 0.04,
                            'hover-opacity': 0.04,
                            'focus-opacity': 0.12,
                            'selected-opacity': 0.08,
                            'activated-opacity': 0.12,
                            'pressed-opacity': 0.12,
                            'dragged-opacity': 0.08,
                            'theme-kbd': '#212529',
                            'theme-on-kbd': '#FFFFFF',
                            'theme-code': '#F5F5F5',
                            'theme-on-code': '#000000'
                        }
                    }
                }
            }
        }));
    }
}
