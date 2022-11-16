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

/** 查询左侧菜单树 */
export async function fetchMenuData(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/menu/listTree', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 查询角色对应的菜单权限，参数为空时返回所有菜单 */
//export async function fetchMenuDataByRoleId(options?: { [key: number]: any }) {
export async function fetchMenuDataByRoleId(id?: number) {
  return request('/api/menu/listTreeByRoleId', {
    method: 'POST',
    params: {
      id: id
    },
  });
}

export async function fetchMenuIdsByRoleId(id: number) {
  return request('/api/menu/listMenuIdsByRoleId', {
    method: 'POST',
    params: {
      id: id
    },
  });
}

export async function fetchMenuDataByRoleParentId(id: number) {
  return request('/api/menu/listTreeByRoleParentId', {
    method: 'POST',
    params: {
      id: id
    },
  });
}

/** 获取所有菜单信息，不根据角色ID限定查询结果 */
export async function fetchAllMenuData() {
  return request('/api/menu/listAllByTree', {
    method: 'POST',
  });
}

/** 新建菜单 POST /api/menu/add */
export async function addMenu(
  body?: object,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/menu/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 修改用户 PUT /api/menu/update */
export async function updateMenu(
  body?: object,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/menu/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除菜单 DELETE /api/menu/remove */
export async function removeMenu(
  body?: object,
  //如果需要在请求时除了selected还需要传递其他参数，可以用下面的方式
  // data: { //在data中定义通过body传递的参数
  //   selected?: object
  // },
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/menu/remove', {
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
