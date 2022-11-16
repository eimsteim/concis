// @ts-ignore
/* eslint-disable */

declare namespace API {

  type PageParams = {
    current?: number;
    pageSize?: number;
  };

  //角色管理
  type RoleListItem = {
    id?: number;
    parentId?: number;
    name?: string;
    levels?: number;
    sn?: number;
    code?: string;
  }

  type RoleList = {
    data?: RoleListItem[];
    /** 列表的内容总数 */
    total?: number;
    success?: boolean;
  };

  type ErrorResponse = {
    /** 业务约定的错误码 */
    errorCode: string;
    /** 业务上的错误信息 */
    errorMessage?: string;
    /** 业务上的请求是否成功 */
    success?: boolean;
  };
}
