import { fetchRoleData } from '@/services/system/role/api';
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

export type RoleFormSelectProps = {
  name: string;
  label: string;
  width: number | "sm" | "md" | "xl" | "xs" | "lg" | undefined;
  initialValue?: number | undefined;
};

const RoleFormSelect: React.FC<RoleFormSelectProps> = (props) => {
  //const intl = useIntl();
  const { name, label, width, initialValue } = props;

  return (
    <ProFormTreeSelect
      name={name}
      label={label}
      placeholder="选择角色"
      allowClear
      width={width}
      secondary
      debounceTime={1}//请求防抖
      request= {async () => {
        const res: API.RtnInfo = await fetchRoleData()
        if (res.code == 200) {
          return res.data
        }
        return []
      }}
      initialValue={initialValue? initialValue : undefined}//新建角色时，initialValue=undefined
      fieldProps={{
        showArrow: false,
        filterTreeNode: true,
        treeNodeFilterProp: 'name',
        showSearch: true,
        dropdownMatchSelectWidth: false,
        labelInValue: false,//不是取value，而是取entry，形如：{"label": "供应商平台","value": 6}
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

export default RoleFormSelect;
