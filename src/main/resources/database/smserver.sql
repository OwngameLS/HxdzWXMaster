/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50709
Source Host           : localhost:3306
Source Database       : smserver

Target Server Type    : MYSQL
Target Server Version : 50709
File Encoding         : 65001

Date: 2017-03-16 23:14:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for askrecord
-- ----------------------------
DROP TABLE IF EXISTS `askrecord`;
CREATE TABLE `askrecord` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `type` tinyint(1) DEFAULT NULL,
  `issuccess` tinyint(1) DEFAULT NULL,
  `functions` text,
  `description` text,
  `time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=426 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of askrecord
-- ----------------------------
INSERT INTO `askrecord` VALUES ('385', 'Owngame', '18107436127', '1', '1', '查询功能[abc] 成功。', 'null', '2016-12-07 22:50:19');
INSERT INTO `askrecord` VALUES ('386', 'Owngame', '18107436127', '1', '1', '查询关键字[def] 没有找到对应的功能，因此没有获得查询结果。查询关键字[2] 没有找到对应的功能，因此没有获得查询结果。查询关键字[33] 没有找到对应的功能，因此没有获得查询结果。查询功能[abc] 成功。', 'null', '2016-12-07 22:50:18');
INSERT INTO `askrecord` VALUES ('387', 'Owngame', '18107436127', '1', '1', 'abc,def,2,33', '查询关键字[def] 没有找到对应的功能，因此没有获得查询结果。查询关键字[2] 没有找到对应的功能，因此没有获得查询结果。查询关键字[33] 没有找到对应的功能，因此没有获得查询结果。查询功能[abc] 成功。', '2016-12-07 22:50:10');
INSERT INTO `askrecord` VALUES ('388', 'Owngame', '18107436127', '1', '1', 'abc,def,2,33', '查询关键字[def] 没有找到对应的功能，因此没有获得查询结果。查询关键字[2] 没有找到对应的功能，因此没有获得查询结果。查询关键字[33] 没有找到对应的功能，因此没有获得查询结果。查询功能[abc] 成功。', '2016-12-07 22:50:09');
INSERT INTO `askrecord` VALUES ('389', 'Owngame', '18107436127', '1', '0', 'abc,def,2,33', '查询关键字[def] 没有找到对应的功能，因此没有获得查询结果。查询关键字[2] 没有找到对应的功能，因此没有获得查询结果。查询关键字[33] 没有找到对应的功能，因此没有获得查询结果。查询功能[abc] 成功。', '2016-12-07 22:53:10');
INSERT INTO `askrecord` VALUES ('390', 'Owngame', '18107436127', '1', '0', 'abc', '功能[abc]的查询结果：统计类型:T;\n', '2016-12-10 12:57:39');
INSERT INTO `askrecord` VALUES ('391', 'Owngame', '18107436127', '1', '0', 'abc,abcde,abcdee', '关键字[]所查询的功能暂时不可用;\n关键字[]所查询的功能暂时不可用;\n功能[abc]的查询结果：统计类型:T;\n', '2016-12-10 12:58:48');
INSERT INTO `askrecord` VALUES ('392', 'Owngame', '18107436127', '1', '1', '帮助信息', '查询帮助信息。', '2016-12-10 13:00:35');
INSERT INTO `askrecord` VALUES ('393', 'Owngame', '18107436127', '1', '1', '查询关键字信息', '查询功能对应的关键字信息。', '2016-12-10 13:00:42');
INSERT INTO `askrecord` VALUES ('394', 'Owngame', '18107436127', '1', '0', 'ab', '关键字[]所查询的功能暂时不可用;\n', '2016-12-10 13:02:05');
INSERT INTO `askrecord` VALUES ('395', 'Owngame', '18107436127', '1', '0', 'ad,abc,dee', '关键字[ad]对应的功能由于你的权限不足，无法查询;\n关键字[dee] 没有查询到对应的功能。您可能要查询的关键字有[null]。功能[abc]的查询结果：统计类型:T;\n', '2016-12-10 13:16:14');
INSERT INTO `askrecord` VALUES ('396', 'Owngame', '18107436127', '1', '0', 'ad,abc,dee', '关键字[ad]对应的功能由于你的权限不足，无法查询;\n关键字[dee] 没有查询到对应的功能。功能[abc]的查询结果：统计类型:T;\n', '2016-12-10 13:17:13');
INSERT INTO `askrecord` VALUES ('397', 'Owngame', '18107436127', '1', '0', 'ac', '关键字[ac] 没有查询到对应的功能。', '2016-12-11 22:56:55');
INSERT INTO `askrecord` VALUES ('398', '管理员', '管理员', '2', '1', 'abc', '管理员网页查询。功能[abc]的查询结果：统计类型:T;\n', '2016-12-11 22:58:03');
INSERT INTO `askrecord` VALUES ('399', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端查询。景美滴好嗯可以的没有', '2016-12-20 21:39:32');
INSERT INTO `askrecord` VALUES ('400', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端查询。景美滴好嗯可以的没有', '2016-12-20 21:41:14');
INSERT INTO `askrecord` VALUES ('401', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端查询。景美滴好嗯可以的没有', '2016-12-20 21:41:24');
INSERT INTO `askrecord` VALUES ('402', 'Owngame', '18107436127', '1', '0', 'abc', '功能[abc]的查询结果：统计类型:T;\n', '2016-12-20 21:44:46');
INSERT INTO `askrecord` VALUES ('403', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端操作。景美滴好嗯可以的没有', '2016-12-20 21:48:21');
INSERT INTO `askrecord` VALUES ('404', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端操作。景美滴好嗯可以的没有', '2016-12-20 21:48:43');
INSERT INTO `askrecord` VALUES ('405', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端操作。景美滴好嗯可以的没有', '2016-12-20 21:49:43');
INSERT INTO `askrecord` VALUES ('406', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端操作。景美滴好嗯可以的没有', '2016-12-20 21:50:11');
INSERT INTO `askrecord` VALUES ('407', 'Owngame', '18107436127', '1', '0', 'abc', '功能[abc]的查询结果：统计类型:T;\n', '2016-12-20 21:53:07');
INSERT INTO `askrecord` VALUES ('408', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端操作。景美滴好嗯可以的没有', '2016-12-20 21:55:58');
INSERT INTO `askrecord` VALUES ('409', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端操作。景美滴好嗯可以的没有', '2016-12-20 21:56:35');
INSERT INTO `askrecord` VALUES ('410', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端操作。景美滴好嗯可以的没有', '2016-12-20 22:01:36');
INSERT INTO `askrecord` VALUES ('411', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端操作。景美滴好嗯可以的没有', '2016-12-20 22:02:18');
INSERT INTO `askrecord` VALUES ('412', 'Owngame', '18107436127', '1', '0', 'abc', '功能[abc]的查询结果：统计类型:T;\n', '2016-12-20 22:05:38');
INSERT INTO `askrecord` VALUES ('413', '管理员', '管理员', '3', '1', '自定义消息。', '管理员客户端操作。景美滴好嗯可以的没有', '2016-12-20 22:07:08');
INSERT INTO `askrecord` VALUES ('414', '管理员', '管理员', '3', '1', '自定义消息 与 功能查询结果(abc)。', '管理员客户端操作。你真的是我群发的消息吗？功能[abc]的查询结果：统计类型:T;\n', '2016-12-20 22:07:42');
INSERT INTO `askrecord` VALUES ('415', 'Owngame', '18107436127', '1', '1', 'B股', '关键字[B股] 没有查询到对应的功能。', '2016-12-24 12:56:25');
INSERT INTO `askrecord` VALUES ('416', 'Owngame', '18107436127', '1', '1', 'abc', '功能[abc]的查询结果：统计类型:T;\n', '2017-01-16 12:17:10');
INSERT INTO `askrecord` VALUES ('417', 'Owngame', '18107436127', '1', '1', 'abc', '功能[abc]的查询结果：统计类型:T;\n', '2017-01-16 21:55:24');
INSERT INTO `askrecord` VALUES ('418', 'Owngame', '18107436127', '1', '1', 'abc', '功能[abc]的查询结果：统计类型:T;\n', '2017-01-16 21:55:50');
INSERT INTO `askrecord` VALUES ('419', 'Owngame', '18107436127', '1', '1', 'abc', '功能[abc]的查询结果：统计类型:T;\n', '2017-01-16 22:09:23');
INSERT INTO `askrecord` VALUES ('420', 'Owngame', '18107436127', '1', '1', '', '快捷回复了关键字[龙生];龙生最棒！关键字[] 没有查询到对应的功能。您可能要查询的关键字有[abc,cde,tt,ad,222,3,32]。', '2017-03-15 21:44:35');
INSERT INTO `askrecord` VALUES ('421', 'Owngame', '18107436127', '1', '1', '', '快捷回复了关键字[龙生];龙生最棒！关键字[] 没有查询到对应的功能。您可能要查询的关键字有[abc,cde,tt,ad,222,3,32]。', '2017-03-15 21:44:35');
INSERT INTO `askrecord` VALUES ('422', 'Owngame', '18107436127', '1', '1', 'nofunctions', '快捷回复了关键字[龙生];', '2017-03-15 21:53:25');
INSERT INTO `askrecord` VALUES ('423', 'Owngame', '18107436127', '1', '1', 'nofunctions', '快捷回复了关键字[龙生];', '2017-03-15 21:53:25');
INSERT INTO `askrecord` VALUES ('424', 'Owngame', '18107436127', '1', '1', 'nofunctions', '快捷回复了关键字[龙生];', '2017-03-15 21:53:36');
INSERT INTO `askrecord` VALUES ('425', 'Owngame', '18107436127', '1', '1', 'nofunctions', '快捷回复了关键字[龙生];', '2017-03-15 22:03:11');

-- ----------------------------
-- Table structure for authorization
-- ----------------------------
DROP TABLE IF EXISTS `authorization`;
CREATE TABLE `authorization` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` text,
  `mac` text,
  `phone` text,
  `expiration` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of authorization
