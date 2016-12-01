/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50711
Source Host           : localhost:3306
Source Database       : smserver

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2016-11-30 14:51:56
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `contactbase`
-- ----------------------------
DROP TABLE IF EXISTS `contactbase`;
CREATE TABLE `contactbase` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupname` text,
  `name` text,
  `title` text,
  `phoneid` int(11) DEFAULT NULL,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=80 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contactbase
-- ----------------------------
INSERT INTO `contactbase` VALUES ('1', 'A', '张三', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('3', 'A', 'Owngamesd', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('4', 'A', '邵文泉', '老师', null, '老婆大人');
INSERT INTO `contactbase` VALUES ('5', 'A', 'fgh', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('6', 'A', 'ghi', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('7', 'B', 'hij', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('8', 'B', 'ijk', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('9', 'B', 'abc', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('10', 'B', 'cde', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('11', 'B', 'def', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('12', 'B', 'efg', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('17', 'A', 'abc', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('18', 'A', 'cde', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('19', 'A', 'def', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('20', 'A', 'aad', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('21', 'A', 'fgh', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('22', 'A', 'ghi', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('23', 'B', 'hij', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('24', 'B', 'ijk', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('25', 'B', 'abc', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('26', 'B', 'cde', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('27', 'B', 'def', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('28', 'B', 'efg', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('33', 'A', 'abc', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('34', 'A', 'cde', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('35', 'A', 'def', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('36', 'A', 'aad', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('37', 'A', 'fgh', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('38', 'A', 'ghi', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('39', 'B', 'hij', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('40', 'B', 'ijk', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('41', 'B', 'abc', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('42', 'B', 'cde', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('43', 'B', 'def', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('44', 'B', 'efg', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('49', 'A', 'abc', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('50', 'A', 'cde', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('51', 'A', 'def', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('52', 'A', 'aad', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('53', 'A', 'fgh', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('54', 'A', 'ghi', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('55', 'B', 'hij', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('56', 'B', 'ijk', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('57', 'B', 'abc', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('58', 'B', 'cde', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('59', 'B', 'def', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('60', 'B', 'efg', 'asd', null, 'adsfasd');
INSERT INTO `contactbase` VALUES ('79', null, null, null, null, null);

-- ----------------------------
-- Table structure for `contacthigh`
-- ----------------------------
DROP TABLE IF EXISTS `contacthigh`;
CREATE TABLE `contacthigh` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `phone` text NOT NULL,
  `grade` varchar(3) NOT NULL,
  `openid` text,
  `backup` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contacthigh
-- ----------------------------

-- ----------------------------
-- Table structure for `function`
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
INSERT INTO `function` VALUES ('2', 'cde', 'Test2', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '0', 'no');
INSERT INTO `function` VALUES ('3', 'efg', 'Test3Test3Test3Test3', '', '', '', 'MySQL', '', '', '', '', 'sql', 'undefined', 'undefined', 'undefined', 'undefined', '', 'id,序号#count_type,统计类型', '1', 'no');
INSERT INTO `function` VALUES ('4', 'ghi', 'Test4', null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, '1', 'no');
INSERT INTO `function` VALUES ('5', 'te22', '23442', '222', 'localhost', '3306', 'MySQL', 'bzdb', 'owngame', 'root', 'gameinfos', 'sql', 'undefined', 'undefined', 'undefined', 'undefined', 'select id, count_type from gameinfos', 'id,序号#count_type,统计类型', '1', 'no');
INSERT INTO `function` VALUES ('6', '23234', '23442', '32', 'localhost', '3306', 'MySQL', 'bzdb', 'owngame', 'root', 'gameinfos', 'rule', 'id,序号#count_type,统计类型', 'id desc', 'count_type,统计类型,5,NE', 'oncase', 'undefined', 'undefined', '5', 'no');
INSERT INTO `function` VALUES ('7', '23234', '23442', '32', 'localhost', '3306', 'MySQL', 'bzdb', 'owngame', 'root', 'gameinfos', 'rules', '', '', '', 'anyway', '', '', '0', 'no');

-- ----------------------------
-- Table structure for `qrtz_blob_triggers`
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
-- Table structure for `qrtz_calendars`
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
-- Table structure for `qrtz_cron_triggers`
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
-- Table structure for `qrtz_fired_triggers`
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
-- Table structure for `qrtz_job_details`
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
-- Table structure for `qrtz_locks`
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
-- Table structure for `qrtz_paused_trigger_grps`
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
-- Table structure for `qrtz_scheduler_state`
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
-- Table structure for `qrtz_simple_triggers`
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
-- Table structure for `qrtz_simprop_triggers`
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
-- Table structure for `qrtz_triggers`
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
-- Table structure for `task`
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
) ENGINE=MyISAM AUTO_INCREMENT=384 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('379', '群发消息', '主动发送消息给一部分人...', '2', 'asdfasdf', '18107436127', '2016-11-24 11:31:48');
INSERT INTO `task` VALUES ('380', '群发消息', '主动发送消息给一部分人...(消息内容:asdfklsa;jfl;sadfu8iorkenfl;daksjflkadsjfoijsadlfksadlk;fj)', '2', 'asdfklsa;jfl;sadfu8iorkenfl;daksjflkadsjfoijsadlfksadlk;fj', '18107436127', '2016-11-24 11:31:48');
INSERT INTO `task` VALUES ('381', '群发消息', '主动发送消息给一部分人...(消息内容:test contents...)', '2', 'test contents...', '13945671987,13945677891,18107436127', '2016-11-24 11:31:48');

-- ----------------------------
-- Table structure for `timertask`
-- ----------------------------
DROP TABLE IF EXISTS `timertask`;
CREATE TABLE `timertask` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  `functions` text,
  `description` text,
  `firerules` varchar(120) DEFAULT NULL,
  `receivers` text,
  `state` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of timertask
-- ----------------------------
INSERT INTO `timertask` VALUES ('3', '2fb7e32b-de19-44e0-acfc-97af9f8ad477', 'abc', '关于ABC的故事', '0 * * * * ? *', '1,3,4,5', 'pause');
