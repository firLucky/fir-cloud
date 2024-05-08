import Router from 'vue-router'
import Test from "@/components/demoTest.vue";

import Vue from "vue";
import loginTest from "@/components/loginTest.vue";

Vue.use(Router)

const routes = [
  {
    path: '/',
    name: 'home',
    component: loginTest
  },
  {
    path: '/test',
    name: 'test',
    component: Test
  },
]


const router = new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: routes
})

export default router
