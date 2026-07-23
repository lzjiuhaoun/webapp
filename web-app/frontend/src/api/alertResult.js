import request from '@/utils/request'

export function page(query) {
  return request({
    url: '/alert-result',
    method: 'get',
    params: query
  })
}

export function detail(id) {
  return request({
    url: '/alert-result/detail',
    method: 'get',
    params: { id }
  })
}

export function remove(ids) {
  return request({
    url: '/alert-result/delete',
    method: 'post',
    data: Array.isArray(ids) ? ids : [ids]
  })
}
