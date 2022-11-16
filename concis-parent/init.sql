-- -------------------------------------------------------------
-- TablePlus 5.1.0(468)
--
-- https://tableplus.com/
--
-- Database: concis
-- Generation Time: 2022-11-13 23:36:23.2010
-- -------------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `code` varchar(255) DEFAULT NULL COMMENT '菜单编号',
  `pcode` varchar(255) DEFAULT NULL COMMENT '菜单父编号',
  `pcodes` varchar(255) DEFAULT NULL COMMENT '当前菜单的所有父菜单编号',
  `name` varchar(255) DEFAULT NULL COMMENT '菜单名称',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `path` varchar(255) DEFAULT NULL COMMENT 'url地址',
  `num` int(65) DEFAULT NULL COMMENT '菜单排序号',
  `levels` int(65) DEFAULT NULL COMMENT '菜单层级',
  `ismenu` int(11) DEFAULT NULL COMMENT '是否是菜单（1：是  0：不是）',
  `tips` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` int(65) DEFAULT NULL COMMENT '菜单状态 :  1:启用   0:不启用',
  `isopen` int(11) DEFAULT NULL COMMENT '是否打开:    1:打开   0:不打开',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8 COMMENT='菜单表';

DROP TABLE IF EXISTS `sys_relation`;
CREATE TABLE `sys_relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `menu_id` bigint(11) DEFAULT NULL COMMENT '菜单id',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8 COMMENT='角色和菜单关联表';

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `sn` int(11) DEFAULT '0' COMMENT '序号',
  `pid` int(11) DEFAULT NULL COMMENT '父角色id',
  `name` varchar(255) DEFAULT NULL COMMENT '角色名称',
  `dept_id` int(11) DEFAULT '0' COMMENT '部门名称',
  `code` varchar(255) DEFAULT NULL COMMENT '提示',
  `version` int(11) DEFAULT NULL COMMENT '保留字段(暂时没用）',
  `levels` int(11) DEFAULT '1' COMMENT '级别',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='角色表';

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `account` varchar(45) DEFAULT NULL COMMENT '账号',
  `password` varchar(45) DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) DEFAULT NULL COMMENT 'md5密码盐',
  `name` varchar(45) DEFAULT NULL COMMENT '名字',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `sex` int(11) DEFAULT NULL COMMENT '性别（1：男 2：女）',
  `email` varchar(45) DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) DEFAULT NULL COMMENT '电话',
  `role_id` varchar(255) DEFAULT NULL COMMENT '角色id',
  `dept_id` int(11) DEFAULT NULL COMMENT '部门id',
  `status` int(11) DEFAULT NULL COMMENT '状态(1：启用  2：冻结  3：删除）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `version` int(11) DEFAULT NULL COMMENT '保留字段',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='管理员表';

