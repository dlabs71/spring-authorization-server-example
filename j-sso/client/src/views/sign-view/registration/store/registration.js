const state = {
    data: null,
    registrationStep: 0
}

const getters = {
    getRegistrationData: state => state.data,
    getRegistrationStep: state => state.registrationStep
}

const mutations = {
    SET_REGISTRATION_DATA(state, data) {
        state.data = data;
    },
    SET_REGISTRATION_STEP(state, step) {
        state.registrationStep = step;
    },
    NEXT_REGISTRATION_STEP(state) {
        state.registrationStep += 1;
    },
    BACK_REGISTRATION_STEP(state) {
        state.registrationStep -= 1;
    }
}

const actions = {
    setRegistrationData(context, data) {
        context.commit('SET_REGISTRATION_DATA', data);
    },
    setRegistrationStep(context, step) {
        context.commit('SET_REGISTRATION_STEP', step);
    },
    nextRegistrationStep(context) {
        context.commit('NEXT_REGISTRATION_STEP');
    },
    backRegistrationStep(context) {
        context.commit('BACK_REGISTRATION_STEP');
    }
}

export default {
    state,
    getters,
    mutations,
    actions
}