-- ----------------------------
INSERT INTO `authorization` VALUES ('1', 'Owngame', '44-37-E6-CC-7A-4D', '18107436127', '1515396574956');
INSERT INTO `authorization` VALUES ('2', 'owngamee', null, '18107436128', '1515396654224');
INSERT INTO `authorization` VALUES ('3', 'oowngamee', null, null, null);
INSERT INTO `authorization` VALUES ('4', 'ls', null, '18107436129', '1515396794957');
INSERT INTO `authorization` VALUES ('5', 'eeee', null, '18107436120', '1515401583888');
INSERT INTO `authorization` VALUES ('6', 'owngamea', null, '18107436127', '0');
INSERT INTO `authorization` VALUES ('7', 'zhiyun', null, '18107436666', '0');
INSERT INTO `authorization` VALUES ('8', 'zhiyuna', null, '18107463333', '0');

-- ----------------------------
-- Table structure for contactbase
-- ----------------------------
DROP TABLE IF EXISTS `contactbase`;
CREATE TABLE `contactbase` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupname` text,
  `name` text,
  `title` text,
  `highid` int(11) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contactbase
-- ----------------------------
INSERT INTO `contactbase` VALUES ('1', 'A', 'Owngame', '123.0', '20', 'waerfa');
INSERT INTO `contactbase` VALUES ('2', 'A', '张三', '123.0', '26', 'waerfa');
INSERT INTO `contactbase` VALUES ('3', 'A', '李四', '123.0', '22', 'waerfa');
INSERT INTO `contactbase` VALUES ('4', 'A', '王五', '123.0', '23', 'waerfa');
INSERT INTO `contactbase` VALUES ('5', 'A', '赵六', '123.0', '24', 'waerfa');
INSERT INTO `contactbase` VALUES ('6', 'A', '孙七', '123.0', '25', 'waerfa');
INSERT INTO `contactbase` VALUES ('7', 'A', '龙八', '123.0', '26', 'waerfa');
INSERT INTO `contactbase` VALUES ('8', 'B', 'Owngame', '123.0', '27', 'waerfa');
INSERT INTO `contactbase` VALUES ('9', 'B', '张三', '123.0', '21', 'waerfa');
INSERT INTO `contactbase` VALUES ('10', 'B', '李四', '123.0', '28', 'waerfa');
INSERT INTO `contactbase` VALUES ('11', 'B', '王五', '123.0', '29', 'waerfa');
INSERT INTO `contactbase` VALUES ('12', 'B', '赵六', '123.0', '30', 'waerfa');
INSERT INTO `contactbase` VALUES ('13', 'B', '孙七', '123.0', '31', 'waerfa');
INSERT INTO `contactbase` VALUES ('14', 'C', '龙八', '123.0', '32', 'waerfa');
INSERT INTO `contactbase` VALUES ('15', 'C', 'Owngame', '123.0', '33', 'waerfa');
INSERT INTO `contactbase` VALUES ('16', 'C', '张三', '123.0', '34', 'waerfa');
INSERT INTO `contactbase` VALUES ('17', 'C', '龙生', '阿哥', '20', '备注');
INSERT INTO `contactbase` VALUES ('18', 'C', '龙生22', '阿哥', '20', '备注');
INSERT INTO `contactbase` VALUES ('19', 'D', '龙生', '阿哥', '20', '备注');
INSERT INTO `contactbase` VALUES ('20', 'D', '龙生22', '阿哥', '20', '备注');
INSERT INTO `contactbase` VALUES ('21', 'D', '龙八', '123.0', '32', 'waerfa');
INSERT INTO `contactbase` VALUES ('22', 'D', 'Owngame', '123.0', '33', 'waerfa');
INSERT INTO `contactbase` VALUES ('23', 'D', '张三', '123.0', '34', 'waerfa');

-- ----------------------------
-- Table structure for contacthigh
-- ----------------------------
DROP TABLE IF EXISTS `contacthigh`;
CREATE TABLE `contacthigh` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` text NOT NULL,
  `grade` varchar(3) NOT NULL,
  `openid` text,
  `backup` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=36 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contacthigh