INSERT INTO `sys_menu` (`id`, `code`, `pcode`, `pcodes`, `name`, `icon`, `path`, `num`, `levels`, `ismenu`, `tips`, `status`, `isopen`) VALUES
(1, 'system', '0', '[0],', '系统管理', 'Setting', '/system', 4, 1, 1, NULL, 1, 1),
(2, 'user', 'system', '[0],[system],', '用户管理', '', '/system/user', 1, 2, 1, NULL, 1, 0),
(3, 'user_list', 'user', '[0],[system],[user],', '用户列表', '', '/user/list', 10, 3, 0, NULL, 1, NULL),
(4, 'user_add', 'user', '[0],[system],[user],', '添加用户', NULL, '/user/add', 1, 3, 0, NULL, 1, 0),
(5, 'user_edit', 'user', '[0],[system],[user],', '修改用户', NULL, '/user/edit', 2, 3, 0, NULL, 1, 0),
(6, 'user_delete', 'user', '[0],[system],[user],', '删除用户', NULL, '/user/delete', 3, 3, 0, NULL, 1, 0),
(7, 'user_reset', 'user', '[0],[system],[user],', '重置密码', NULL, '/user/reset', 4, 3, 0, NULL, 1, 0),
(8, 'user_freeze', 'user', '[0],[system],[user],', '冻结用户', NULL, '/user/freeze', 5, 3, 0, NULL, 1, 0),
(9, 'user_unfreeze', 'user', '[0],[system],[user],', '解除冻结用户', NULL, '/user/unfreeze', 6, 3, 0, NULL, 1, 0),
(10, 'user_setRole', 'user', '[0],[system],[user],', '分配角色', NULL, '/user/setRole', 7, 3, 0, NULL, 1, 0),
(11, 'role', 'system', '[0],[system],', '角色管理', NULL, '/system/role', 2, 2, 1, NULL, 1, 0),
(12, 'role_list', 'role', '[0],[system],[role],', '角色列表', '', '/role/list', 7, 3, 0, NULL, 1, NULL),
(13, 'role_add', 'role', '[0],[system],[role],', '添加角色', NULL, '/role/add', 1, 3, 0, NULL, 1, 0),
(14, 'role_edit', 'role', '[0],[system],[role],', '修改角色', NULL, '/role/edit', 2, 3, 0, NULL, 1, 0),
(15, 'role_remove', 'role', '[0],[system],[role],', '删除角色', NULL, '/role/remove', 3, 3, 0, NULL, 1, 0),
(16, 'role_setAuthority', 'role', '[0],[system],[role],', '配置权限', NULL, '/role/setAuthority', 4, 3, 0, NULL, 1, 0),
(17, 'menu', 'system', '[0],[system],', '菜单管理', NULL, '/system/menu', 4, 2, 1, NULL, 1, 0),
(18, 'menu_list', 'menu', '[0],[system],[menu],', '菜单列表', '', '/menu/list', 5, 3, 0, NULL, 1, NULL),
(19, 'menu_add', 'menu', '[0],[system],[menu],', '添加菜单', NULL, '/menu/add', 1, 3, 0, NULL, 1, 0),
(20, 'menu_edit', 'menu', '[0],[system],[menu],', '修改菜单', NULL, '/menu/edit', 2, 3, 0, NULL, 1, 0),
(21, 'menu_remove', 'menu', '[0],[system],[menu],', '删除菜单', NULL, '/menu/remove', 3, 3, 0, NULL, 1, 0),
(22, 'dept', 'system', '[0],[system],', '部门管理', NULL, '/dept', 3, 2, 1, NULL, 1, NULL),
(23, 'dept_list', 'dept', '[0],[system],[dept],', '部门列表', '', '/dept/list', 5, 3, 0, NULL, 1, NULL),
(24, 'dept_detail', 'dept', '[0],[system],[dept],', '部门详情', '', '/dept/detail', 6, 3, 0, NULL, 1, NULL),
(25, 'dept_add', 'dept', '[0],[system],[dept],', '添加部门', NULL, '/dept/add', 1, 3, 0, NULL, 1, NULL),
(26, 'dept_update', 'dept', '[0],[system],[dept],', '修改部门', NULL, '/dept/update', 1, 3, 0, NULL, 1, NULL),
(27, 'dept_delete', 'dept', '[0],[system],[dept],', '删除部门', NULL, '/dept/delete', 1, 3, 0, NULL, 1, NULL),
(28, 'notice', 'system', '[0],[system],', '通知管理', NULL, '/notice', 9, 2, 1, NULL, 1, NULL),
(29, 'notice_add', 'notice', '[0],[system],[notice],', '添加通知', NULL, '/notice/add', 1, 3, 0, NULL, 1, NULL),
(30, 'notice_update', 'notice', '[0],[system],[notice],', '修改通知', NULL, '/notice/update', 2, 3, 0, NULL, 1, NULL),
(31, 'notice_delete', 'notice', '[0],[system],[notice],', '删除通知', NULL, '/notice/delete', 3, 3, 0, NULL, 1, NULL),
(32, 'welcome', '0', '[0],', '首页', 'Crown', '/welcome', 1, 1, 1, NULL, 1, NULL),
(33, 'code', '0', '[0],', '代码生成', 'Code', '/code', 3, 1, 1, NULL, 1, NULL),
(34, 'product', '0', '[0],', '商品管理', 'Tag', '/product', 6, 1, 1, NULL, 1, NULL),
(35, 'product_list', 'product', '[0],[product],', '商品列表', '', '/product/list', 1, 2, 1, NULL, 1, NULL),
(36, 'product_add', 'product', '[0],[product],', '新增商品', '', '/product/add', 1, 2, 0, NULL, 1, NULL),
(37, 'product_update', 'product', '[0],[product],', '修改商品', '', '/product/update', 2, 2, 0, NULL, 1, NULL),
(38, 'product_delete', 'product', '[0],[product],', '删除商品', '', '/product/delete', 3, 2, 0, NULL, 1, NULL),
(39, 'category', 'product', '[0],[product],', '品类管理', '', '/category', 4, 2, 1, NULL, 1, NULL),
(40, 'category_add', 'category', '[0],[product],[category],', '新增品类', '', '/category/add', 1, 2, 0, NULL, 1, NULL),
(41, 'category_update', 'category', '[0],[product],[category],', '修改品类', '', '/category/update', 2, 2, 0, NULL, 1, NULL),
(42, 'category_delete', 'category', '[0],[product],[category],', '删除品类', '', '/category/delete', 3, 2, 0, NULL, 1, NULL),
(43, 'category_attrs', 'category', '[0],[product],[category],', '规格详情', '', '/category/attrs', 4, 2, 0, NULL, 1, NULL),
(44, 'order', '0', '[0],', '订单管理', 'ShoppingCart', '/order', 9, 1, 1, NULL, 1, NULL),
(45, 'order_list', 'order', '[0],[order],', '订单列表', '', '/order/list', 1, 2, 1, NULL, 1, NULL),
(46, 'attribute', 'product', '[0],[product],', '商品规格', '', '/attribute', 5, 2, 1, NULL, 1, NULL),
(47, 'attribute_add', 'attribute', '[0],[product],[attribute],', '添加规格', '', '/attribute/add', 1, 3, 0, NULL, 1, NULL),
(48, 'attribute_update', 'attribute', '[0],[product],[attribute],', '修改规格', '', '/attribute/update', 2, 3, 0, NULL, 1, NULL),
(49, 'attribute_delete', 'attribute', '[0],[product],[attribute],', '删除规格', '', '/attribute/delete', 3, 3, 0, NULL, 1, NULL);

