-- phpMyAdmin SQL Dump
-- version 2.10.0.2
-- http://www.phpmyadmin.net
-- 
-- 主机: localhost
-- 生成日期: 2010 年 05 月 20 日 22:46
-- 服务器版本: 5.1.37
-- PHP 版本: 5.2.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

-- 
-- 数据库: `dshop`
-- 

-- --------------------------------------------------------

-- 
-- 表的结构 `dcms_doc_content`
-- 

CREATE TABLE `dcms_doc_content` (
  `content` mediumblob,
  `doc_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`doc_id`),
  UNIQUE KEY `doc_id` (`doc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- 导出表中的数据 `dcms_doc_content`
-- 


-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_category`
-- 

CREATE TABLE `dshop_category` (
  `active` tinyint(1) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL,
  `cat_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `image` varchar(128) DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`cat_id`),
  UNIQUE KEY `cat_id` (`cat_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=57 ;

-- 
-- 导出表中的数据 `dshop_category`
-- 

INSERT INTO `dshop_category` (`active`, `update_time`, `add_time`, `parent_id`, `cat_id`, `image`, `display_order`) VALUES 
(1, '2010-05-06 09:48:52', '2010-02-12 17:13:06', -1, 0, '["/nopic.gif"]', 0),
(1, '2010-05-06 13:24:29', '2010-03-01 11:26:50', 0, 1, '["/root/manufacturer/106ijenf6qmro1271916775755"]', 2),
(1, '2010-05-06 14:26:58', '2010-03-19 15:12:58', 0, 3, '["/root/manufacturer/106ijenf6qmro1271916775755"]', 0),
(1, '2010-05-06 14:33:39', '2010-04-27 16:49:54', 0, 14, '["/nopic.gif"]', 1),
(1, '2010-05-05 16:25:03', '2010-04-28 16:14:01', 3, 27, '["/nopic.gif"]', 0),
(1, '2010-05-06 15:48:23', '2010-04-29 16:56:24', 3, 56, '["/nopic.gif"]', 1);

-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_category_name_description`
-- 

CREATE TABLE `dshop_category_name_description` (
  `name` varchar(128) DEFAULT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `lang_id` bigint(20) unsigned DEFAULT NULL,
  `category_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `dshop_category_name_description_lang_id` (`lang_id`),
  KEY `dshop_category_name_description_category_id` (`category_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=83 ;

-- 
-- 导出表中的数据 `dshop_category_name_description`
-- 

INSERT INTO `dshop_category_name_description` (`name`, `id`, `description`, `lang_id`, `category_id`) VALUES 
('种类1', 1, '测试种类 第一项', 22, 1),
('根目录', 3, '根目录描述', 22, 0),
('cat1', 6, 'cat1 cat1', 23, 1),
('fr cat1', 7, 'jpjpjp', 24, 1),
('种类2', 8, '种类2222222222', 22, 3),
('cat 2', 9, 'cat2222222222', 23, 3),
('jp cat 2', 10, 'fucking jp!!!!!!!', 24, 3),
('种类3', 40, '', 22, 14),
('c33', 53, '', 22, 27),
('c34', 82, '', 22, 56);

-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_language`
-- 

CREATE TABLE `dshop_language` (
  `name` varchar(50) DEFAULT NULL,
  `directory` varchar(128) DEFAULT NULL,
  `image` varchar(128) DEFAULT NULL,
  `code` varchar(2) DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  `lang_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`lang_id`),
  UNIQUE KEY `lang_id` (`lang_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=25 ;

-- 
-- 导出表中的数据 `dshop_language`
-- 

INSERT INTO `dshop_language` (`name`, `directory`, `image`, `code`, `display_order`, `lang_id`) VALUES 
('中国', 'chinese', 'cn', 'cn', 0, 22),
('America', 'america', 'us', 'us', 2, 23),
('France', 'france', 'fr', 'fr', 3, 24);

-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_manufacture`
-- 

CREATE TABLE `dshop_manufacture` (
  `name` varchar(128) DEFAULT NULL,
  `url` varchar(256) DEFAULT NULL,
  `image` varchar(128) DEFAULT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `manufact_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`manufact_id`),
  UNIQUE KEY `manufact_id` (`manufact_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=8 ;

-- 
-- 导出表中的数据 `dshop_manufacture`
-- 

INSERT INTO `dshop_manufacture` (`name`, `url`, `image`, `add_time`, `update_time`, `manufact_id`) VALUES 
('m1', 'url1', '[]', '2010-04-07 11:41:08', '2010-05-19 13:25:11', 1),
('m2', 'm2', '["/root/manufacturer/106ijenf6qmro1271916832232"]', '2010-04-26 10:42:27', '2010-05-19 13:24:55', 5),
('m3', 'url', '["/root/manufacturer/106ijenf6qmro1271916775755"]', '2010-05-18 17:51:25', '2010-05-18 17:52:31', 6),
('M4', 'mmm', '["/root/manufacturer/1n1gcnqky02391272419500676"]', '2010-05-19 13:24:11', '2010-05-19 13:24:49', 7);

-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_option_group`
-- 

CREATE TABLE `dshop_option_group` (
  `display_order` int(11) DEFAULT NULL,
  `option_group_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`option_group_id`),
  UNIQUE KEY `option_group_id` (`option_group_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=67 ;

-- 
-- 导出表中的数据 `dshop_option_group`
-- 

INSERT INTO `dshop_option_group` (`display_order`, `option_group_id`) VALUES 
(21, 2),
(22, 3),
(23, 4),
(11, 7),
(12, 9),
(13, 10),
(14, 11),
(16, 17),
(27, 18),
(28, 19),
(29, 20),
(30, 21),
(2, 35),
(3, 37),
(1, 38),
(4, 48),
(5, 49),
(6, 50),
(7, 51),
(8, 52),
(9, 53),
(10, 54),
(19, 55),
(15, 58),
(0, 59),
(0, 60),
(0, 62),
(0, 63),
(0, 64),
(0, 65),
(0, 66);

-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_option_group_name`
-- 

CREATE TABLE `dshop_option_group_name` (
  `name` varchar(128) DEFAULT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `lang_id` bigint(20) unsigned DEFAULT NULL,
  `option_group_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `dshop_option_group_name_lang_id` (`lang_id`),
  KEY `dshop_option_group_name_option_group_id` (`option_group_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=182 ;

-- 
-- 导出表中的数据 `dshop_option_group_name`
-- 

INSERT INTO `dshop_option_group_name` (`name`, `id`, `lang_id`, `option_group_id`) VALUES 
('长度', 1, 22, 1),
('大小', 2, 22, 2),
('颜色', 3, 22, 3),
('内存', 4, 22, 4),
('版本', 5, 22, 5),
('型号', 6, 22, 6),
('测试选项组1', 7, 22, 7),
('测试选项组1', 8, 22, 8),
('Length', 9, 23, 1),
('Size', 10, 23, 2),
('Color', 11, 23, 3),
('Memory', 12, 23, 4),
('Color', 13, 24, 3),
('测试选项组1', 14, 23, 7),
('测试选项组1', 15, 24, 7),
('测试选项组2', 16, 22, 9),
('测试选项组2', 17, 23, 9),
('测试选项组2', 18, 24, 9),
('测试选项组3', 19, 22, 10),
('测试选项组3', 20, 23, 10),
('测试选项组3', 21, 24, 10),
('测试选项组4', 22, 22, 11),
('测试选项组4', 23, 23, 11),
('测试选项组4', 24, 24, 11),
('5', 25, 22, 12),
('5', 26, 23, 12),
('5', 27, 24, 12),
('6', 28, 22, 13),
('6', 29, 23, 13),
('6', 30, 24, 13),
('7', 31, 22, 14),
('7', 32, 23, 14),
('7', 33, 24, 14),
('8', 34, 22, 15),
('8', 35, 23, 15),
('8', 36, 24, 15),
('9', 37, 22, 16),
('9', 38, 23, 16),
('9', 39, 24, 16),
('11', 40, 22, 17),
('11', 41, 23, 17),
('11', 42, 24, 17),
('22', 43, 22, 18),
('22', 44, 23, 18),
('22', 45, 24, 18),
('33', 46, 22, 19),
('33', 47, 23, 19),
('33', 48, 24, 19),
('44', 49, 22, 20),
('44', 50, 23, 20),
('44', 51, 24, 20),
('55', 52, 22, 21),
('55', 53, 23, 21),
('55', 54, 24, 21),
('66', 55, 22, 22),
('66', 56, 23, 22),
('66', 57, 24, 22),
('77', 58, 22, 23),
('77', 59, 23, 23),
('77', 60, 24, 23),
('88', 61, 22, 24),
('88', 62, 23, 24),
('88', 63, 24, 24),
('88', 64, 22, 25),
('88', 65, 23, 25),
('88', 66, 24, 25),
('88', 67, 22, 26),
('88', 68, 23, 26),
('88', 69, 24, 26),
('88', 70, 22, 27),
('88', 71, 23, 27),
('88', 72, 24, 27),
('88', 73, 22, 28),
('88', 74, 23, 28),
('88', 75, 24, 28),
('88', 76, 22, 29),
('88', 77, 23, 29),
('88', 78, 24, 29),
('88', 79, 22, 30),
('88', 80, 23, 30),
('88', 81, 24, 30),
('88', 82, 22, 31),
('88', 83, 23, 31),
('88', 84, 24, 31),
('99', 85, 22, 32),
('99', 86, 23, 32),
('99', 87, 24, 32),
('99', 88, 22, 33),
('99', 89, 23, 33),
('99', 90, 24, 33),
('99', 91, 22, 34),
('99', 92, 23, 34),
('99', 93, 24, 34),
('111', 94, 22, 35),
('111aAA', 95, 23, 35),
('111', 96, 24, 35),
('111', 97, 22, 36),
('111', 98, 23, 36),
('111', 99, 24, 36),
('222', 100, 22, 37),
('222', 101, 23, 37),
('222', 102, 24, 37),
('333', 103, 22, 38),
('333', 104, 23, 38),
('333', 105, 24, 38),
('333', 106, 22, 39),
('333', 107, 23, 39),
('333', 108, 24, 39),
('333', 109, 22, 40),
('333', 110, 23, 40),
('333', 111, 24, 40),
('333', 112, 22, 41),
('333', 113, 23, 41),
('333', 114, 24, 41),
('333', 115, 22, 42),
('333', 116, 23, 42),
('333', 117, 24, 42),
('121212', 118, 22, 43),
('121212', 119, 23, 43),
('121212', 120, 24, 43),
('121212', 121, 22, 44),
('121212', 122, 23, 44),
('121212', 123, 24, 44),
('121212', 124, 22, 45),
('121212', 125, 23, 45),
('121212', 126, 24, 45),
('121212', 127, 22, 46),
('121212', 128, 23, 46),
('121212', 129, 24, 46),
('444', 130, 22, 47),
('444', 131, 23, 47),
('444', 132, 24, 47),
('4445', 133, 22, 48),
('4445', 134, 23, 48),
('4445', 135, 24, 48),
('5555', 136, 22, 49),
('555', 137, 23, 49),
('555', 138, 24, 49),
('666', 139, 22, 50),
('666', 140, 23, 50),
('666', 141, 24, 50),
('777', 142, 22, 51),
('777', 143, 23, 51),
('777', 144, 24, 51),
('888', 145, 22, 52),
('888', 146, 23, 52),
('888', 147, 24, 52),
('999', 148, 22, 53),
('999', 149, 23, 53),
('999', 150, 24, 53),
('qqq', 151, 22, 54),
('qqq', 152, 23, 54),
('qqq', 153, 24, 54),
('aaa', 154, 22, 55),
('aaa', 155, 23, 55),
('aaa', 156, 24, 55),
('轴', 157, 22, 57),
('pivot', 158, 23, 57),
('pivot', 159, 24, 57),
('测试选项组6', 160, 22, 58),
('2', 161, 22, 59),
('2', 162, 23, 59),
('2', 163, 24, 59),
('tk', 164, 22, 59),
('tk', 165, 23, 59),
('tk', 166, 24, 59),
('fdfdfdfd', 167, 22, 60),
('dfdfdf', 168, 23, 60),
('dfdfdf', 169, 24, 60),
('yyyyyyyyyyyy', 170, 22, 61),
('yyyyyyyy', 171, 23, 61),
('yyyyyyyyy', 172, 24, 61),
('fdffffffffffcd', 173, 22, 62),
('fffffffffff', 174, 23, 62),
('fffffffffff', 175, 24, 62),
('lljkj', 176, 22, 63),
('l', 177, 23, 63),
('lll', 178, 24, 63),
('dddddd', 179, 22, 64),
('ffffffssss', 180, 22, 65),
('ffffffffffffddddddddddddddd', 181, 22, 66);

-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_option_value`
-- 

CREATE TABLE `dshop_option_value` (
  `display_order` int(11) DEFAULT NULL,
  `option_group_id` bigint(20) unsigned DEFAULT NULL,
  `option_value_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`option_value_id`),
  UNIQUE KEY `option_value_id` (`option_value_id`),
  KEY `dshop_option_value_option_group_id` (`option_group_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=16 ;

-- 
-- 导出表中的数据 `dshop_option_value`
-- 

INSERT INTO `dshop_option_value` (`display_order`, `option_group_id`, `option_value_id`) VALUES 
(1, 1, 1),
(2, 2, 3),
(1, 2, 6),
(0, 51, 9),
(0, 2, 10),
(0, 3, 11),
(1, 3, 12),
(2, 3, 13),
(4, 3, 14),
(3, 3, 15);

-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_option_value_name`
-- 

CREATE TABLE `dshop_option_value_name` (
  `name` varchar(128) DEFAULT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `lang_id` bigint(20) unsigned DEFAULT NULL,
  `option_value_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `dshop_option_value_name_lang_id` (`lang_id`),
  KEY `dshop_option_value_name_option_value_id` (`option_value_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=36 ;

-- 
-- 导出表中的数据 `dshop_option_value_name`
-- 

INSERT INTO `dshop_option_value_name` (`name`, `id`, `lang_id`, `option_value_id`) VALUES 
('111米', 1, 22, 1),
('111M', 2, 23, 1),
('大', 5, 22, 3),
('中', 9, 22, 6),
('middle', 10, 23, 6),
('111Mi', 11, 24, 1),
('wqwq', 19, 22, 9),
('qwqwq', 20, 23, 9),
('qwqwq', 21, 24, 9),
('小', 22, 22, 10),
('small', 23, 23, 10),
('small', 24, 24, 10),
('红色', 25, 22, 11),
('red', 26, 23, 11),
('red', 27, 24, 11),
('黑色', 28, 22, 12),
('black', 29, 23, 12),
('白色', 30, 22, 13),
('white', 31, 23, 13),
('绿色', 32, 22, 14),
('green', 33, 23, 14),
('橙色', 34, 22, 15),
('orange', 35, 23, 15);

-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_product`
-- 

CREATE TABLE `dshop_product` (
  `product_id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `image` varchar(128) DEFAULT NULL,
  `active` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`product_id`),
  UNIQUE KEY `product_id` (`product_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

-- 
-- 导出表中的数据 `dshop_product`
-- 

INSERT INTO `dshop_product` (`product_id`, `image`, `active`) VALUES 
(1, '["/nopic.gif"]', NULL),
(2, '["/nopic.gif","/root/manufacturer/106ijenf6qmro1271916775755","/root/manufacturer/1xx15ustkll9r1271921005236"]', NULL),
(3, '["/root/manufacturer/106ijenf6qmro1271916775755","/root/manufacturer/1hja2hs7425pf1273123839335"]', 1),
(4, '["/root/manufacturer/106ijenf6qmro1271916775755"]', 1),
(5, '["/root/manufacturer/1hja2hs7425pf1273123839335"]', 1);

-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_product_category`
-- 

CREATE TABLE `dshop_product_category` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `category_id` bigint(20) unsigned DEFAULT NULL,
  `product_id` bigint(20) unsigned DEFAULT NULL,
  `display_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `dshop_product_category_category_id` (`category_id`),
  KEY `dshop_product_category_product_id` (`product_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=116 ;

-- 
-- 导出表中的数据 `dshop_product_category`
-- 

INSERT INTO `dshop_product_category` (`id`, `category_id`, `product_id`, `display_order`) VALUES 
(1, 1, 1, 0),
(7, 14, 1, 0),
(112, 56, 2, 10),
(113, 56, 3, 15),
(114, 56, 4, 0),
(115, 56, 5, 2147483647);

-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_product_name_description`
-- 

CREATE TABLE `dshop_product_name_description` (
  `name` varchar(128) DEFAULT NULL,
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `lang_id` bigint(20) unsigned DEFAULT NULL,
  `product_id` bigint(20) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `dshop_product_name_description_lang_id` (`lang_id`),
  KEY `dshop_product_name_description_product_id` (`product_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=6 ;

-- 
-- 导出表中的数据 `dshop_product_name_description`
-- 

INSERT INTO `dshop_product_name_description` (`name`, `id`, `description`, `lang_id`, `product_id`) VALUES 
('商品1', 1, '商品1', 22, 1),
('p2', 2, 'p2', 22, 2),
('p3', 3, '', 22, 3),
('p4', 4, '', 22, 4),
('p5', 5, '', 22, 5);

-- --------------------------------------------------------

-- 
-- 表的结构 `dshop_upload_file`
-- 

CREATE TABLE `dshop_upload_file` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `real_name` varchar(128) DEFAULT NULL,
  `display_name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=36 ;

-- 
-- 导出表中的数据 `dshop_upload_file`
-- 

INSERT INTO `dshop_upload_file` (`id`, `real_name`, `display_name`) VALUES 
(9, '106ijenf6qmro1271916775755', 'http_imgload4.jpg'),
(12, '106ijenf6qmro1271916832232', '6PYL``)Q2EY($1PR6}CL}SU.JPG'),
(20, '1pttpub9halyb1271995089818', 'http_imgload4.jpg'),
(34, '1n1gcnqky02391272419500676', 'Desert Landscape.jpg'),
(35, '1hja2hs7425pf1273123839335', 'title.jpg');

-- --------------------------------------------------------

-- 
-- 表的结构 `users`
-- 

CREATE TABLE `users` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `firstname` varchar(32) DEFAULT NULL,
  `lastname` varchar(32) DEFAULT NULL,
  `email` varchar(48) DEFAULT NULL,
  `locale` varchar(16) DEFAULT NULL,
  `timezone` varchar(32) DEFAULT NULL,
  `password_pw` varchar(48) DEFAULT NULL,
  `password_slt` varchar(20) DEFAULT NULL,
  `textarea` varchar(2048) DEFAULT NULL,
  `superuser` tinyint(1) DEFAULT NULL,
  `validated` tinyint(1) DEFAULT NULL,
  `uniqueid` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `users_email` (`email`),
  KEY `users_uniqueid` (`uniqueid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- 
-- 导出表中的数据 `users`
-- 

