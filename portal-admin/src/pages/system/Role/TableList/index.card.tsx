import { fetchRoleData } from '@/services/system/role/api';
import { PlusOutlined, ExclamationCircleOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import {
  FooterToolbar,
  ModalForm,
  ProForm,
  PageContainer,
  ProFormText,
  ProTable,
  ProCard,
} from '@ant-design/pro-components';
import { Button, Modal } from 'antd';
import React, {useEffect, useRef, useState} from 'react';
import { FormattedMessage, useIntl } from 'umi';
import {MenuDataItem} from "@umijs/route-utils";
import { fetchMenuDataByRoleId } from "@/services/system/menu/api";
import styles from './index.less';

const { confirm } = Modal;

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
 *  Delete node
 * @zh-CN `删除`节点
 *
 * @param selectedRows
 */
// const handleRemove = async (selectedRows: API.RoleListItem[]) => {
//   const hide = message.loading('正在删除角色');
//   if (!selectedRows) return true;
//   try {
//     await removeUser(selectedRows.map((row) => row.id));
//     //如果需要在请求时除了selected还需要传递其他参数，可以用下面的方式
//     // await removeUser({
//     //   selected: selectedRows.map((row) => row.id),
//     // });
//     hide();
//     message.success('操作成功');
//     return true;
//   } catch (error) {
//     hide();
//     message.error('操作失败');
//     return false;
//   }
// };

type RoleTreeTableProps = {
  currentRoleId: number;
  onSelectRow: (roleId: number, roleName: string) => void;
}

/**
 * 角色树，点击角色树的Row，会在右侧展示其对应的权限树
 * @constructor
 */
const RoleTreeTable: React.FC<RoleTreeTableProps> = (props) => {

  const { onSelectRow, currentRoleId } = props

  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalVisible, handleUpdateModalVisible] = useState<boolean>(false);

  const [showDetail, setShowDetail] = useState<boolean>(false);

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.RoleListItem>();
  const [selectedRowsState, setSelectedRows] = useState<API.RoleListItem[]>([]);

  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */
  const intl = useIntl();

  /**
   * @en-US Show Remove Confirm
   * @zh-CN 在删除角色之前显示提示
   */
  const showRemoveConfirm = () => {
    confirm({
      title: '确认删除这 ' + selectedRowsState.length + ' 项？',
      icon: <ExclamationCircleOutlined />,
      content: '已删除的角色将不会显示在角色列表中',
      okText: '删除',
      okType: 'danger',
      cancelText: '取消',
      onOk() {
        //handleRemove(selectedRowsState);
        setSelectedRows([]);
        actionRef.current?.reloadAndRest?.();
      },
      onCancel() {
      },
    });
  };

  const columns: ProColumns<API.RoleListItem>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      hideInTable: true,
    },
    {
      title: <FormattedMessage id="pages.searchTable.createForm.roleName" defaultMessage="角色名称" />,
      dataIndex: 'name',
    },
    {
      title: <FormattedMessage id="pages.searchTable.createForm.roleCode" defaultMessage="角色编码" />,
      dataIndex: 'code',
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
                  handleUpdateModalVisible(true);
                  setCurrentRow(record);
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
    <ProCard ghost>
      <ProTable<API.RoleListItem, API.PageParams>
        headerTitle={intl.formatMessage({
          id: 'pages.searchTable.createForm.role.treeTable',
          defaultMessage: '角色树',
        })}
        actionRef={actionRef}
        rowKey={record => record.id + ''}
        rowClassName={(record) => {
          console.log("currentRoleId==>", currentRoleId)
          return record.id === currentRoleId ? styles['split-row-select-active'] : '';
        }}
        search={false}//不显示搜索栏
        options={false}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalVisible(true);
            }}
          >
            <PlusOutlined /> <FormattedMessage id="pages.searchTable.new" defaultMessage="New" />
          </Button>,
        ]}
        columns={columns}
        request={fetchRoleData}
        pagination={false}
        //这个是要check的
        rowSelection={{
          checkStrictly: false,//设置为false能让父子节点选中时相互关联
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
        //这个是不用check的
        onRow={(record, index) => {
          console.log('onRow...')
          return {
            onClick: () => {
              if (record.id) {
                console.log('onClick...record.id =1=2>', record.name)
                onSelectRow(record.id, record.name);
                //setSelectIndex(index + 1)
              }
            },
          };
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
        </FooterToolbar>
      )}
      {/*新增角色模态窗*/}
      <ModalForm
        title={intl.formatMessage({
          id: 'pages.searchTable.createForm.user',
          defaultMessage: '新建角色',
        })}
        // width="1000px"
        autoFocusFirstInput
        visible={createModalVisible}
        onVisibleChange={handleModalVisible}
        onFinish={async (value) => {
          // const success = await handleAdd(value as API.RoleListItem);
          // if (success) {
          //   handleModalVisible(false);
          //   if (actionRef.current) {
          //     actionRef.current.reload();
          //   }
          // }
        }}
        modalProps={{ destroyOnClose: true}}
      >
        <ProForm.Group>
          {/*角色名*/}
          <ProFormText
            width="md"
            name="name"
            label="角色名称"
            tooltip="最长为 24 位"
            placeholder="请输入角色名称"
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
          {/*角色编码*/}
          <ProFormText
            width="md"
            name="code"
            label="角色编码"
            tooltip="最长为 24 位"
            placeholder="请输入角色编码"
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
        </ProForm.Group>
      </ModalForm>
      {/*修改角色信息模态窗*/}
      <ModalForm
        title={intl.formatMessage({
          id: 'pages.searchTable.updateForm.user',
          defaultMessage: '新建角色',
        })}
        // width="1000px"
        autoFocusFirstInput
        visible={updateModalVisible}
        onVisibleChange={handleUpdateModalVisible}
        onFinish={async (value) => {
          // const success = await handleUpdate(value as API.RoleListItem);
          // if (success) {
          //   handleUpdateModalVisible(false);
          //   if (actionRef.current) {
          //     actionRef.current.reload();
          //   }
          // }
        }}
        modalProps={{ destroyOnClose: true}}
      >
        {/*ID*/}
        <ProForm.Group>
          <ProFormText
            name="id"
            hidden={true}
            initialValue={currentRow?.id}
            />
          {/*角色名*/}
          <ProFormText
            width="md"
            name="name"
            label="角色名称"
            tooltip="最长为 24 位"
            placeholder="请输入角色名称"
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
          {/*角色编码*/}
          <ProFormText
            width="md"
            name="code"
            disabled={true}
            label="角色编码"
            tooltip="编码不可修改"
            placeholder="请输入角色编码"
            initialValue={currentRow?.code || ''}
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
        </ProForm.Group>
      </ModalForm>
    </ProCard>
  );
};

