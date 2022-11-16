import {addRole, updateRole, removeRole, fetchRoleData, roleGrant} from '@/services/system/role/api';
import { PlusOutlined, ExclamationCircleOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-components';
import {
  FooterToolbar,
  ModalForm,
  ProForm,
  ProFormText,
  ProTable,
  ProCard,
  ProFormDigit,
  DrawerForm,
} from '@ant-design/pro-components';
import { Button, Modal, message } from 'antd';
import React, {Key, useEffect, useRef, useState} from 'react';
import { FormattedMessage, useIntl } from 'umi';
import styles from '../TableList/index.less';
import RoleFormSelect from "@/pages/system/Role/components/RoleFormSelect";
import MenuTreeTable from "@/pages/system/Menu/components/MenuTreeTable";
import {fetchRoleIdsByUserId} from "@/services/system/role/api";

const { confirm } = Modal;

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: API.RoleListItem) => {
  const hide = message.loading('正在新建角色');
  try {
    await addRole({ ...fields });
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
const handleUpdate = async (fields: API.RoleListItem) => {
  const hide = message.loading('正在修改角色');
  try {
    await updateRole({ ...fields });
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
const handleRemove = async (selectedRows: API.RoleListItem[]) => {
  const hide = message.loading('正在删除角色');
  if (!selectedRows) return true;
  try {
    hide();
    const res: API.RtnInfo = await removeRole(selectedRows.map((row) => row.id));
    //如果需要在请求时除了selected还需要传递其他参数，可以用下面的方式
    // await removeRole({
    //   selected: selectedRows.map((row) => row.id),
    // });
    if (res.success == true) {
      message.success('操作成功');
    } else {
      message.error("操作失败：" + res.message);
    }
    return true;
  } catch (error) {
    hide();
    message.error('操作失败：' + error);
    return false;
  }
};

/** 扩展的外部属性 */
type ExtProps = {
  //roleId?: number;
  userId?: number;
  title?: string;
  showFooterToolbar?: boolean;//是否显示底部的删除确认栏
  showMiddleToolBar?: boolean;//是否显示中部的工具栏（一般用来显示'新建'按钮）
  showSearchToolBar?: boolean;//是否显示搜索工具栏
  showTableOptions?: boolean;//是否在表格中显示操作栏
  //setMenuIds?: (menuIds: Key[]) => void;
  setRoleIds?: (roleIds: Key[]) => void;
  onSelectRow: (id: number | undefined, name: string | undefined) => void;
}

// type RoleTreeTableProps = {
//   show: string | undefined;
//   onSelectRow: (id: number | undefined, name: string | undefined) => void;
// }

/**
 * 角色树，点击角色树的Row，会在右侧展示其对应的权限树
 * @constructor
 */
const RoleTreeTable: React.FC<ExtProps> = (props) => {

  //const { onSelectRow, currentRoleId } = props

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

  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.RoleListItem>();
  const [selectedRowsState, setSelectedRows] = useState<API.RoleListItem[]>([]);

  // 控制 Drawer 的显示和隐藏
  const [showMenuDrawer, setShowMenuDrawer] = useState(false);
  // setMenuIds 会被传递到子组件 MenuTreeTable，在子组件修改选中的权限时，会通过 setMenuIds 将修改结果同步到下面的 DrawerForm 组件
  const [menuIds, setMenuIds] = useState<number[]>([]);

  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */
  const intl = useIntl();
  //这是所有的角色数据
  const [roleDataList, setRoleDataList] = useState<API.MenuNode[]>([])
  //这是选中的ID数组
  const [selectedRoleIds, setSelectedRoleIds] = useState<Key[]>([])

  // 查询选中的角色其父级包含的所有权限
  const loadDatasource = async () => {
    fetchRoleData()
      .then((res1: any) => {
        if (res1 == null) {
          message.error("服务器异常")
        } else {
          if (res1.success == true) {
            //设置
            setRoleDataList(res1.data)
            // 查询选中的角色自己包含的所有权限ID（用于更新checkbox）
            if (props.userId) {
              fetchRoleIdsByUserId(props.userId)
                .then((res2: any) => {
                  if (res2 == null) {
                    message.error("服务器异常")
                  } else {
                    if (res2.success == true) {
                      //设置
                      setSelectedRoleIds([...res2.data])
                    } else {
                      message.error(res2.message)
                    }
                  }
                })
            }
          } else {
            message.error(res1.message)
          }
        }
      })
  }


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
        handleRemove(selectedRowsState);
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
      hideInSearch: true,
    },
    {
      title: <FormattedMessage id="pages.searchTable.createForm.roleName" defaultMessage="角色名称" />,
      dataIndex: 'name',
      render: (dom, entity) => {
        return (
          <span>{dom}</span>
        );
      },
    },
    {
      title: <FormattedMessage id="pages.searchTable.createForm.roleCode" defaultMessage="角色编码" />,
      dataIndex: 'code',
    },
    {
      title: <FormattedMessage id="pages.searchTable.titleOption" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      hideInTable: !props.showTableOptions,
      render: (_, record) => [
        <Button key="grant"
                type="link"
                size="small"
                onClick={() => {
                  setCurrentRow(record);
                  setShowMenuDrawer(true)
                  //onSelectRow(record.id, record.name);
                }}>
          <FormattedMessage id="pages.searchTable.grant" defaultMessage="Grant" />
        </Button>,
        <a key="updateRow"
           onClick={(e) => {
             handleUpdateModalVisible(true);
             setCurrentRow(record);
             //合成事件（在jsx中绑定的事件）需要用下面的方式阻止冒泡
             //e.stopPropagation();
           }}>
          <FormattedMessage
            id="pages.searchTable.updateForm"
            defaultMessage="修改"
          />
        </a>,
      ],
    },
  ];

  //定义副作用，当切换角色时，重载当前表格的内容
  useEffect(() => {
    loadDatasource()
  }, [props.userId])

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
          return record.id === currentRow?.id ? styles['split-row-select-active'] : '';
        }}
        search={props.showSearchToolBar && {
          labelWidth: 120,
        }}//不显示搜索栏
        options={false}
        toolBarRender={() => {
          if (props.showMiddleToolBar) {
            return [
              <Button
                type="primary"
                key="primary"
                onClick={() => {
                  handleCreateModalVisible(true);
                }}
              >
                <PlusOutlined /> <FormattedMessage id="pages.searchTable.new" defaultMessage="New" />
              </Button>,
            ]
          }else {
            return []
          }
        }}
        columns={columns}
        //request={fetchRoleData}
        dataSource={roleDataList}
        pagination={false}
        //这个是要check的
        rowSelection={{
          checkStrictly: false,//设置为false能让父子节点选中时相互关联
          onChange: (selectedRowKeys, selectedRows) => {
            setSelectedRoleIds(selectedRowKeys)
            setSelectedRows(selectedRows);
            //同步通知父组件Role.DrawerForm
            if(props.setRoleIds) {
              props.setRoleIds([...selectedRowKeys])
            }
          },
          selectedRowKeys: selectedRoleIds,
        }}
        //这个是不用check的，但是onRow事件会频繁触发，并且会与行内onClick事件冲突，需要通过e.stopPropagation()阻止事件冒泡
        // onRow={(record, index) => {
        //   console.log('onRow...')
        //   return {
        //     onClick: () => {
        //       console.log('onRow.onClick...')
        //       if (record.id) {
        //         onSelectRow(record.id, record.name);
        //         //setSelectIndex(index + 1)
        //       }
        //     },
        //   };
        // }}
      />
      {/*底部批量删除弹窗*/}
      { (props.showFooterToolbar && selectedRowsState?.length > 0) && (
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
          id: 'pages.searchTable.createForm.role',
          defaultMessage: '新建角色',
        })}
        autoFocusFirstInput
        visible={createModalVisible}
        onVisibleChange={handleCreateModalVisible}
        onFinish={async (value) => {
          const success = await handleAdd(value as API.RoleListItem);
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
        <ProForm.Group>
          {/*角色下拉框*/}
          <RoleFormSelect name={"pid"} label={"上级角色"} width={"md"} />
          {/*排序*/}
          <ProFormDigit label="排序" name="sn" width="md" min={1} max={100} />
          {/*<ProFormText*/}
          {/*  width="md"*/}
          {/*  name="remark"*/}
          {/*  label="备注"*/}
          {/*  tooltip="最长为 500 字"*/}
          {/*  placeholder="请输入备注" />*/}
        </ProForm.Group>
      </ModalForm>
      {/*修改角色信息模态窗*/}
      <ModalForm
        title={intl.formatMessage({
          id: 'pages.searchTable.updateForm.role',
          defaultMessage: '新建角色',
        })}
        // width="1000px"
        autoFocusFirstInput
        visible={updateModalVisible}
        onVisibleChange={handleUpdateModalVisible}
        onFinish={async (value) => {
          const success = await handleUpdate(value as API.RoleListItem);
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
          {/*角色名*/}
          <ProFormText
            width="md"
            name="name"
            label="角色名称"
            tooltip="最长为 24 位"
            placeholder="请输入角色名称"
            initialValue={currentRow?.name}
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
            initialValue={currentRow?.code}
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
        <ProForm.Group>
          {/*角色下拉框*/}
          <RoleFormSelect name={"pid"} label={"上级角色"} width={"md"} initialValue={currentRow?.parentId} />
          {/*排序*/}
          <ProFormDigit label="排序" name="sn" width="md" min={1} max={100} initialValue={currentRow?.sn} />
        </ProForm.Group>
      </ModalForm>
      {/*抽屉弹出框*/}
      <DrawerForm
        title={"【" + currentRow?.name + "】的菜单权限："}
        visible={showMenuDrawer}
        onVisibleChange={setShowMenuDrawer}
        drawerProps={{ destroyOnClose: true}}
        onFinish={async () => {
          const res: API.RtnInfo = await roleGrant({
            roleId: currentRow?.id,
            menuIds: menuIds,
          })
          if (res.success == true) {
            message.success(res.message)
          } else {
            message.error(res.message)
          }
          return true;
        }}
      >
        {/*资源树表格*/}
        <MenuTreeTable
          roleId={currentRow?.id}
          roleName={currentRow?.name}
          showFooterToolbar={false}
          showMiddleToolbar={false}
          showSearchToolBar={false}
          showTableOptions ={false}
          setMenuIds={setMenuIds} />
      </DrawerForm>
    </ProCard>
  );
};

export default RoleTreeTable
