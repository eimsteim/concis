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

/** 此处后端没有提供注释 GET /api/notices */
export async function getNotices(options?: { [key: string]: any }) {
  return request<API.NoticeIconList>('/api/notices', {
    method: 'GET',
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


/** 获取规则列表 GET /api/rule */
export async function rule(
  params: {
    // query
    /** 当前的页码 */
    current?: number;
    /** 页面的容量 */
    pageSize?: number;
  },
  options?: { [key: string]: any },
) {
  return request<API.RuleList>('/api/rule', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 新建规则 PUT /api/rule */
export async function updateRule(options?: { [key: string]: any }) {
  return request<API.RuleListItem>('/api/rule', {
    method: 'PUT',
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

/** 删除规则 DELETE /api/user/remove */
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

/** 查询左侧菜单树 */
export async function fetchMenuData(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/menu/listTree', {
    method: 'POST',
    ...(options || {}),
  });
}
