import axios from 'axios'
import { Message } from 'element-ui'

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API || '',
  timeout: 30000
})

service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.code === '000000000') {
      return res.data
    }
    Message.error(res.message || '请求失败')
    return Promise.reject(new Error(res.message || '请求失败'))
  },
  error => {
    Message.error(error.message || '网络异常')
    return Promise.reject(error)
  }
)

export default service
