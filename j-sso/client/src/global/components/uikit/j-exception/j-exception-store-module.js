import {v4 as uuidv4} from 'uuid';

const state = {
    exceptionId: null,
    status: null,
    description: null,
    stacktrace: [],
    notificationId: null,
    notificationLevel: null,
    notificationMessage: null
}

const getters = {
    getExceptionId: state => state.exceptionId,
    getStatus: state => state.status,
    getDescription: state => state.description,
    getStacktrace: state => state.stacktrace,
    getNotificationId: state => state.notificationId,
    getNotificationLevel: state => state.notificationLevel,
    getNotificationMessage: state => state.notificationMessage
}

const mutations = {
    SET_EXCEPTION(state, payload) {
        state.exceptionId = uuidv4();
        state.status = payload.status;
        state.description = payload.description;
        state.stacktrace = payload.stacktrace;
    },
    RESET_EXCEPTION(state) {
        state.exceptionId = null;
        state.status = null;
        state.description = null;
        state.stacktrace = [];
    },
    SET_NOTIFICATION(state, payload) {
        state.notificationId = uuidv4();
        state.notificationLevel = payload.level;
        state.notificationMessage = payload.message;
    },
    RESET_NOTIFICATION(state) {
        state.notificationId = null;
        state.notificationLevel = null;
        state.notificationMessage = null;
    },
}

const actions = {
    setException(context, payload) {
        context.commit('SET_EXCEPTION', payload);
    },
    setNotification(context, payload) {
        context.commit('SET_NOTIFICATION', payload);
    },
    resetException(context) {
        context.commit('RESET_EXCEPTION');
    },
    resetNotification(context) {
        context.commit('RESET_NOTIFICATION');
    }
}

export default {
    state,
    getters,
    mutations,
    actions
}
