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

/** 获取当前的用户 GET /api/user/current */
export async function currentUser(options?: { [key: string]: any }) {
  return request<{
    data: API.CurrentUser;
  }>('/api/user/current', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 退出登录接口 POST /api/user/logout */
export async function outLogin(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/user/logout', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 登录接口 POST /api/login/account */
export async function login(body: API.LoginParams, options?: { [key: string]: any }) {
  //对密码加密
  body.password = Buffer.from(String(body.password), 'utf-8').toString('base64');

  return request<API.LoginResult>('/api/user/login/account', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 分页查询系统用户列表 GET /api/user/list */
export async  function user(
  params: {
    // query
    /** 当前的页码 */
    current?: number;
    /** 页面的容量 */
    pageSize?: number;
  },
  options?: { [key: string]: any },
) {
  return request<API.UserList>('/api/user/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 新建用户 POST /api/user/add */
export async function addUser(
  body?: object,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/user/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 修改用户 PUT /api/user/update */
export async function updateUser(
  body?: object,
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/user/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 删除用户 DELETE /api/user/remove */
export async function removeUser(
  body?: object,
  //如果需要在请求时除了selected还需要传递其他参数，可以用下面的方式
  // data: { //在data中定义通过body传递的参数
  //   selected?: object
  // },
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/user/remove', {
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


/** 用户授权 /api/user/setAuthority */
export async function userGrant(
  data: { //在data中定义通过body传递的参数
    userId?: number
    roleIds?: number[]
  },
  options?: { [key: string]: any }
) {
  return request<Record<string, any>>('/api/user/setAuthority', {
    method: 'POST',
    //如果需要在请求时除了selected还需要传递其他参数，可以用下面的方式
    data: {
      ...data,
    },
    ...(options || {}),
  });
}
