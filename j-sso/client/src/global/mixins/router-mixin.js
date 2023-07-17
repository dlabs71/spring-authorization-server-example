export default {
    methods: {
        goTo(name, options = {}) {
            this.$router.push({name: name, ...options})
        },
        goToHome() {
            this.$router.push({name: 'news'})
        }
    }
}