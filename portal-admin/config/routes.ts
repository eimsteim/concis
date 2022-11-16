export default [
  {
    path: '/user',
    layout: false,
    routes: [
      {
        name: 'login',
        path: '/user/login',
        component: './system/User/Login',
      },
      {
        component: './404',
      },
    ],
  },
  {
    path: '/welcome',
    name: 'welcome',
    icon: 'smile',
    component: './Welcome',
  },
  {
    path: '/system',
    name: 'system',
    icon: 'crown',
    //access: 'canAdmin',
    routes: [
      {
        path: '/system/user',
        name: '用户管理',
        component: './system/User/TableList',
      },
      {
        path: '/system/role',
        name: '角色管理',
        component: './system/Role/TableList',
      },
      {
        path: '/system/menu',
        name: '菜单管理',
        component: './system/Menu/TableList',
      },
      {
        component: './404',
      },
    ],
  },
  {
    name: 'list.table-list',
    icon: 'table',
    path: '/list',
    component: './system/User/TableList',
  },
  {
    path: '/',
    redirect: '/welcome',
  },
  {
    component: './404',
  },
];
