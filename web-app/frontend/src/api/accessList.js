import request from '@/utils/request'

export function page(query) {
  return request({
    url: '/alert-access-list',
    method: 'get',
    params: query
  })
}

export function detail(id) {
  return request({
    url: '/alert-access-list/detail',
    method: 'get',
    params: { id }
  })
}

export function names(type) {
  return request({
    url: '/alert-access-list/names',
    method: 'get',
    params: type != null ? { type } : {}
  })
}

export function add(data) {
  return request({
    url: '/alert-access-list',
    method: 'post',
    data
  })
}

export function update(data) {
  return request({
    url: '/alert-access-list',
    method: 'put',
    data
  })
}

export function remove(ids) {
  return request({
    url: '/alert-access-list/delete',
    method: 'post',
    data: Array.isArray(ids) ? ids : [ids]
  })
}

export function toggleStatus(id) {
  return request({
    url: '/alert-access-list/status',
    method: 'put',
    data: { id }
  })
}
