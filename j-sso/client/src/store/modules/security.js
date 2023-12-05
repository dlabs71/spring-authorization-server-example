const state = {
    user: null,
    authorities: []
}

const getters = {
    getAuthUser: state => state.user,
    isAuth: state => !!state.user
}

const mutations = {
    SET_AUTH_USER(state, data) {
        state.user = data;
    },
    SET_AUTHORITIES(state, authorities) {
        state.authorities = authorities;
    },
    RESET_AUTH(state) {
        state.user = null;
        state.authorities = [];
    }
}

const actions = {
    setAuthUser(context, securitySessionUser) {
        context.commit('SET_AUTH_USER', securitySessionUser);
        context.commit('SET_AUTHORITIES', securitySessionUser.authorities);
    },
    resetAuth(context) {
        context.commit('RESET_AUTH');
    }
}

export default {
    state,
    getters,
    mutations,
    actions
}
