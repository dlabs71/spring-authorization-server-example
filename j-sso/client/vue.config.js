const {defineConfig} = require('@vue/cli-service');

module.exports = defineConfig({
    transpileDependencies: true,
    publicPath: process.env.VUE_APP_NODE_ENV !== "development" ? "/static" : "/",

    devServer: {
        port: 8080
    },

    pluginOptions: {
        vuetify: {
            styles: {
                configFile: "src/assets/scss/settings.scss"
            }
        }
    }
});
