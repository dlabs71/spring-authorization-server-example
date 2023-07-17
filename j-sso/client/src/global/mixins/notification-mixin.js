export default {
    methods: {
        showNotify(title, message, type) {
            this.$notify({
                group: 'main',
                type: type,
                title: title,
                text: message
            })
        },
        showSNotify(title, message) {
            this.showNotify(title, message, "success");
        },
        showENotify(title, message) {
            this.showNotify(title, message, "error");
        },
        showINotify(title, message) {
            this.showNotify(title, message, "info");
        },
        showWNotify(title, message) {
            this.showNotify(title, message, "warning");
        }
    }
};
