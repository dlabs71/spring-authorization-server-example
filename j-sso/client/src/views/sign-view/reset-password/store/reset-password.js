const state = {
    data: null,
    resetPasswordStep: 0
}

const getters = {
    getResetPasswordData: state => state.data,
    getResetPasswordStep: state => state.resetPasswordStep
}

const mutations = {
    SET_RESET_DATA(state, data) {
        state.data = data;
    },
    SET_RESET_STEP(state, step) {
        state.resetPasswordStep = step;
    },
    NEXT_RESET_STEP(state) {
        state.resetPasswordStep += 1;
    },
    BACK_RESET_STEP(state) {
        state.resetPasswordStep -= 1;
    }
}

const actions = {
    setResetData(context, data) {
        context.commit('SET_RESET_DATA', data);
    },
    setResetStep(context, step) {
        context.commit('SET_RESET_STEP', step);
    },
    nextResetStep(context) {
        context.commit('NEXT_RESET_STEP');
    },
    backResetStep(context) {
        context.commit('BACK_RESET_STEP');
    }
}

export default {
    state,
    getters,
    mutations,
    actions
}
