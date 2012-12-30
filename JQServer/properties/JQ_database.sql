/*
SQLyog Ultimate v8.32 
MySQL - 5.5.8 : Database - jq_database
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`jq_database` /*!40100 DEFAULT CHARACTER SET gb2312 */;

USE `jq_database`;

/*Table structure for table `leftinfo` */

DROP TABLE IF EXISTS `leftinfo`;

CREATE TABLE `leftinfo` (
  `IDto` varchar(10) NOT NULL,
  `content` varchar(1024) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312 COMMENT='JQ用户离线消息保存表';

/*Data for the table `leftinfo` */

/*Table structure for table `userfriends` */

DROP TABLE IF EXISTS `userfriends`;

CREATE TABLE `userfriends` (
  `userID` varchar(10) NOT NULL,
  `friendID` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gb2312 COMMENT='JQ用户好友信息';

/*Data for the table `userfriends` */

insert  into `userfriends`(`userID`,`friendID`) values ('1001','1000'),('1000','1001'),('1003','1004'),('1004','1003'),('1004','1003'),('1003','1004'),('1000','1002'),('1002','1000'),('1000','1005'),('1005','1000');

/*Table structure for table `userinfo` */

DROP TABLE IF EXISTS `userinfo`;

CREATE TABLE `userinfo` (
  `userID` varchar(10) NOT NULL,
  `nickname` varchar(20) DEFAULT NULL,
  `face` int(11) DEFAULT NULL,
  `sex` varchar(2) DEFAULT NULL,
  `remark` varchar(20) DEFAULT NULL,
  `age` int(11) DEFAULT NULL,
  `academy` varchar(20) DEFAULT NULL,
  `department` varchar(20) DEFAULT NULL,
  `registerEmail` varchar(25) DEFAULT NULL,
  `provence` varchar(20) DEFAULT NULL,
  `city` varchar(20) DEFAULT NULL,
  `email` varchar(25) DEFAULT NULL,
  `homePage` varchar(20) DEFAULT NULL,
  `description` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312 COMMENT='JQ用户详细资料';

/*Data for the table `userinfo` */

insert  into `userinfo`(`userID`,`nickname`,`face`,`sex`,`remark`,`age`,`academy`,`department`,`registerEmail`,`provence`,`city`,`email`,`homePage`,`description`) values ('1000','A',13,'男',NULL,22,'网络工程','网络工程','abc@163.com','浙江','杭州','i@laycher.com','杭电','修改签名啊'),('1001','B',15,NULL,NULL,20,NULL,NULL,'abc@163.com',NULL,NULL,NULL,NULL,NULL),('1002','C',15,'男',NULL,18,'','','abc@163.com','','','','','这次成功了八'),('1003','D',15,NULL,NULL,20,NULL,NULL,'abc@63.com',NULL,NULL,NULL,NULL,NULL),('1004','E',15,NULL,NULL,20,NULL,NULL,'abc@163.com',NULL,NULL,NULL,NULL,NULL),('1005','ds',15,NULL,NULL,20,NULL,NULL,'abc@163.com',NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `useripinfo` */

DROP TABLE IF EXISTS `useripinfo`;

CREATE TABLE `useripinfo` (
  `userID` varchar(10) NOT NULL,
  `IP` varchar(20) NOT NULL,
  `PORT` int(11) NOT NULL,
  `STATUS` int(11) NOT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312 COMMENT='用户登陆后存储用户当前IP地址,占用端口和状态信息.';

/*Data for the table `useripinfo` */

insert  into `useripinfo`(`userID`,`IP`,`PORT`,`STATUS`) values ('1000','169.254.2.36',8000,0),('1001','169.254.2.36',8000,0),('1002','192.168.21.129',8000,0),('1003','169.254.2.36',8000,0),('1004','169.254.2.36',8000,0),('1005','192.168.110.1',8000,0);

/*Table structure for table `userlogin` */

DROP TABLE IF EXISTS `userlogin`;

CREATE TABLE `userlogin` (
  `userID` varchar(10) NOT NULL,
  `password` varchar(32) NOT NULL,
  PRIMARY KEY (`userID`)
) ENGINE=InnoDB DEFAULT CHARSET=gb2312 COMMENT='JQ用户登陆账号密码存储表';

/*Data for the table `userlogin` */

insert  into `userlogin`(`userID`,`password`) values ('1000','E10ADC3949BA59ABBE56E057F20F883E'),('1001','E10ADC3949BA59ABBE56E057F20F883E'),('1002','E10ADC3949BA59ABBE56E057F20F883E'),('1003','123456'),('1004','123456'),('1005','E10ADC3949BA59ABBE56E057F20F883E');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
