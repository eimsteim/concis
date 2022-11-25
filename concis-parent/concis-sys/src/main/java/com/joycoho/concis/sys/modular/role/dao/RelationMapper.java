/**
 * Copyright 2022-2025 Leo (https://github.com/eimsteim/concis)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joycoho.concis.sys.modular.role.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.joycoho.concis.sys.modular.menu.entity.Relation;

import java.util.List;

/**
 * <p>
 * 角色和菜单关联表 Mapper 接口
 * </p>
 *
 * @author Leo
 * @since 2017-07-11
 */
public interface RelationMapper extends BaseMapper<Relation> {

    List<Long> selectMenusByRoleId(int roleId);
}