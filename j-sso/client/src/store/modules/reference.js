const state = {
    authMethods: null,
    grantTypes: null,
    scopes: null
}

const getters = {
    getAuthMethods: state => state.authMethods,
    getGrantTypes: state => state.grantTypes,
    getScopes: state => state.scopes
}

const mutations = {
    SET_AUTH_METHODS(state, data) {
        state.authMethods = data;
    },
    SET_GRANT_TYPES(state, data) {
        state.grantTypes = data;
    },
    SET_SCOPES(state, data) {
        state.scopes = data;
    },
    CLEAR_ALL(state) {
        state.authMethods = null;
        state.grantTypes = null;
        state.scopes = null;
    }
}

const actions = {
    setAuthMethods(context, data) {
        context.commit('SET_AUTH_METHODS', data);
    },
    setGrantTypes(context, data) {
        context.commit('SET_GRANT_TYPES', data);
    },
    setScopes(context, data) {
        context.commit('SET_SCOPES', data);
    },
    clearAll(context) {
        context.commit('CLEAR_ALL');
    }
}

export default {
    state,
    getters,
    mutations,
    actions
}
