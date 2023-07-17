export default {
    computed: {
        deviceType() {
            return this.$store.getters.getDeviceType
        },
        isSm() {
            return this.deviceType === "sm";
        },
        smAndDown() {
            return this.deviceType === "sm" || this.deviceType === 'xs';
        }
    }
}
