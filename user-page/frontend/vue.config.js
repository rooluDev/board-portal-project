const {defineConfig} = require('@vue/cli-service');

module.exports = defineConfig({
    transpileDependencies: true,
    lintOnSave: false,
    outputDir: './build',

    devServer: {
        proxy: {
            '/api': {
                target: process.env.VUE_APP_API_URL,
                changeOrigin: true,
            },
        },
    },

    pluginOptions: {
        vuetify: {
            // https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vuetify-loader
        }
    }
});
