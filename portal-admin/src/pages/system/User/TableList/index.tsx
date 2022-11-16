import {addUser, removeUser, updateUser, user} from '@/services/system/user/api';
import { PlusOutlined, ExclamationCircleOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns, ProDescriptionsItemProps } from '@ant-design/pro-components';
import {
  FooterToolbar,
  ModalForm,
  ProForm,
  ProFormDatePicker,
  PageContainer,
  ProDescriptions,
  ProFormText,
  ProFormSelect,
  ProTable,
  DrawerForm,
} from '@ant-design/pro-components';
import { Button, Modal, message } from 'antd';
import React, {useRef, useState} from 'react';
import { FormattedMessage, useIntl } from 'umi';
import RoleTreeTable from "@/pages/system/Role/components/RoleTreeTable";
import {userGrant} from "@/services/system/user/api";
const { confirm } = Modal;

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: API.UserListItem) => {
  const hide = message.loading('正在新建用户');
  try {
    await addUser({ ...fields });
    hide();
    message.success('操作成功');
    return true;
  } catch (error) {
    hide();
    message.error('操作失败');
    return false;
  }
};

/**
 * @en-US Update node
 * @zh-CN 更新节点
 *
 * @param fields
 */
const handleUpdate = async (fields: API.UserListItem) => {
  const hide = message.loading('正在修改用户');
  try {
    await updateUser({ ...fields });
    hide();
    message.success('操作成功');
    return true;
  } catch (error) {
    hide();
    message.error('操作失败');
    return false;
  }
};

/**
 *  Delete node
 * @zh-CN `删除`节点
 *
 * @param selectedRows
 */
const handleRemove = async (selectedRows: API.UserListItem[]) => {
  const hide = message.loading('正在删除用户');
  if (!selectedRows) return true;
  try {
    await removeUser(selectedRows.map((row) => row.id));
    //如果需要在请求时除了selected还需要传递其他参数，可以用下面的方式
    // await removeUser({
    //   selected: selectedRows.map((row) => row.id),
    // });
    hide();
    message.success('操作成功');
    return true;
  } catch (error) {
    hide();
    message.error('操作失败');
    return false;
  }
};



