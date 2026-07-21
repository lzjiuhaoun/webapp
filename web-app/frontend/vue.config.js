const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8081,
    proxy: {
      '/alert-access-list': {
        target: 'http://localhost:8543',
        changeOrigin: true
      },
      '/alert-rule': {
        target: 'http://localhost:8543',
        changeOrigin: true
      }
    }
  }
})
