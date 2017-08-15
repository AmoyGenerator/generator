Drop DATABASE if exists jmr;

CREATE DATABASE jmr;

use jmr;

/*
MySQL Data Transfer
Source Host: localhost
Source Database: jmr
Target Host: localhost
Target Database: jmr
Date: 2016/5/18 18:46:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `type` int(4) NOT NULL,
  `regist_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for codedesc
-- ----------------------------
CREATE TABLE `codedesc` (
  `type` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `code` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT '',
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `sort` int(10) DEFAULT NULL,
  PRIMARY KEY (`type`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for functions
-- ----------------------------
CREATE TABLE `functions` (
  `functionsid` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET gb2312 DEFAULT NULL,
  `link` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `sort` int(10) DEFAULT NULL,
  `topfuncid` int(10) DEFAULT NULL,
  `level` int(10) DEFAULT NULL,
  PRIMARY KEY (`functionsid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for member
-- ----------------------------
CREATE TABLE `member` (
  `memberid` int(11) NOT NULL AUTO_INCREMENT,
  `loginid` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `email` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `epaper` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `status` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `cuserid` int(10) DEFAULT NULL,
  `createdatetime` date DEFAULT NULL,
  `uuserid` int(10) DEFAULT NULL,
  `lastupdatetime` date DEFAULT NULL,
  PRIMARY KEY (`memberid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for news
-- ----------------------------
CREATE TABLE `news` (
  `newsid` int(11) NOT NULL AUTO_INCREMENT,
  `newstype` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `subject_chi` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `content_chi` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `subject_eng` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `content_eng` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `lang_chi` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `lang_eng` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  `openapply` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `cuserid` int(10) DEFAULT NULL,
  `createdatetime` date DEFAULT NULL,
  `uuserid` int(10) DEFAULT NULL,
  `lastupdatetime` date DEFAULT NULL,
  PRIMARY KEY (`newsid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Table structure for student
-- ----------------------------
CREATE TABLE `student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `sex` int(4) DEFAULT NULL,
  `registTime` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users
-- ----------------------------
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `account` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `password` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `name` varchar(50) CHARACTER SET utf8 DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `cuserid` int(11) DEFAULT NULL,
  `createdatetime` datetime DEFAULT NULL,
  `uuserid` int(11) DEFAULT NULL,
  `lastupdatetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `functions` VALUES ('2', 'users', 'users.do?act=find', '1', null, '1');
INSERT INTO `users` VALUES ('1', 'admin', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin', '1', '1', '2012-09-25 09:20:13', null, null);
INSERT INTO `users` VALUES ('2', 'admin2', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin2', '1', '1', '2012-09-25 09:47:55', null, null);
INSERT INTO `users` VALUES ('3', 'admin3', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin3', '1', '1', '2012-09-25 09:48:08', null, null);
INSERT INTO `users` VALUES ('4', 'admin4', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin4', '1', '1', '2012-09-25 09:48:23', null, null);
INSERT INTO `users` VALUES ('5', 'admin5', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin5', '1', '1', '2012-09-25 09:48:37', null, null);
INSERT INTO `users` VALUES ('6', 'admin6', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin6', '1', '1', '2012-09-25 09:48:53', null, null);
INSERT INTO `users` VALUES ('7', 'admin7', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin7', '1', '1', '2012-09-25 09:49:05', null, null);
INSERT INTO `users` VALUES ('8', 'admin8', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin8', '1', '1', '2012-09-25 09:49:19', null, null);
INSERT INTO `users` VALUES ('9', 'admin9', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin9', '1', '1', '2012-09-25 09:49:33', null, null);
INSERT INTO `users` VALUES ('11', 'admin11', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin11', '1', '1', '2012-09-25 09:50:05', null, null);
INSERT INTO `users` VALUES ('12', 'admin12', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin12', '1', '1', '2012-09-25 09:50:21', null, null);
INSERT INTO `users` VALUES ('13', 'admin13', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin13', '1', '1', '2012-09-25 09:50:38', null, null);
INSERT INTO `users` VALUES ('14', 'admin14', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin14', '1', '1', '2012-09-25 09:50:49', null, null);
INSERT INTO `users` VALUES ('15', 'admin15', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin15', '1', '1', '2012-09-25 09:51:01', null, null);
INSERT INTO `users` VALUES ('16', 'admin16', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin16', '1', '1', '2012-09-25 09:51:13', null, null);
INSERT INTO `users` VALUES ('17', 'admin17', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin17', '1', '1', '2012-09-25 09:51:24', null, null);
INSERT INTO `users` VALUES ('18', 'admin18', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin18', '1', '1', '2012-09-25 09:51:43', null, null);
INSERT INTO `users` VALUES ('19', 'admin19', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin19', '1', '1', '2012-09-25 09:51:55', null, null);
INSERT INTO `users` VALUES ('20', 'admin20', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin20', '1', '1', '2012-09-25 09:52:06', null, null);
INSERT INTO `users` VALUES ('21', 'admin21', 'd2518f1f1f61247581ea8e1a69785b3b', 'admin21', '1', '1', '2012-09-25 09:52:17', null, null);
