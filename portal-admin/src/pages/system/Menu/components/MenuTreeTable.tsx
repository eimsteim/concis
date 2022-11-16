//定义切换时用到的id载体
//import React, {forwardRef, Key, useEffect, useImperativeHandle, useState} from "react";
import React, {Key, useEffect, useRef, useState} from "react";
import {
  ProColumns,
  ProTable,
  FooterToolbar,
  ActionType,
  ProForm,
  ProFormText,
  ProFormDigit,
  ModalForm,
  ProFormSelect,
} from "@ant-design/pro-components";
import {FormattedMessage, useIntl} from "umi";
import {message, Button, Modal} from "antd";
import {ExclamationCircleOutlined, PlusOutlined} from "@ant-design/icons";
import {addMenu, updateMenu, removeMenu, fetchAllMenuData, fetchMenuIdsByRoleId} from "@/services/system/menu/api";
import MenuFormSelect from "@/pages/system/Menu/components/MenuFormSelect";
const { confirm } = Modal;

/**
 * @en-US Add node
 * @zh-CN 添加节点
 * @param fields
 */
const handleAdd = async (fields: API.MenuNode) => {
  const hide = message.loading('正在新建菜单');
  try {
    await addMenu({ ...fields });
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
const handleUpdate = async (fields: API.MenuNode) => {
  const hide = message.loading('正在修改菜单');
  try {
    await updateMenu({ ...fields });
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
const handleRemove = async (selectedMenuIds: Key[]) => {
  const hide = message.loading('正在删除菜单');
  if (!selectedMenuIds) return true;
  try {
    await removeMenu(selectedMenuIds);
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


/** 扩展的外部属性 */
type ExtProps = {
  roleId?: number;
  title?: string;
  showFooterToolbar?: boolean;//是否显示底部的删除确认栏
  showMiddleToolBar?: boolean;//是否显示中部的工具栏（一般用来显示'新建'按钮）
  showSearchToolBar?: boolean;//是否显示搜索工具栏
  showTableOptions?: boolean;//是否在表格中显示操作栏
  setMenuIds?: (menuIds: Key[]) => void;
}

/**
 * 展示当前选中的角色对应的权限树
 * @constructor
 */
const MenuTreeTable: React.FC<ExtProps> = (props, ref) => {
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
  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */
  const intl = useIntl();
  //这是所有的菜单数据
  const [menuDataList, setMenuDataList] = useState<API.MenuNode[]>([])
  //这是选中的ID数组
  const [selectedMenuIds, setSelectedMenuIds] = useState<Key[]>([])
  const [currentRow, setCurrentRow] = useState<API.MenuNode>();
  const [showIconForm, setShowIconForm] = useState(true)

  const actionRef = useRef<ActionType>();

  // 查询选中的角色其父级包含的所有权限
  const loadDatasource = async () => {
    fetchAllMenuData()
      .then((menuData: any) => {
        if (menuData == null) {
          message.error("服务器异常")
        } else {
          if (menuData.success == true) {
            //设置
            setMenuDataList(menuData.data)
            // 查询选中的角色自己包含的所有权限ID（用于更新checkbox）
            if (props.roleId) {
              fetchMenuIdsByRoleId(props.roleId)
                .then((menuIds: any) => {
                  if (menuIds == null) {
                    message.error("服务器异常")
                  } else {
                    if (menuIds.success == true) {
                      //设置
                      setSelectedMenuIds([...menuIds.data])
                    } else {
                      message.error(menuIds.message)
                    }
                  }
                })
            }
          } else {
            message.error(menuData.message)
          }
        }
      })
  }


  /**
   * @en-US Show Remove Confirm
   * @zh-CN 在删除用户之前显示提示
   */
  const showRemoveConfirm = () => {
    confirm({
      title: '确认删除这 ' + selectedMenuIds.length + ' 项？',
      icon: <ExclamationCircleOutlined />,
      content: '删除当前菜单，会同步删除其所有子菜单',
      okText: '删除',
      okType: 'danger',
      cancelText: '取消',
      onOk: async () => {
        const success = await handleRemove(selectedMenuIds);
        if (success) {
          setSelectedMenuIds([]);
          loadDatasource()
        }
      },
      onCancel() {
      },
    })
  }

  //定义表头
  const columns: ProColumns<API.MenuNode>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      hideInTable: true,
      hideInSearch: true,
    },
    {
      title: '菜单名称',
      dataIndex: 'name',
    },
    {
      title: '菜单编码',
      dataIndex: 'code',
    },
    {
      title: '父级编码',
      dataIndex: 'parentId',
      hideInTable: true,
      hideInSearch: true,
    },
    {
      title: '菜单地址',
      dataIndex: 'path',
      hideInSearch: true,
    },
    {
      title: '是否菜单',
      dataIndex: 'ismenu',
      hideInSearch: true,
      valueEnum: {
        0: {
          text: (
            <FormattedMessage id="pages.common.enum.no" defaultMessage="否" />
          ),
          status: 'Default',
        },
        1: {
          text: (
            <FormattedMessage id="pages.common.enum.yes" defaultMessage="是" />
          ),
          status: 'Processing',
        },
      },
    },
    {
      title: <FormattedMessage id="pages.searchTable.titleOption" defaultMessage="Operating" />,
      dataIndex: 'option',
      valueType: 'option',
      hideInTable: !props.showTableOptions,
      render: (_, record) => [
        <a key="updateRow"
           onClick={() => {
             handleUpdateModalVisible(true);
             console.log(JSON.stringify(record))
             setCurrentRow(record);
           }}>
          <FormattedMessage
            id="pages.searchTable.updateForm"
            defaultMessage="修改"
          />
        </a>,
      ],
    },
  ]
  //定义副作用，当切换角色时，重载当前表格的内容
  useEffect(() => {
    loadDatasource()
  }, [props.roleId])


  //定义菜单权限表
  return (
    <>
    <ProTable<API.MenuNode>
      headerTitle={props.title}
      columns={columns}
      dataSource={menuDataList}
      pagination={false}
      actionRef={actionRef}
      rowKey={record => record.id + ''}
      options={false}
      search={props.showSearchToolBar && {
        labelWidth: 120,
      }}
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
        } else {
          return []
        }
      }}
      //这个是要check的
      rowSelection={{
        //alwaysShowAlert: true,//一直显示表格顶部的选中提示
        checkStrictly: false,//设置为false能让父子节点选中时相互关联
        onChange: (selectedRowKeys, selectedRows) => {
          setSelectedMenuIds([...selectedRowKeys])
          //同步通知父组件Role.DrawerForm
          if(props.setMenuIds) {
            props.setMenuIds([...selectedRowKeys])
          }
        },
        selectedRowKeys: selectedMenuIds,
        //defaultSelectedRowKeys: selectedMenuIds,//defaultSelectedRowKeys - 只能定义Table初始化时的状态
      }}
    />
    {/*底部批量删除弹窗*/}
    { (props.showFooterToolbar && selectedMenuIds?.length > 0) && (
      <FooterToolbar
        extra={
          <div>
            <FormattedMessage id="pages.searchTable.chosen" defaultMessage="Chosen" />{' '}
            <a style={{ fontWeight: 600 }}>{selectedMenuIds.length}</a>{' '}
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
        {/*<Button type="primary">*/}
        {/*  <FormattedMessage*/}
        {/*    id="pages.searchTable.batchGrant"*/}
        {/*    defaultMessage="批量授权"*/}
        {/*  />*/}
        {/*</Button>*/}
      </FooterToolbar>
    )}
    {/*新增菜单模态窗*/}
    <ModalForm
      title={intl.formatMessage({
        id: 'pages.searchTable.createForm.menu',
        defaultMessage: '新建菜单',
      })}
      autoFocusFirstInput
      visible={createModalVisible}
      onVisibleChange={handleCreateModalVisible}
      onFinish={async (value) => {
        const success = await handleAdd(value as API.MenuNode);
        if (success) {
          handleCreateModalVisible(false);
          //刷新表格
          loadDatasource()
          //TODO 这里要看下，为什么 actionRef.current=undefined
          // 在有datasource的情况下，使用actionRef刷新表格会失效
          // if (actionRef.current) {
          //   //actionRef.current.reload();
          //   actionRef.current?.reloadAndRest?.();
          // }
        }
      }}
      modalProps={{ destroyOnClose: true}}
    >
      <ProForm.Group>
        {/*菜单名*/}
        <ProFormText
          width="md"
          name="name"
          label="菜单名称"
          tooltip="最长为 24 位"
          placeholder="请输入菜单名称"
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
        {/*请求地址*/}
        <ProFormText
          width="md"
          name="path"
          label="请求地址"
          tooltip="最长为 200 字"
          placeholder="例如：/system/user" />
      </ProForm.Group>
      <ProForm.Group>
        {/*菜单编码*/}
        <ProFormText
          width="md"
          name="code"
          label="菜单编码"
          tooltip="最长为 24 位"
          placeholder="请输入菜单编码"
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
        {/*上级菜单*/}
        <MenuFormSelect name={"parentId"} label={"上级菜单"} width={"md"} />
      </ProForm.Group>
      <ProForm.Group>
        {/*是否菜单*/}
        <ProFormSelect
          name="ismenu"
          width="md"
          label="是否菜单"
          request={async () => [
            { label: '是', value: 1 },
            { label: '否', value: 0 },
          ]}
          initialValue={1}
          fieldProps={{
            defaultValue: 1,
            onSelect: (value) => {
              if(value === 0) {
                setShowIconForm(false)
              } else if(value === 1){
                setShowIconForm(true)
              }
            }
          }}
        />
        {/*排序*/}
        <ProFormDigit label="排序" placeholder="选择数字" name="num" width="md" min={1} max={100} initialValue={1} />
      </ProForm.Group>
      <ProForm.Group>
        {/*图标*/}
        <ProFormText
          hidden={!showIconForm}
          width="md"
          name="icon"
          label="图标"
          tooltip="最长为 24 字"
          placeholder="请选择图标" />
      </ProForm.Group>
    </ModalForm>
    {/*修改菜单模态窗*/}
    <ModalForm
      title={intl.formatMessage({
        id: 'pages.searchTable.createForm.menu',
        defaultMessage: '修改菜单',
      })}
      autoFocusFirstInput
      visible={updateModalVisible}
      onVisibleChange={handleUpdateModalVisible}
      onFinish={async (value) => {
        const success = await handleUpdate(value as API.MenuNode);
        if (success) {
          handleUpdateModalVisible(false);
          //刷新表格
          loadDatasource()
          //TODO 这里要看下，为什么 actionRef.current=undefined
          // 在有datasource的情况下，使用actionRef刷新表格会失效
          // if (actionRef.current) {
          //   //actionRef.current.reload();
          //   actionRef.current?.reloadAndRest?.();
          // }
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
        {/*菜单名*/}
        <ProFormText
          width="md"
          name="name"
          label="菜单名称"
          tooltip="最长为 24 位"
          placeholder="请输入菜单名称"
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
        {/*请求地址*/}
        <ProFormText
          width="md"
          name="path"
          label="请求地址"
          tooltip="最长为 200 字"
          placeholder="例如：/system/user"
          initialValue={currentRow?.path || ''}
        />
      </ProForm.Group>
      <ProForm.Group>
        {/*菜单编码*/}
        <ProFormText
          width="md"
          name="code"
          label="菜单编码"
          tooltip="最长为 24 位"
          placeholder="请输入菜单编码"
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
        {/*上级菜单*/}
        <MenuFormSelect name={"parentId"} label={"上级菜单"} width={"md"} initialValue={currentRow?.parentId} />
      </ProForm.Group>
      <ProForm.Group>
        {/*是否菜单*/}
        <ProFormSelect
          name="ismenu"
          width="md"
          label="是否菜单"
          request={async () => [
            { label: '是', value: 1 },
            { label: '否', value: 0 },
          ]}
          initialValue={currentRow?.ismenu || 1}
          fieldProps={{
            //defaultValue: 1,
            onSelect: (value) => {
              if(value === 0) {
                setShowIconForm(false)
              } else if(value === 1){
                setShowIconForm(true)
              }
            }
          }}
        />
        {/*排序*/}
        <ProFormDigit label="排序" placeholder="选择数字" name="num" width="md" min={1} max={100} initialValue={currentRow?.num || 1} />
      </ProForm.Group>
      <ProForm.Group>
        {/*图标*/}
        <ProFormText
          hidden={!showIconForm}
          width="md"
          name="icon"
          label="图标"
          tooltip="最长为 24 字"
          placeholder="请选择图标"
          initialValue={currentRow?.icon}
        />
      </ProForm.Group>
    </ModalForm>
    </>
  )
};

export default MenuTreeTable
