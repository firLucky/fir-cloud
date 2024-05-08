

const { defineConfig } = require('@vue/cli-service')
// 引入polyfill
const NodePolyfillPlugin = require('node-polyfill-webpack-plugin')

module.exports = defineConfig({
  transpileDependencies: true,
  // 引入polyfill
  configureWebpack: {
    plugins: [
      new NodePolyfillPlugin({})
    ]
  },
})
