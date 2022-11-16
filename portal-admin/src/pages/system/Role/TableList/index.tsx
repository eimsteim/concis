import {
  PageContainer,
} from '@ant-design/pro-components';

import React, {useState} from 'react';
import RoleTreeTable from "@/pages/system/Role/components/RoleTreeTable";

/**
 * 最终合体效果
 * @constructor
 */
const TableList: React.FC = () => {

  return (
    <PageContainer>
      {/*角色树表格*/}
      <RoleTreeTable
        showFooterToolbar = {true}//是否显示底部的删除确认栏
        showMiddleToolBar = {true}//是否显示中部的工具栏（一般用来显示'新建'按钮）
        showSearchToolBar = {true}//是否显示搜索工具栏
        showTableOptions  = {true}//是否在表格中显示操作栏
        onSelectRow={(id, name) => {
        // setRoleId(id)
        // setRoleName(name)
      }} />
    </PageContainer>
  );
}

export default TableList;
