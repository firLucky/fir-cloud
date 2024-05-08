import Vue from 'vue'
import App from './App.vue'
import router from './router'
import http from "@/api/index";
import securityUtils from "@/utils/securityUtils";

Vue.config.productionTip = false

Vue.prototype.$http = http;
Vue.prototype.$securityUtils = securityUtils;


import MessageBox from './components/MessageBox.vue'

Vue.component('MessageBox', MessageBox)

// 将 MessageBox 组件挂载到 Vue.prototype 上
Vue.prototype.$message = function ({ message, duration, description }) {
  const MessageBoxComponent = Vue.extend(MessageBox)

  const instance = new MessageBoxComponent({
    propsData: { message, duration, description }
  })

  const vm = instance.$mount()
  document.body.appendChild(vm.$el)

  setTimeout(() => {
    document.body.removeChild(vm.$el)
    vm.$destroy()
  }, duration * 1000)
}

// 在组件中使用 this.$message
// this.$message({ message: 'Hello world!', duration: 1.5, description: '' })


new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
