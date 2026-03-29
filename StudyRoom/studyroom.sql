SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 用户表
-- ----------------------------
DROP TABLE IF EXISTS `appuser`;
CREATE TABLE `appuser` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户主键',
  `CreationTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `CreatorId` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `Password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '密码',
  `Name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `ImageUrls` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '头像',
  `RoleType` int(11) NULL DEFAULT NULL COMMENT '角色',
  `PhoneNumber` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号码',
  `Email` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
  `UserName` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号',
  `Birth` datetime NULL DEFAULT NULL COMMENT '出生年月',
  `OverdueTimes` int(11) NULL DEFAULT NULL COMMENT '逾期次数',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

INSERT INTO `appuser` VALUES (1, NOW(), 0, 'admin', '管理员', NULL, 1, '13800000000', 'admin@qq.com', 'admin', '2000-01-01 00:00:00', NULL);
INSERT INTO `appuser` VALUES (2, NOW(), 0, 'test0001', '测试用户', NULL, 2, '13800000001', 'test@qq.com', 'test0001', '2000-01-01 00:00:00', 0);

-- ----------------------------
-- 自习室表
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自习室主键',
  `CreationTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `CreatorId` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `Name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称',
  `Cover` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面',
  `Address` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
  `Content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '介绍',
  `EveryMonCancelCount` int(11) NULL DEFAULT NULL COMMENT '每月可取消次数',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `CreatorId`(`CreatorId`) USING BTREE,
  CONSTRAINT `room_ibfk_1` FOREIGN KEY (`CreatorId`) REFERENCES `appuser` (`Id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

INSERT INTO `room` VALUES (1, NOW(), 1, '静心自习室', NULL, '图书馆3楼A区', '<p>安静舒适的自习环境，配备空调和Wi-Fi</p>', 3);

-- ----------------------------
-- 座位表
-- ----------------------------
DROP TABLE IF EXISTS `seat`;
CREATE TABLE `seat` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '座位主键',
  `CreationTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `CreatorId` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `No` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '座位编号',
  `SRow` int(11) NULL DEFAULT NULL COMMENT '行',
  `SCol` int(11) NULL DEFAULT NULL COMMENT '列',
  `RoomId` int(11) NULL DEFAULT NULL COMMENT '自习室',
  `IsMaintain` int(11) NULL DEFAULT 0 COMMENT '是否维修中',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `CreatorId`(`CreatorId`) USING BTREE,
  INDEX `RoomId`(`RoomId`) USING BTREE,
  CONSTRAINT `seat_ibfk_1` FOREIGN KEY (`CreatorId`) REFERENCES `appuser` (`Id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `seat_ibfk_2` FOREIGN KEY (`RoomId`) REFERENCES `room` (`Id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- 预约记录表
-- ----------------------------
DROP TABLE IF EXISTS `appointrecord`;
CREATE TABLE `appointrecord` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '预约记录主键',
  `CreationTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `CreatorId` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `RoomId` int(11) NULL DEFAULT NULL COMMENT '自习室',
  `SeatId` int(11) NULL DEFAULT NULL COMMENT '座位',
  `UserId` int(11) NULL DEFAULT NULL COMMENT '预约人',
  `No` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '流水编号',
  `Phone` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
  `Name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '姓名',
  `BeginTime` datetime NULL DEFAULT NULL COMMENT '起始时间',
  `EndTime` datetime NULL DEFAULT NULL COMMENT '截至时间',
  `CommentScore` double(20, 5) NULL DEFAULT NULL COMMENT '评分',
  `SComment` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '评论',
  `AppointStatus` int(11) NULL DEFAULT NULL COMMENT '预约状态枚举',
  `AppointDateType` int(11) NULL DEFAULT NULL COMMENT '预约时间范围',
  `AppointDate` datetime NULL DEFAULT NULL COMMENT '预约日期',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `CreatorId`(`CreatorId`) USING BTREE,
  INDEX `UserId`(`UserId`) USING BTREE,
  INDEX `RoomId`(`RoomId`) USING BTREE,
  INDEX `SeatId`(`SeatId`) USING BTREE,
  CONSTRAINT `appointrecord_ibfk_1` FOREIGN KEY (`CreatorId`) REFERENCES `appuser` (`Id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `appointrecord_ibfk_2` FOREIGN KEY (`UserId`) REFERENCES `appuser` (`Id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `appointrecord_ibfk_3` FOREIGN KEY (`RoomId`) REFERENCES `room` (`Id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `appointrecord_ibfk_4` FOREIGN KEY (`SeatId`) REFERENCES `seat` (`Id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- 积分表
-- ----------------------------
DROP TABLE IF EXISTS `integral`;
CREATE TABLE `integral` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '积分主键',
  `CreationTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `CreatorId` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `Title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `UserId` int(11) NULL DEFAULT NULL COMMENT '用户',
  `IntegralValue` int(11) NULL DEFAULT NULL COMMENT '积分值',
  `Source` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '来源',
  `RelativeCode` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '关联编码',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `CreatorId`(`CreatorId`) USING BTREE,
  INDEX `UserId`(`UserId`) USING BTREE,
  CONSTRAINT `integral_ibfk_1` FOREIGN KEY (`CreatorId`) REFERENCES `appuser` (`Id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `integral_ibfk_2` FOREIGN KEY (`UserId`) REFERENCES `appuser` (`Id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- 轮播图表
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner` (
  `Id` int(11) NOT NULL AUTO_INCREMENT COMMENT '封面主键',
  `CreationTime` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `CreatorId` int(11) NULL DEFAULT NULL COMMENT '创建人',
  `Cover` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '封面',
  `Remark` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `CreatorId`(`CreatorId`) USING BTREE,
  CONSTRAINT `banner_ibfk_1` FOREIGN KEY (`CreatorId`) REFERENCES `appuser` (`Id`) ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