-- ----------------------------
INSERT INTO `contacthigh` VALUES ('20', '18107436127', '7', 'oZDbcsmRjwkLHJd-gGEq83cuIjXk', null);
INSERT INTO `contacthigh` VALUES ('21', '18107436126', '0', null, null);
INSERT INTO `contacthigh` VALUES ('22', '18107436125', '0', null, null);
INSERT INTO `contacthigh` VALUES ('23', '18107436124', '4', null, null);
INSERT INTO `contacthigh` VALUES ('24', '18107436123', '2', null, null);
INSERT INTO `contacthigh` VALUES ('25', '18107436122', '0', null, null);
INSERT INTO `contacthigh` VALUES ('26', '18107436121', '0', null, null);
INSERT INTO `contacthigh` VALUES ('27', '18107436120', '0', null, null);
INSERT INTO `contacthigh` VALUES ('28', '18107436118', '0', null, null);
INSERT INTO `contacthigh` VALUES ('29', '18107436117', '0', null, null);
INSERT INTO `contacthigh` VALUES ('30', '18107436116', '0', null, null);
INSERT INTO `contacthigh` VALUES ('31', '18107436115', '0', null, null);
INSERT INTO `contacthigh` VALUES ('32', '18107436114', '0', null, null);
INSERT INTO `contacthigh` VALUES ('33', '18107436113', '0', null, null);
INSERT INTO `contacthigh` VALUES ('34', '18107436112', '0', null, null);
INSERT INTO `contacthigh` VALUES ('35', 'superman', '7', null, 'superman');