//定义切换时用到的id载体
type RoleInfoProps = {
  roleId: number;
  roleName: string;
}

/**
 * 展示当前选中的角色对应的权限树
 * @constructor
 */
const RoleMenuTreeTable: React.FC<RoleInfoProps> = (props) => {
  const [menuDataList, setMenuDataList] = useState<MenuDataItem[]>([])
  //定义表头
  const columns: ProColumns<MenuDataItem>[] = [
    {
      title: '资源名称',
      dataIndex: 'id',
      hideInTable: true,
    },
    {
      title: '资源名称',
      dataIndex: 'name',
    },
    {
      title: '资源地址',
      dataIndex: 'path',
    },
    {
      title: '是否菜单',
      dataIndex: 'ismenu',
    },
  ]
  //定义副作用，当切换角色时，重载当前表格的内容
  useEffect(() => {
    // 从服务器查询对应角色的资源权限
    const result: any = fetchMenuDataByRoleId(props.roleId)
    result.then((res) => {
      //设置
      setMenuDataList(res.data)
    })

  }, [props.roleId])

  //定义资源权限表
  return (
    <ProTable<MenuDataItem>
      headerTitle={props.roleName}
      columns={columns}
      dataSource={menuDataList}
      pagination={false}
      rowKey={record => record.id + ''}
      options={false}
      search={false}
      //这个是要check的
      rowSelection={{
        checkStrictly: false,//设置为false能让父子节点选中时相互关联
        // onChange: (_, selectedRows) => {
        //   setSelectedRows(selectedRows);
        // },
      }}
    />
  );
}

/**
 * 最终合体效果
 * @constructor
 */
const TableList: React.FC = () => {
  const [roleId, setRoleId] = useState(1);
  const [roleName, setRoleName] = useState("超级管理员");
  return (
    <PageContainer>
      <ProCard ghost split="vertical" gutter={8} style={{ marginBlockStart: 8 }}>
        {/*角色树表格*/}
        <RoleTreeTable currentRoleId={roleId} onSelectRow={(id, name) => {
          setRoleId(id)
          setRoleName(name)
        }} />
        {/*资源树表格*/}
        <ProCard ghost>
          <RoleMenuTreeTable roleId={roleId} roleName={roleName} />
        </ProCard>
      </ProCard>
    </PageContainer>
  );
}

export default TableList;
