<template>
    <div class="menu-card elevation-2">
        <div class="admin-badge" v-if="!!adminBadgeText">
            {{ adminBadgeText }}
        </div>
        <div class="header-section">
            <j-avatar
                :user-name="fullUserName"
                :url="avatarUrl"
            />
            <h3 class="username">{{ fullUserName }}</h3>
        </div>
        <v-divider/>
        <div class="menu-section">
            <v-list density="compact">
                <v-list-item color="primary"
                             v-for="item in menuItems"
                             :active="item.activeRouteNames.includes(chosePage)"
                             @click="()=>goTo(item.routeName)"
                >
                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                </v-list-item>
            </v-list>
            <v-divider v-if="showAdminMenu"/>
            <v-list density="compact" v-if="showAdminMenu">
                <v-list-subheader>Администрирование</v-list-subheader>
                <v-list-item color="primary"
                             v-for="item in adminMenuItems"
                             :active="item.activeRouteNames.includes(chosePage)"
                             @click="()=>goTo(item.routeName)"
                >
                    <v-list-item-title>{{ item.title }}</v-list-item-title>
                </v-list-item>
            </v-list>
            <v-divider/>
            <v-list density="compact">
                <v-list-item @click="logout" base-color="primary">
                    <v-list-item-title>
                        <v-icon class="mr-1">logout</v-icon>
                        Выход
                    </v-list-item-title>
                </v-list-item>
            </v-list>
        </div>
    </div>
</template>

<script setup>
    import {useStore} from "vuex";
    import {useRoute, useRouter} from "vue-router";
    import {computed} from "vue";
    import {hasAnyAuthority} from "@/global/mixins/security-utils";
    import LoginService from '@/views/sign-view/login/service/login-service';

    const store = useStore();
    const router = useRouter();
    const route = useRoute();

    const user = computed(() => store.getters.getAuthUser);
    const fullUserName = computed(() => {
        let data = user.value;
        if (!data) {
            return '';
        }
        return `${data.lastName} ${data.firstName}`.trim();
    });
    const chosePage = computed(() => {
        return route.name;
    });
    const avatarUrl = computed(() => {
        if (!user.value) {
            return null;
        }
        return '/account/avatar/current?userId' + user.value.id + "&time=" + new Date().getTime();
    });

    let goTo = (name) => {
        router.push({name: name});
    };

    function logout() {
        LoginService.logout();
    }

    const menuItemList = [
        {
            value: "profile",
            title: "Профиль",
            routeName: "home",
            activeRouteNames: ["home"],
            authorities: ['GET_OWN_DATA']
        },
        {
            value: "events",
            title: "Активность",
            routeName: "events",
            activeRouteNames: ["events"],
            authorities: ['GET_OWN_EVENTS']
        },
        {
            value: "tokens",
            title: "Выданные токены",
            routeName: "tokens",
            activeRouteNames: ["tokens"],
            authorities: ['GET_OWN_TOKENS']
        }
    ];

    const adminMenuItemList = [
        {
            value: "admin-oauth-clients",
            title: "Oauth2 клиенты",
            routeName: "admin-oauth-clients",
            activeRouteNames: ["admin-oauth-clients", "admin-oauth-client"],
            authorities: ['GET_OAUTH_CLIENT_DATA']
        },
        {
            value: "admin-users",
            title: "Администраторы",
            routeName: "admin-users",
            activeRouteNames: ["admin-users"],
            authorities: ['GET_ADMIN_USER_DATA']
        }
    ];

    const menuItems = computed(() => {
        return menuItemList.filter(item => hasAnyAuthority(item.authorities));
    });
    const adminMenuItems = computed(() => {
        return adminMenuItemList.filter(item => hasAnyAuthority(item.authorities));
    });
    const showAdminMenu = computed(() => adminMenuItems.value.length > 0);
    const adminBadgeText = computed(() => {
        if (store.getters.isSuperuser) {
            return 'СУПЕР ПОЛЬЗОВ.';
        }
        if (store.getters.isAdmin) {
            return 'АДМИНИСТРАТОР';
        }
        return null;
    })
</script>

<style scoped lang="scss">
.menu-card {
    border-radius: 10px;
    max-width: 250px;
    min-width: 220px;
    background-color: white;
    margin-right: 0;
    margin-left: auto;
    min-height: 400px;
    padding-bottom: 10px;
    position: relative;

    .admin-badge {
        position: absolute;
        top: 25px;
        left: 5px;
        font-size: 10px;
        border-radius: 5px;
        border: 1px solid rgb(var(--v-theme-error));
        padding: 3px;
        color: rgb(var(--v-theme-error));
        pointer-events: none;
        transform: rotate(-30deg);
    }

    .header-section {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding-top: 30px;
        padding-bottom: 10px;

        .username {
            margin-top: 10px;
            text-overflow: ellipsis;
            white-space: nowrap;
            max-width: 90%;
            overflow: hidden;
        }
    }

    .menu-section {
        .v-list-item--active {
            border-left: #ac27f2 solid 3px !important;
        }

        .v-list-item {
            border-left: white solid 3px;
        }
    }
}
</style>