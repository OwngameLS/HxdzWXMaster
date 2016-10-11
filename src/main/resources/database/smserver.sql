/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50711
Source Host           : localhost:3306
Source Database       : smserver

Target Server Type    : MYSQL
Target Server Version : 50711
File Encoding         : 65001

Date: 2016-10-11 11:21:25
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `contact`
-- ----------------------------
DROP TABLE IF EXISTS `contact`;
CREATE TABLE `contact` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `groupname` text,
  `name` text,
  `title` text,
  `phone` text,
  `description` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of contact
-- ----------------------------
INSERT INTO `contact` VALUES ('1', 'A', '张三', 'asd', '13945671987', 'adsfasd');
INSERT INTO `contact` VALUES ('3', 'A', 'Owngamesd', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('4', 'A', 'aad', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('5', 'A', 'fgh', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('6', 'A', 'ghi', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('7', 'B', 'hij', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('8', 'B', 'ijk', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('9', 'B', 'abc', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('10', 'B', 'cde', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('11', 'B', 'def', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('12', 'B', 'efg', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('17', 'A', 'abc', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('18', 'A', 'cde', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('19', 'A', 'def', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('20', 'A', 'aad', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('21', 'A', 'fgh', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('22', 'A', 'ghi', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('23', 'B', 'hij', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('24', 'B', 'ijk', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('25', 'B', 'abc', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('26', 'B', 'cde', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('27', 'B', 'def', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('28', 'B', 'efg', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('33', 'A', 'abc', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('34', 'A', 'cde', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('35', 'A', 'def', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('36', 'A', 'aad', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('37', 'A', 'fgh', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('38', 'A', 'ghi', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('39', 'B', 'hij', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('40', 'B', 'ijk', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('41', 'B', 'abc', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('42', 'B', 'cde', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('43', 'B', 'def', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('44', 'B', 'efg', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('49', 'A', 'abc', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('50', 'A', 'cde', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('51', 'A', 'def', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('52', 'A', 'aad', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('53', 'A', 'fgh', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('54', 'A', 'ghi', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('55', 'B', 'hij', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('56', 'B', 'ijk', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('57', 'B', 'abc', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('58', 'B', 'cde', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('59', 'B', 'def', 'asd', '13945677891', 'adsfasd');
INSERT INTO `contact` VALUES ('60', 'B', 'efg', 'asd', '13945677891', 'adsfasd');

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
  `sortfields` text,
  `fields` text,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of function
-- ----------------------------
INSERT INTO `function` VALUES ('1', 'abc', 'Test1', 'abc', '192.168.1.111', '3306', 'MySQL', 'bzdb', 'owngame', 'root', 'gameinfos', 'gametime desc', 'player_statids_home,home,-1,NN#player_statids_guest,guest,-1,NN');
INSERT INTO `function` VALUES ('2', 'cde', 'Test2', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `function` VALUES ('3', 'efg', 'Test3', null, null, null, null, null, null, null, null, null, null);
INSERT INTO `function` VALUES ('4', 'ghi', 'Test4', null, null, null, null, null, null, null, null, null, null);

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
INSERT INTO `qrtz_triggers` VALUES ('quartzScheduler', 'ad3e2749-f5f5-4bcd-a8d8-89260ff2da99', 'DEFAULT', 'jobDetail', 'DEFAULT', null, '1476154140000', '1476154121335', '5', 'PAUSED', 'CRON', '1476152342000', '0', null, '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787001737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F4000000000000C7708000000100000000274000966756E6374696F6E73740003616263740009726563656976657273740007312C332C342C357800);

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
) ENGINE=MyISAM AUTO_INCREMENT=379 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of task
-- ----------------------------
INSERT INTO `task` VALUES ('378', 'testName', 'descirption test....', '0', 'Test1???::home:4,5,6;guest:7,8,9;#', '13945671987,13945677891,13945677891,13945677891', '2016-10-11 10:48:41');

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
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of timertask
-- ----------------------------
INSERT INTO `timertask` VALUES ('2', 'ad3e2749-f5f5-4bcd-a8d8-89260ff2da99', 'abc', 'abc的功能，测试。', '0 0/1 * * * ? *', '1,3,4,5', 'pause');
