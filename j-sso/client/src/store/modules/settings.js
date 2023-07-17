const state = {
    deviceType: 'md'
}

const getters = {
    getDeviceType: state => state.deviceType
}

const mutations = {
    SET_DEVICE_TYPE(state, deviceType) {
        state.deviceType = deviceType
    }
}

const actions = {
    setDeviceType(context, deviceType) {
        context.commit('SET_DEVICE_TYPE', deviceType)
    }
}

export default {
    state,
    getters,
    mutations,
    actions
}
