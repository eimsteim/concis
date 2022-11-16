import { fetchAllMenuData } from '@/services/system/menu/api';
import {
  ProFormTreeSelect,
} from '@ant-design/pro-components';
import React from 'react';

// export type FormValueType = {
//   target?: string;
//   template?: string;
//   type?: string;
//   time?: string;
//   frequency?: string;
// } & Partial<API.RuleListItem>;

export type MenuFormSelectProps = {
  name: string;
  label: string;
  width: number | "sm" | "md" | "xl" | "xs" | "lg" | undefined;
  initialValue?: number | undefined;
};

const MenuFormSelect: React.FC<MenuFormSelectProps> = (props) => {
  //const intl = useIntl();
  const { name, label, width, initialValue } = props;

  return (
    <ProFormTreeSelect
      name={name}
      label={label}
      placeholder="选择菜单资源"
      allowClear
      width={width}
      secondary
      debounceTime={1}//请求防抖
      request= {async () => {
        const res: API.RtnInfo = await fetchAllMenuData()
        if (res.success) {
          return res.data
        } else {
          return []
        }
        // const menuData: MenuDataItem[] = await fetchMenuData()
        // return menuData;
      }}
      initialValue={initialValue? initialValue : undefined}//新建菜单时，initialValue=undefined
      fieldProps={{
        showArrow: false,
        filterTreeNode: true,
        treeNodeFilterProp: 'name',
        showSearch: true,
        dropdownMatchSelectWidth: false,
        // labelInValue: true,
        autoClearSearchValue: true,
        multiple: false,//是否多选
        fieldNames: {
          label: 'name',
          value: 'id',
        }
      }}
    />
  )
}

export default MenuFormSelect;
