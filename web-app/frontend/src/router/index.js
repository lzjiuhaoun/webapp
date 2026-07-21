import Vue from 'vue'
import Router from 'vue-router'
import AlertLayout from '@/views/alert/AlertLayout'
import AccessListConfig from '@/views/alert/AccessListConfig'
import AlertRuleConfig from '@/views/alert/AlertRuleConfig'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/alert',
      component: AlertLayout,
      redirect: '/alert/rule',
      children: [
        {
          path: 'rule',
          name: 'AlertRuleConfig',
          component: AlertRuleConfig
        },
        {
          path: 'access-list',
          name: 'AccessListConfig',
          component: AccessListConfig
        }
      ]
    },
    {
      path: '/',
      redirect: '/alert/rule'
    }
  ]
})