-- ----------------------------
-- Table structure for function
-- ----------------------------
DROP TABLE IF EXISTS `function`;
CREATE TABLE `function` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text,
  `description` text,
  `keywords` text,
  `ip` varchar(20) DEFAULT NULL,
  `port` varchar(10) DEFAULT NULL,
  `dbtype` varchar(20) DEFAULT NULL,
  `dbname` varchar(20) DEFAULT NULL,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `tablename` varchar(20) DEFAULT NULL,
  `usetype` varchar(5) DEFAULT NULL,
  `readfields` text,
  `sortfields` text,
  `fieldrules` text,
  `isreturn` varchar(10) DEFAULT NULL,
  `sqlstmt` text,
  `sqlfields` text,
  `grade` varchar(3) DEFAULT NULL,
  `usable` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of function
-- ----------------------------
INSERT INTO `function` VALUES ('1', 'abc', 'Test1Test1Test1Test1Test', 'abc', 'localhost', '3306', 'MySQL', 'bzdb', 'owngame', 'root', 'gameinfos', 'sql', 'undefined', 'undefined', 'undefined', 'undefined', 'select count_type from gameinfos;', 'count_type,统计类型', '4', 'yes');
INSERT INTO `function` VALUES ('2', 'cde', 'Test2', 'cde', null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', 'no');
INSERT INTO `function` VALUES ('3', 'efg', 'Test3Test3Test3Test3', 'tt', '', '', 'MySQL', '', '', '', '', 'sql', 'undefined', 'undefined', 'undefined', 'undefined', '', 'id,序号#count_type,统计类型', '1', 'no');
INSERT INTO `function` VALUES ('4', 'ghi', 'Test4', 'ad', null, null, null, null, null, null, null, null, null, null, null, null, null, null, '7', 'no');
INSERT INTO `function` VALUES ('5', 'te22', '23442', '222', 'localhost', '3306', 'MySQL', 'bzdb', 'owngame', 'root', 'gameinfos', 'sql', 'undefined', 'undefined', 'undefined', 'undefined', 'select id, count_type from gameinfos', 'id,序号#count_type,统计类型', '1', 'no');
INSERT INTO `function` VALUES ('6', '23234', '23442', '3', 'localhost', '3306', 'MySQL', 'bzdb', 'owngame', 'root', 'gameinfos', 'rule', 'id,序号#count_type,统计类型', 'id desc', 'count_type,统计类型,5,NE', 'oncase', 'undefined', 'undefined', '5', 'no');
INSERT INTO `function` VALUES ('7', '23234', '23442', '32', 'localhost', '3306', 'MySQL', 'bzdb', 'owngame', 'root', 'gameinfos', 'rules', '', '', '', 'anyway', '', '', '0', 'no');

-- ----------------------------
-- Table structure for qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_blob_triggers`;
CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_blob_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_calendars`;
CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_calendars
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_cron_triggers`;
CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_cron_triggers
-- ----------------------------
INSERT INTO `qrtz_cron_triggers` VALUES ('quartzScheduler', '2fb7e32b-de19-44e0-acfc-97af9f8ad477', 'DEFAULT', '0 * * * * ? *', 'GMT+08:00');

-- ----------------------------
-- Table structure for qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_fired_triggers`;
CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_fired_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_job_details`;
CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_job_details
-- ----------------------------
INSERT INTO `qrtz_job_details` VALUES ('quartzScheduler', 'jobDetail', 'DEFAULT', null, 'com.owngame.utils.MyQuartzJobBean', '1', '1', '1', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000010770800000010000000007800);

-- ----------------------------
-- Table structure for qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_locks`;
CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_locks
-- ----------------------------
INSERT INTO `qrtz_locks` VALUES ('quartzScheduler', 'TRIGGER_ACCESS');

-- ----------------------------
-- Table structure for qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;
CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_paused_trigger_grps
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_scheduler_state`;
CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_scheduler_state
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simple_triggers`;
CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simple_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_simprop_triggers`;
CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_simprop_triggers
-- ----------------------------

-- ----------------------------
-- Table structure for qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `qrtz_triggers`;
CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of qrtz_triggers
-- ----------------------------
INSERT INTO `qrtz_triggers` VALUES ('quartzScheduler', '2fb7e32b-de19-44e0-acfc-97af9f8ad477', 'DEFAULT', 'jobDetail', 'DEFAULT', null, '1476928680000', '1476928648239', '5', 'PAUSED', 'CRON', '1476928060000', '0', null, '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000274000966756E6374696F6E73740003616263740009726563656976657273740007312C332C342C357800);

-- ----------------------------
-- Table structure for quickanswers
-- ----------------------------
DROP TABLE IF EXISTS `quickanswers`;
CREATE TABLE `quickanswers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keyname` text CHARACTER SET utf8,
  `result` text CHARACTER SET utf8,
  `description` text,
  `enable` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of quickanswers
-- ----------------------------
INSERT INTO `quickanswers` VALUES ('1', '龙生', '龙生最棒！', null, '1');
INSERT INTO `quickanswers` VALUES ('2', '龙生2', '哈哈哈', null, '1');
INSERT INTO `quickanswers` VALUES ('3', '龙生4', '你你你啊啊', null, '1');
INSERT INTO `quickanswers` VALUES ('4', 'aaa', 'nimade', null, '0');
INSERT INTO `quickanswers` VALUES ('5', 'aa', '3e', null, '1');
INSERT INTO `quickanswers` VALUES ('7', '322', 'aaaa', null, '1');

-- ----------------------------
-- Table structure for settings
-- ----------------------------
DROP TABLE IF EXISTS `settings`;
CREATE TABLE `settings` (
  `id` tinyint(4) NOT NULL AUTO_INCREMENT,
  `description` text,
  `name` text,
  `value` text,
  `referto` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of settings
-- ----------------------------
INSERT INTO `settings` VALUES ('1', '授权状态', 'authorizedState', 'valid', 'no');
INSERT INTO `settings` VALUES ('2', '到期时间', 'validTime', '1515396574956', 'no');
INSERT INTO `settings` VALUES ('3', 'nodes', '5891675fd9c7128fb390426a184f9f29', '62c4ece6fa44df42618188dcee0c187d', 'no');
INSERT INTO `settings` VALUES ('4', '授权中心手机号', 'phone', '18107436127', 'no');
INSERT INTO `settings` VALUES ('5', '用户名', 'username', 'Owngame', 'no');
INSERT INTO `settings` VALUES ('6', '数据库用户名', 'db_username', 'root', 'no');
INSERT INTO `settings` VALUES ('7', '数据库密码', 'db_password', 'root', 'no');
INSERT INTO `settings` VALUES ('8', '数据库端口', 'db_port', '3306', 'no');
INSERT INTO `settings` VALUES ('9', '是否有微信公众号', 'wx_hasmp', 'true', 'self');
INSERT INTO `settings` VALUES ('10', 'AppID(应用ID)', 'wx_appid', 'wxac0bb909a8c87ebc', 'wx_hasmp');
INSERT INTO `settings` VALUES ('11', 'AppSecret(应用密钥)', 'wx_appsecret', 'd4624c36b6795d1d99dcf0547af5443d', 'wx_hasmp');
INSERT INTO `settings` VALUES ('12', 'URL(服务器地址)', 'wx_url', 'http://vgogoing.tunnel.2bdata.com/Smserver/', 'wx_hasmp');
INSERT INTO `settings` VALUES ('13', 'Token(令牌)', 'wx_token', 'Owngame', 'wx_hasmp');
INSERT INTO `settings` VALUES ('14', 'EncodingAESKey(消息加解密密钥)', 'wx_encodingAESkey', null, 'wx_hasmp');
INSERT INTO `settings` VALUES ('15', '测试微信', 'wx_test', '123', 'wx_hasmp');
INSERT INTO `settings` VALUES ('16', '测试依赖', 'yl_1', 'true', 'self');
INSERT INTO `settings` VALUES ('17', '依赖1', 'yl_aa', 'aa', 'yl_1');
INSERT INTO `settings` VALUES ('18', '依赖2', 'yl_bb', 'bb', 'yl_1');
INSERT INTO `settings` VALUES ('20', '新增测试', 'newadd2', 'false', 'self');
INSERT INTO `settings` VALUES ('21', '1233241', 'asdaf', 'false', 'self');
INSERT INTO `settings` VALUES ('22', 'wodem', 'wodema', '789123', 'yl_1');
INSERT INTO `settings` VALUES ('23', 'nidema', 'nidema', '8888', 'yl_1');
INSERT INTO `settings` VALUES ('24', 'sdaf', '3242', '0000', 'asdaf');
INSERT INTO `settings` VALUES ('25', '14321', '143', '1342', 'newadd2');
INSERT INTO `settings` VALUES ('44', 'ls', 'ls', 'ls', 'newadd2');
INSERT INTO `settings` VALUES ('45', '依赖测试ls', 'ces', 'false', 'self');
INSERT INTO `settings` VALUES ('49', '授权失败原因', 'invalidReason', 'no', 'no');
INSERT INTO `settings` VALUES ('50', '用户手机号', 'userphone', '18107436127', 'no');
INSERT INTO `settings` VALUES ('51', 'nodes', 'password', 'bbc100ce57af46bbed25d29222087bda', 'no');

-- ----------------------------
-- Table structure for task
-- ----------------------------
DROP TABLE IF EXISTS `task`;
CREATE TABLE `task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` text,
  `description` text,
  `state` tinyint(4) DEFAULT NULL,
  `content` text,
  `receivers` text,
  `createTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=403 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('379', '群发消息', '主动发送消息给一部分人...', '2', 'asdfasdf', '18107436127', '2016-11-24 11:31:48');
INSERT INTO `task` VALUES ('380', '群发消息', '主动发送消息给一部分人...(消息内容:asdfklsa;jfl;sadfu8iorkenfl;daksjflkadsjfoijsadlfksadlk;fj)', '2', 'asdfklsa;jfl;sadfu8iorkenfl;daksjflkadsjfoijsadlfksadlk;fj', '18107436127', '2016-11-24 11:31:48');
INSERT INTO `task` VALUES ('381', '群发消息', '主动发送消息给一部分人...(消息内容:test contents...)', '2', 'test contents...', '13945671987,13945677891,18107436127', '2016-11-24 11:31:48');
INSERT INTO `task` VALUES ('384', '变更绑定微信号', '手机号18107436127变更绑定其微信号，用户验证信息发送。', '-1', '288594', '18107436127', '2016-12-07 10:46:34');
INSERT INTO `task` VALUES ('385', '群发消息', '群发消息...(消息内容:1231234321)', '-1', '1231234321', '18107436127', '2016-12-24 13:02:35');
INSERT INTO `task` VALUES ('386', '群发消息', '群发消息...(消息内容:adsfas)', '-1', 'adsfas', '18107436127', '2017-01-02 12:32:37');
INSERT INTO `task` VALUES ('387', 'authorize request', '授权申请', '-1', 'AR--Owngame--60-A4-4C-D0-21-A2', '18107436127', '2017-01-10 15:49:46');
INSERT INTO `task` VALUES ('388', 'authorize request', '授权申请', '-1', 'AR--Owngame--60-A4-4C-D0-21-A2', '18107436127', '2017-01-10 15:49:53');
INSERT INTO `task` VALUES ('392', 'authorize repost', '授权应答', '2', 'AR--FAILED--您尚未缴费，请充值后再获取授权吧。', '13581695827', '2017-01-11 10:56:16');
INSERT INTO `task` VALUES ('391', 'authorize request', '授权申请', '2', 'AR--Owngame--13581695827--44-37-E6-CC-7A-4D', '18107436127', '2017-01-11 10:56:06');
INSERT INTO `task` VALUES ('393', 'authorize request', '授权申请', '1', 'AR--Owngame--18107436127--44-37-E6-CC-7A-4D', '18107436127', '2017-01-11 10:59:17');
INSERT INTO `task` VALUES ('394', 'authorize repost', '授权应答', '2', 'AR--FAILED--您尚未缴费，请充值后再获取授权吧。', '18107436127', '2017-01-11 10:59:27');
INSERT INTO `task` VALUES ('395', 'authorize request', '授权申请', '2', 'AR--Owngame--18107436127--44-37-E6-CC-7A-4D', '18107436127', '2017-01-11 11:01:38');
INSERT INTO `task` VALUES ('396', 'authorize repost', '授权应答', '2', 'AR--FAILED--您尚未缴费，请充值后再获取授权吧。', '18107436127', '2017-01-11 11:01:49');
INSERT INTO `task` VALUES ('397', 'authorize request', '授权申请', '2', 'AR--Owngame--18107436127--44-37-E6-CC-7A-4D', '18107436127', '2017-01-11 11:19:10');
INSERT INTO `task` VALUES ('398', 'authorize repost', '授权应答', '2', 'AR--FAILED--您尚未缴费，请充值后再获取授权吧。', '18107436127', '2017-01-11 11:19:20');
INSERT INTO `task` VALUES ('399', 'authorize request', '授权申请', '2', 'AR--Owngame--18107436127--44-37-E6-CC-7A-4D', '18107436127', '2017-01-11 11:21:30');
INSERT INTO `task` VALUES ('400', 'authorize repost', '授权应答', '2', 'AR--FAILED--nofee', '18107436127', '2017-01-11 11:21:40');
INSERT INTO `task` VALUES ('401', 'authorize request', '授权申请', '2', 'AR--Owngame--18107436127--44-37-E6-CC-7A-4D', '18107436127', '2017-01-16 12:16:01');
INSERT INTO `task` VALUES ('402', 'authorize repost', '授权应答', '2', 'AR--SUCCESS--1515396574956--62c4ece6fa44df42618188dcee0c187d', '18107436127', '2017-01-16 12:16:13');

-- ----------------------------
-- Table structure for timertask
-- ----------------------------
DROP TABLE IF EXISTS `timertask`;
CREATE TABLE `timertask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `functions` text,
  `description` text,
  `firerules` varchar(120) DEFAULT NULL,
  `receivetype` tinyint(1) DEFAULT '0',
  `receivers` text,
  `state` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of timertask
-- ----------------------------
INSERT INTO `timertask` VALUES ('3', '2fb7e32b-de19-44e0-acfc-97af9f8ad477', 'abc', '关于ABC的故事', '0 * * * * ? *', '0', '', 'pause');

-- ----------------------------
-- Table structure for weixinaccesstoken
-- ----------------------------
DROP TABLE IF EXISTS `weixinaccesstoken`;
CREATE TABLE `weixinaccesstoken` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accesstoken` text CHARACTER SET utf8 NOT NULL,
  `expiresin` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of weixinaccesstoken
-- ----------------------------
INSERT INTO `weixinaccesstoken` VALUES ('1', 'aD67rwMk4wO5GX9QS8diGWta1_Q5vwsRTTwS0UuUH1JuU_SzwrCc_izAoh3bzf23gEXnGAAt8KWjwAfCDQ7f41icewdAWqYSnGCd8GwR4UeYwHUTA68Qd4FqNCA_iRqUGMMhAGASYY', '1489592556300');
