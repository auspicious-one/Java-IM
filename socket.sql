/*
Navicat MySQL Data Transfer

Source Server         : five
Source Server Version : 50045
Source Host           : localhost:3306
Source Database       : socket

Target Server Type    : MYSQL
Target Server Version : 50045
File Encoding         : 65001

Date: 2017-03-22 10:21:01
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `t_ceshi`
-- ----------------------------
DROP TABLE IF EXISTS `t_ceshi`;
CREATE TABLE `t_ceshi` (
  `id` int(255) NOT NULL auto_increment,
  `fid` int(255) NOT NULL,
  `uid` int(255) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of t_ceshi
-- ----------------------------
INSERT INTO t_ceshi VALUES ('1', '2', '1');
INSERT INTO t_ceshi VALUES ('2', '3', '2');
INSERT INTO t_ceshi VALUES ('3', '1', '4');

-- ----------------------------
-- Table structure for `t_customer`
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer` (
  `userid` int(255) NOT NULL auto_increment,
  `account` varchar(40) NOT NULL default '',
  `password` varchar(255) NOT NULL,
  `sex` varchar(255) NOT NULL,
  `qianming` varchar(255) NOT NULL,
  `tel` varchar(255) NOT NULL,
  `join_time` varchar(255) NOT NULL,
  `admin` int(64) unsigned zerofill NOT NULL,
  PRIMARY KEY  (`userid`),
  KEY `USERID` (`userid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_customer
-- ----------------------------

-- ----------------------------
-- Table structure for `t_friend`
-- ----------------------------
DROP TABLE IF EXISTS `t_friend`;
CREATE TABLE `t_friend` (
  `friends_id` int(255) NOT NULL auto_increment,
  `myfriends` int(255) default NULL,
  `userid` int(255) default NULL,
  PRIMARY KEY  (`friends_id`),
  KEY `userid` (`userid`),
  KEY `myfriends` (`myfriends`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of t_friend
-- ----------------------------

-- ----------------------------
-- Table structure for `t_info`
-- ----------------------------
DROP TABLE IF EXISTS `t_info`;
CREATE TABLE `t_info` (
  `infoId` int(10) NOT NULL auto_increment,
  `from_num` varchar(255) character set utf8 NOT NULL,
  `to_num` varchar(255) character set utf8 NOT NULL,
  `info` varchar(255) character set utf8 NOT NULL,
  `from_name` varchar(255) character set utf8 NOT NULL,
  `date` varchar(255) character set utf8 NOT NULL,
  `send` int(10) unsigned zerofill NOT NULL default '0000000000',
  PRIMARY KEY  (`infoId`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of t_info
-- ----------------------------
