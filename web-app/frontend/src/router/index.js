import Vue from 'vue'
import Router from 'vue-router'
import AccessListConfig from '@/views/alert/AccessListConfig'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/alert/access-list',
      name: 'AccessListConfig',
      component: AccessListConfig
    }
  ]
})