INSERT INTO `sys_relation` (`id`, `menu_id`, `role_id`) VALUES
(1, 1, 1),
(2, 2, 1),
(3, 3, 1),
(4, 4, 1),
(5, 5, 1),
(6, 6, 1),
(7, 7, 1),
(8, 8, 1),
(9, 9, 1),
(10, 10, 1),
(11, 11, 1),
(12, 12, 1),
(13, 13, 1),
(14, 14, 1),
(15, 15, 1),
(16, 16, 1),
(17, 17, 1),
(18, 18, 1),
(19, 19, 1),
(20, 20, 1),
(21, 21, 1),
(22, 22, 1),
(23, 23, 1),
(24, 24, 1),
(25, 25, 1),
(26, 26, 1),
(27, 27, 1),
(28, 28, 1),
(29, 29, 1),
(30, 30, 1),
(31, 31, 1),
(32, 32, 1),
(33, 33, 1),
(34, 34, 1),
(35, 35, 1),
(36, 36, 1),
(37, 37, 1),
(38, 38, 1),
(39, 39, 1),
(40, 40, 1),
(41, 41, 1),
(42, 42, 1),
(43, 43, 1),
(44, 44, 1),
(45, 45, 1),
(46, 46, 1),
(47, 47, 1),
(48, 48, 1),
(49, 49, 1),
(50, 32, 2),
(51, 2, 2),
(52, 4, 2),
(53, 5, 2),
(54, 6, 2),
(55, 7, 2),
(56, 8, 2),
(57, 9, 2),
(58, 10, 2),
(59, 3, 2),
(60, 11, 2),
(61, 13, 2),
(62, 14, 2),
(63, 15, 2),
(64, 16, 2),
(65, 12, 2),
(66, 34, 2),
(67, 35, 2),
(68, 36, 2),
(69, 37, 2),
(70, 38, 2),
(71, 39, 2),
(72, 46, 2),
(73, 47, 2),
(74, 48, 2),
(75, 49, 2),
(76, 44, 2),
(77, 45, 2),
(78, 22, 2),
(79, 25, 2),
(80, 26, 2),
(81, 27, 2),
(82, 23, 2),
(83, 24, 2),
(84, 17, 2),
(85, 19, 2),
(86, 20, 2),
(87, 21, 2),
(88, 18, 2),
(89, 28, 2),
(90, 29, 2),
(91, 30, 2),
(92, 31, 2),
(93, 1, 2);

INSERT INTO `sys_role` (`id`, `sn`, `pid`, `name`, `dept_id`, `code`, `version`, `levels`, `remark`) VALUES
(1, 1, 0, '超级管理员', 24, 'root', 1, 1, NULL),
(2, 2, 0, '运维管理员', 0, 'admin', NULL, 1, NULL);

INSERT INTO `sys_user` (`id`, `avatar`, `account`, `password`, `salt`, `name`, `birthday`, `sex`, `email`, `phone`, `role_id`, `dept_id`, `status`, `create_time`, `version`) VALUES
(1, 'https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png', 'root', '41630dc14f494b0341b82fbc613a416e', 'j513r', '超级管理员', '2017-05-05 00:00:00', 2, 'sn93@qq.com', '18200000000', '1', 27, 1, '2016-01-29 08:49:53', 25),
(2, 'https://gw.alipayobjects.com/zos/antfincdn/XAosXuNZyF/BiazfanxmamNRoxxVxka.png', 'admin', '3704e139d9d0e4ebd8a8b8ed81969d58', '1zvdx', '运维管理员', NULL, 1, '', '', '2', NULL, 1, '2022-11-13 23:14:36', NULL);



/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;