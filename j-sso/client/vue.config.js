const {defineConfig} = require('@vue/cli-service');
const context = process.env.VUE_APP_CONTEXT;

module.exports = defineConfig({
    transpileDependencies: true,
    publicPath: process.env.VUE_APP_NODE_ENV !== "development" ? context + "/static" : "/",

    devServer: {
        port: 8181,
        client: {
            overlay: false
        }
    },

    css: {
        extract: process.env.VUE_APP_NODE_ENV !== "development"
    },

    pluginOptions: {
        vuetify: {
            styles: {
                configFile: "src/assets/scss/settings.scss"
            }
        }
    }
});
