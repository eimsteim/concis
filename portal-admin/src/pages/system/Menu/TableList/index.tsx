import {
  PageContainer,
} from '@ant-design/pro-components';
import React from 'react';
import MenuTreeTable from "@/pages/system/Menu/components/MenuTreeTable";

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
// const handleAdd = async (fields: API.RoleListItem) => {
//   const hide = message.loading('正在新建角色');
//   try {
//     await addUser({ ...fields });
//     hide();
//     message.success('操作成功');
//     return true;
//   } catch (error) {
//     hide();
//     message.error('操作失败');
//     return false;
//   }
// };

/**
 * @en-US Update node
 * @zh-CN 更新节点
 *
 * @param fields
 */
// const handleUpdate = async (fields: API.RoleListItem) => {
//   const hide = message.loading('正在修改角色');
//   try {
//     await updateUser({ ...fields });
//     hide();
//     message.success('操作成功');
//     return true;
//   } catch (error) {
//     hide();
//     message.error('操作失败');
//     return false;
//   }
// };

/**
 * 菜单列表
 * @constructor
 */
const TableList: React.FC = () => {
  return (
    <PageContainer>
      <MenuTreeTable
        title="菜单树"
        showFooterToolbar={true}
        showMiddleToolBar={true}
        showSearchToolBar={true}
        showTableOptions ={true}
      />
    </PageContainer>
  );
}

export default TableList;