const TableList: React.FC = () => {
  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalVisible, handleCreateModalVisible] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);

  const [showDetail, setShowDetail] = useState<boolean>(false);
  const [showGrant, setShowGrant] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.UserListItem>();
  const [selectedRowsState, setSelectedRows] = useState<API.UserListItem[]>([]);

  //点击授权按钮时，在角色树的勾选项发生改变时，需要同步改变在用户列表中的Drawer的属性，使其在下方弹出确认按钮
  // setRoleIds 会被传递到子组件 RoleTreeTable，在子组件修改选中的权限时，会通过 setRoleIds 将修改结果同步到下面的 DrawerForm 组件
  const [roleIds, setRoleIds] = useState<number[]>([]);

  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */
  const intl = useIntl();

  /**
   * @en-US Show Remove Confirm
   * @zh-CN 在删除用户之前显示提示
   */
  const showRemoveConfirm = () => {
    confirm({
      title: '确认删除这 ' + selectedRowsState.length + ' 项？',
      icon: <ExclamationCircleOutlined />,
      content: '已删除的用户将不会显示在用户列表中',
      okText: '删除',
      okType: 'danger',
      cancelText: '取消',
      onOk() {
        handleRemove(selectedRowsState);
        setSelectedRows([]);
        actionRef.current?.reloadAndRest?.();
      },
      onCancel() {
      },
    });
  };

  const columns: ProColumns<API.UserListItem>[] = [
    {
      title: <FormattedMessage id="pages.searchTable.updateForm.userName.nameLabel" defaultMessage="用户名称" />,
      dataIndex: 'name',
      tip: '输入用户名或账号模糊搜索',
      render: (dom, entity) => {
        return (
          <a
            onClick={() => {
              setCurrentRow(entity);
              setShowDetail(true);
            }}
          >
            {dom}
          </a>
        );
      },
    },
    {
      title: <FormattedMessage id="pages.searchTable.updateForm.userAccount.nameLabel" defaultMessage="账号" />,
      dataIndex: 'account',
      //search: false,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="pages.searchTable.updateForm.userRole.nameLabel" defaultMessage="角色" />,
      dataIndex: 'roleName',
      //search: false,
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="pages.searchTable.updateForm.userPhone.nameLabel" defaultMessage="电话" />,
      dataIndex: 'phone',
      //search: false,
      hideInSearch: true,
      hideInTable: true,
    },
    {
      title: <FormattedMessage id="pages.searchTable.titleStatus" defaultMessage="Status" />,
      dataIndex: 'status',
      //hideInForm: true,
      valueEnum: {
        0: {
          text: (
            <FormattedMessage
              id="pages.searchTable.nameStatus.default"
              defaultMessage="Shut down"
            />
          ),
          status: 'Default',
        },
        1: {
          text: (
            <FormattedMessage id="pages.searchTable.userStatus.active" defaultMessage="Active" />
          ),
          status: 'Processing',
        },
        2: {
          text: (
            <FormattedMessage id="pages.searchTable.userStatus.frozen" defaultMessage="Frozen" />
          ),
          status: 'Success',
        },
        3: {
          text: (
            <FormattedMessage id="pages.searchTable.userStatus.deleted" defaultMessage="Deleted"
            />
          ),
          status: 'Error',
        },
      },
    },
    {
      title: <FormattedMessage id="pages.searchTable.updateForm.userCreateTime.nameLabel" defaultMessage="创建时间" />,
      dataIndex: 'createTime',
      //search: false,
      hideInSearch: true,
      valueType: 'dateTime',
    },
    {
      title: <FormattedMessage id="pages.searchTable.titleOption" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      render: (_, record) => [
        <Button key="grant"
                type="link"
                size="small"
                onClick={() => {
                  //handleUpdateModalVisible(true);
                  setCurrentRow(record);
                  setShowGrant(true)
                }}>
          <FormattedMessage id="pages.searchTable.grant" defaultMessage="Grant" />
        </Button>,
        <a key="updateRow"
           onClick={() => {
             handleUpdateModalVisible(true);
             setCurrentRow(record);
           }}>
          <FormattedMessage
            id="pages.searchTable.updateForm"
            defaultMessage="修改"
          />
        </a>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.UserListItem, API.PageParams>
        headerTitle={intl.formatMessage({
          id: 'pages.searchTable.title',
          defaultMessage: 'Enquiry Form',
        })}
        options={false}//不显示新疆按钮旁边的表格控制选项
        actionRef={actionRef}
        rowKey={record => record.id + ''}
        search={{
          labelWidth: 120,
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleCreateModalVisible(true);
            }}
          >
            <PlusOutlined /> <FormattedMessage id="pages.searchTable.new" defaultMessage="New" />
          </Button>,
        ]}
        columns={columns}
        request={user}
        pagination={{
          //默认每页显示记录数
          pageSize: 10,
          //是否显示每页显示记录数控制器
          showSizeChanger: true,
          onChange: (page) => console.log(page),
        }}
        rowSelection={{
          onChange: (_, selectedRows) => {
            //console.log('selectedRows:', selectedRows.length)
            setSelectedRows(selectedRows);
          },
        }}
      />
      {/*底部批量删除弹窗*/}
      {selectedRowsState?.length > 0 && (
        <FooterToolbar
          extra={
            <div>
              <FormattedMessage id="pages.searchTable.chosen" defaultMessage="Chosen" />{' '}
              <a style={{ fontWeight: 600 }}>{selectedRowsState.length}</a>{' '}
              <FormattedMessage id="pages.searchTable.item" defaultMessage="项" />
              &nbsp;&nbsp;
              {/*<span>*/}
                {/*<FormattedMessage*/}
                {/*  id="pages.searchTable.updateForm.userName.nameLabel"*/}
                {/*  defaultMessage="用户名称"*/}
                {/*/>{' '}*/}
                {/*{selectedRowsState.reduce((pre, item) => pre + item.id!, 0)}{' '}*/}
                {/*<FormattedMessage id="pages.searchTable.tenThousand" defaultMessage="万" />*/}
              {/*</span>*/}
            </div>
          }
        >
          <Button
            onClick={async () => {
              showRemoveConfirm()
            }}
          >
            <FormattedMessage
              id="pages.searchTable.batchDeletion"
              defaultMessage="批量删除"
            />
          </Button>
          <Button type="primary">
            <FormattedMessage
              id="pages.searchTable.batchGrant"
              defaultMessage="批量授权"
            />
          </Button>
        </FooterToolbar>
      )}
      {/*新增用户模态窗*/}
      <ModalForm
        title={intl.formatMessage({
          id: 'pages.searchTable.createForm.user',
          defaultMessage: '新建用户',
        })}
        // width="1000px"
        autoFocusFirstInput
        visible={createModalVisible}
        onVisibleChange={handleCreateModalVisible}
        onFinish={async (value) => {
          const success = await handleAdd(value as API.UserListItem);
          if (success) {
            handleCreateModalVisible(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        modalProps={{ destroyOnClose: true}}
      >
        <ProForm.Group>
          {/*用户名*/}
          <ProFormText
            width="md"
            name="name"
            label="用户名称"
            tooltip="最长为 24 位"
            placeholder="请输入用户名称"
            rules={[
              {
                required: true,
                message: (
                  <FormattedMessage
                    id="pages.searchTable.createForm.notNone"
                    defaultMessage="必填项，不能为空"
                  />
                ),
              },
            ]}
          />
          {/*部门：这里以后要做成动态读取部门表*/}
          <ProFormSelect
            request={async () => [
              {
                value: '24',
                label: '总公司',
              },
              {
                value: '25',
                label: '开发部',
              },
              {
                value: '26',
                label: '运营部',
              },
              {
                value: '27',
                label: '战略部',
              },
              {
                value: '28',
                label: '供应商客户',
              },
            ]}
            width="md"
            name="deptId"
            label="部门"
          />
        </ProForm.Group>
        <ProForm.Group>
          {/*账户*/}
          <ProFormText
            width="md"
            name="account"
            label="账户"
            tooltip="最长为 24 位"
            placeholder="请输入英文账号"
            rules={[
              {
                required: true,
                message: (
                  <FormattedMessage
                    id="pages.searchTable.createForm.notNone"
                    defaultMessage="必填项，不能为空"
                  />
                ),
              },
            ]}
          />
          {/*密码*/}
          <ProFormText.Password
            width="md"
            name="password"
            label="密码"
            tooltip="最长为 24 位"
            placeholder="缺省密码为1~6"
            rules={[
              // {
              //   required: true,
              //   message: (
              //     <FormattedMessage
              //       id="pages.searchTable.createForm.notNone"
              //       defaultMessage="必填项，不能为空"
              //     />
              //   ),
              // },
            ]}
          />
        </ProForm.Group>
        <ProForm.Group>
          {/*性别*/}
          <ProFormSelect
            request={async () => [
              {
                value: '1',
                label: '男性',
              },
              {
                value: '2',
                label: '女性',
              },
            ]}
            width="md"
            name="sex"
            label="性别"
          />
          {/*生日*/}
          <ProFormDatePicker
            width="md"
            name="birthday"
            placeholder="请选择生日"
            label="出生日期" />
        </ProForm.Group>
        <ProForm.Group>
          {/*联系电话*/}
          <ProFormText width="md" name="phone" label="联系电话" tooltip="最长为 11 位" placeholder="请输入电话"/>
          {/*电子邮箱*/}
          <ProFormText width="md" name="email" label="电子邮箱" tooltip="最长为 20 位" placeholder="请输入邮箱"/>
        </ProForm.Group>
      </ModalForm>
      {/*修改用户信息模态窗*/}
      <ModalForm
        title={intl.formatMessage({
          id: 'pages.searchTable.updateForm.user',
          defaultMessage: '新建用户',
        })}
        // width="1000px"
        autoFocusFirstInput
        visible={updateModalVisible}
        onVisibleChange={handleUpdateModalVisible}
        onFinish={async (value) => {
          const success = await handleUpdate(value as API.UserListItem);
          if (success) {
            handleUpdateModalVisible(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        modalProps={{ destroyOnClose: true}}
      >
        <ProForm.Group>
          {/*ID*/}
          <ProFormText
            name="id"
            hidden={true}
            initialValue={currentRow?.id}
            />
          {/*用户名*/}
          <ProFormText
            width="md"
            name="name"
            label="用户名称"
            tooltip="最长为 24 位"
            placeholder="请输入用户名称"
            initialValue={currentRow?.name || ''}
            rules={[
              {
                required: true,
                message: (
                  <FormattedMessage
                    id="pages.searchTable.createForm.notNone"
                    defaultMessage="必填项，不能为空"
                  />
                ),
              },
            ]}
          />
          {/*部门：这里以后要做成动态读取部门表*/}
          <ProFormSelect
            request={async () => [
              {
                value: '24',
                label: '总公司',
              },
              {
                value: '25',
                label: '开发部',
              },
              {
                value: '26',
                label: '运营部',
              },
              {
                value: '27',
                label: '战略部',
              },
              {
                value: '28',
                label: '供应商客户',
              },
            ]}
            width="md"
            name="deptId"
            label="部门"
            initialValue={currentRow?.deptId + '' || ''}
          />
        </ProForm.Group>
        <ProForm.Group>
          {/*账户*/}
          <ProFormText
            width="md"
            name="account"
            disabled={true}
            label="账户"
            tooltip="账户不可修改"
            placeholder="请输入英文账号"
            initialValue={currentRow?.account || ''}
            rules={[
              {
                required: true,
                message: (
                  <FormattedMessage
                    id="pages.searchTable.createForm.notNone"
                    defaultMessage="必填项，不能为空"
                  />
                ),
              },
            ]}
          />
          {/*密码*/}
          <ProFormText.Password
            width="md"
            name="password"
            label="密码"
            tooltip="最长为 24 位"
            placeholder="缺省密码为1~6"
            //initialValue={currentRow?.password || ''}
            rules={[
              // {
              //   required: true,
              //   message: (
              //     <FormattedMessage
              //       id="pages.searchTable.createForm.notNone"
              //       defaultMessage="必填项，不能为空"
              //     />
              //   ),
              // },
            ]}
          />
        </ProForm.Group>
        <ProForm.Group>
          {/*性别*/}
          <ProFormSelect
            request={async (params) => {
              console.log(params)
              return [
                {
                  value: '1',
                  label: '男性',
                },
                {
                  value: '2',
                  label: '女性',
                },
              ]
            }}
            width="md"
            name="sex"
            label="性别"
            initialValue={currentRow?.sex + '' || ''}
          />
          {/*生日*/}
          <ProFormDatePicker
            width="md"
            name="birthday"
            placeholder="请选择生日"
            label="出生日期"
            initialValue={currentRow?.birthday || ''}
          />
        </ProForm.Group>
        <ProForm.Group>
          {/*联系电话*/}
          <ProFormText width="md" name="phone" label="联系电话" tooltip="最长为 11 位" placeholder="请输入电话"
                       initialValue={currentRow?.phone || ''}/>
          {/*电子邮箱*/}
          <ProFormText width="md" name="email" label="电子邮箱" tooltip="最长为 20 位" placeholder="请输入邮箱"
                       initialValue={currentRow?.email || ''}/>
        </ProForm.Group>
      </ModalForm>
      {/*<UpdateForm*/}
      {/*  onSubmit={async (value) => {*/}
      {/*    const success = await handleUpdate(value);*/}
      {/*    if (success) {*/}
      {/*      handleUpdateModalVisible(false);*/}
      {/*      setCurrentRow(undefined);*/}
      {/*      if (actionRef.current) {*/}
      {/*        actionRef.current.reload();*/}
      {/*      }*/}
      {/*    }*/}
      {/*  }}*/}
      {/*  onCancel={() => {*/}
      {/*    handleUpdateModalVisible(false);*/}
      {/*    if (!showDetail) {*/}
      {/*      setCurrentRow(undefined);*/}
      {/*    }*/}
      {/*  }}*/}
      {/*  updateModalVisible={updateModalVisible}*/}
      {/*  values={currentRow || {}}*/}
      {/*/>*/}
      {/*用户详情模态框*/}
      <DrawerForm
        width={600}
        visible={showDetail}
        onVisibleChange={setShowDetail}
        // onClose={() => {
        //   setCurrentRow(undefined);
        //   setShowDetail(false);
        // }}
        // closable={false}
      >
        {currentRow?.name && (
          <ProDescriptions<API.UserListItem>
            column={2}
            title={currentRow?.name}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.name,
            }}
            columns={columns as ProDescriptionsItemProps<API.UserListItem>[]}
          />
        )}
      </DrawerForm>
      {/*用户授权模态框*/}
      <DrawerForm
        width={window.innerWidth > 900 ? 800 : window.innerWidth - 100}
        visible={showGrant}
        onVisibleChange={setShowGrant}
        drawerProps={{ destroyOnClose: true}}
        onFinish={async () => {//onFinish只会在点击确认按钮时触发（点击取消按钮不会触发）
          //调用用户授权接口
          const res: API.RtnInfo = await userGrant({
            userId: currentRow?.id,
            roleIds: roleIds,
          })
          if (res.success == true) {
            message.success(res.message)
          } else {
            message.error(res.message)
          }
          return true;
        }}
      >
        {/*角色树表格*/}
        <RoleTreeTable
          userId={currentRow?.id}
          showFooterToolbar = {false}//是否显示底部的删除确认栏
          showMiddleToolBar = {false}//是否显示中部的工具栏（一般用来显示'新建'按钮）
          showSearchToolBar = {false}//是否显示搜索工具栏
          showTableOptions  = {false}//是否在表格中显示操作栏
          setRoleIds={setRoleIds}
          // onSelectRow={(id, name) => {
          // }}
        />
      </DrawerForm>
    </PageContainer>
  );
};

export default TableList;
