// @ts-ignore
/* eslint-disable */
// import { request } from 'umi';
import request from 'umi-request';

// request拦截器, 改变url 或 options.
request.interceptors.request.use((url, options) => {
  let token = localStorage.getItem('token');
  if (null === token) {
    token = '';
  }
  const authHeader = { Authorization: `${token}` };
  return {
    url: url,
    options: { ...options, interceptors: true, headers: authHeader },
  };
});

/** 查询角色树，用于填充到树形表格 */
export async function fetchRoleData(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/role/listTree', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 新建角色 POST /api/role/add */
export async function addRole(
  body?: object,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/role/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 修改角色 PUT /api/role/update */
export async function updateRole(
  body?: object,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/role/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除角色 DELETE /api/role/remove */
export async function removeRole(
  body?: object,
  //如果需要在请求时除了selected还需要传递其他参数，可以用下面的方式
  // data: { //在data中定义通过body传递的参数
  //   selected?: object
  // },
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/role/remove', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    //如果需要在请求时除了selected还需要传递其他参数，可以用下面的方式
    // data: {
    //   ...data,
    // },
    ...(options || {}),
  });
}

/** 角色授权 /api/role/setAuthority */
export async function roleGrant(
  data: { //在data中定义通过body传递的参数
    roleId?: number
    menuIds?: number[]
  },
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/role/setAuthority', {
    method: 'POST',
    //如果需要在请求时除了selected还需要传递其他参数，可以用下面的方式
    data: {
      ...data,
    },
    ...(options || {}),
  });
}

/**
 * 查询用户拥有的所有角色ID
 */
export async function fetchRoleIdsByUserId(userId?: number) {
  return request('/api/role/listRoleIdsByUserId', {
    method: 'POST',
    params: {
      userId: userId
    },
  });
}
