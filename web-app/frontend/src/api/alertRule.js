import request from '@/utils/request'

export function page(query) {
  return request({
    url: '/alert-rule',
    method: 'get',
    params: query
  })
}

export function detail(id) {
  return request({
    url: '/alert-rule/detail',
    method: 'get',
    params: { id }
  })
}

export function add(data) {
  return request({
    url: '/alert-rule',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/alert-rule',
    method: 'put',
    data
  })
}

export function remove(ids) {
  return request({
    url: '/alert-rule/delete',
    method: 'post',
    data: Array.isArray(ids) ? ids : [ids]
  })
}

export function toggleStatus(id) {
  return request({
    url: '/alert-rule/status',
    method: 'put',
    data: { id }
  })
}
