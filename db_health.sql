/*
 Navicat Premium Data Transfer

 Source Server         : health
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : db_health

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 30/05/2025 11:09:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for doctors
-- ----------------------------
DROP TABLE IF EXISTS `doctors`;
CREATE TABLE `doctors`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '医生ID',
  `user_id` int NULL DEFAULT NULL COMMENT '关联用户ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '姓名',
  `department` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '科室',
  `gender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '职称',
  `hospital_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '医院名称',
  `introduction` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '医生简介',
  `education` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '教育背景',
  `certificates` json NULL COMMENT '资格证书列表（JSON数组）',
  `patient_ids` json NULL COMMENT '负责的患者ID列表（JSON数组）',
  `years_of_experience` int NOT NULL COMMENT '工作年限',
  `available` tinyint(1) NULL DEFAULT 1 COMMENT '是否接诊',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_userid_doctors`(`user_id`) USING BTREE,
  CONSTRAINT `fk_userid_doctors` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of doctors
-- ----------------------------
INSERT INTO `doctors` VALUES (37, NULL, '医生1', '外科', '男', '主治医师', '北京大学第一医院', '从事外科临床工作6年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[\"68121be8d4633f7a90142dcb\", \"68121b6606f6ed650d6b35c4\", \"681825e7166354369c8fa96b\"]', 6, 0, '2025-04-30 12:45:19', '2025-05-18 02:06:45');
INSERT INTO `doctors` VALUES (38, NULL, '医生2', '儿科', NULL, '副主任医师', '北京大学人民医院', '从事儿科临床工作7年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[\"682a0c95ddb17b5eb2f375f1\"]', 7, 0, '2025-04-30 12:45:19', '2025-05-18 16:36:57');
INSERT INTO `doctors` VALUES (39, NULL, '医生3', '妇产科', NULL, '主任医师', '北京大学第三医院', '从事妇产科临床工作8年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 8, 0, '2025-04-30 12:45:20', '2025-04-30 12:45:20');
INSERT INTO `doctors` VALUES (40, NULL, '医生4', '眼科', NULL, '住院医师', '北京友谊医院', '从事眼科临床工作9年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 9, 0, '2025-04-30 12:45:20', '2025-04-30 12:45:20');
INSERT INTO `doctors` VALUES (41, NULL, '医生5', '耳鼻喉科', NULL, '主治医师', '北京朝阳医院', '从事耳鼻喉科临床工作10年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 10, 0, '2025-04-30 12:45:20', '2025-04-30 12:45:20');
INSERT INTO `doctors` VALUES (42, NULL, '医生6', '口腔科', NULL, '副主任医师', '北京天坛医院', '从事口腔科临床工作11年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 11, 0, '2025-04-30 12:45:20', '2025-04-30 12:45:20');
INSERT INTO `doctors` VALUES (43, NULL, '医生7', '皮肤科', NULL, '主任医师', '北京安贞医院', '从事皮肤科临床工作12年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 12, 0, '2025-04-30 12:45:20', '2025-04-30 12:45:20');
INSERT INTO `doctors` VALUES (44, NULL, '医生8', '中医科', NULL, '住院医师', '北京儿童医院', '从事中医科临床工作13年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 13, 0, '2025-04-30 12:45:21', '2025-04-30 12:45:21');
INSERT INTO `doctors` VALUES (45, NULL, '医生9', '康复科', NULL, '主治医师', '北京妇产医院', '从事康复科临床工作14年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[\"681825e7166354369c8fa96b\"]', 14, 0, '2025-04-30 12:45:21', '2025-05-09 11:31:00');
INSERT INTO `doctors` VALUES (46, NULL, '医生10', '急诊科', NULL, '副主任医师', '北京中医医院', '从事急诊科临床工作15年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 15, 0, '2025-04-30 12:45:21', '2025-04-30 12:45:21');
INSERT INTO `doctors` VALUES (47, NULL, '医生11', '麻醉科', NULL, '主任医师', '北京肿瘤医院', '从事麻醉科临床工作16年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 16, 0, '2025-04-30 12:45:21', '2025-04-30 12:45:21');
INSERT INTO `doctors` VALUES (48, NULL, '医生12', '放射科', NULL, '住院医师', '北京协和医院', '从事放射科临床工作17年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 17, 0, '2025-04-30 12:45:21', '2025-04-30 12:45:21');
INSERT INTO `doctors` VALUES (49, NULL, '医生13', '内科', NULL, '主治医师', '北京大学第一医院', '从事内科临床工作18年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 18, 0, '2025-04-30 12:45:21', '2025-04-30 12:45:21');
INSERT INTO `doctors` VALUES (50, NULL, '医生14', '外科', NULL, '副主任医师', '北京大学人民医院', '从事外科临床工作19年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 19, 0, '2025-04-30 12:45:22', '2025-04-30 12:45:22');
INSERT INTO `doctors` VALUES (51, NULL, '医生15', '儿科', NULL, '主任医师', '北京大学第三医院', '从事儿科临床工作20年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 20, 0, '2025-04-30 12:45:22', '2025-04-30 12:45:22');
INSERT INTO `doctors` VALUES (52, NULL, '医生16', '妇产科', NULL, '住院医师', '北京友谊医院', '从事妇产科临床工作21年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 21, 0, '2025-04-30 12:45:22', '2025-04-30 12:45:22');
INSERT INTO `doctors` VALUES (53, NULL, '医生17', '眼科', NULL, '主治医师', '北京朝阳医院', '从事眼科临床工作22年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 22, 0, '2025-04-30 12:45:22', '2025-04-30 12:45:22');
INSERT INTO `doctors` VALUES (54, NULL, '医生18', '耳鼻喉科', NULL, '副主任医师', '北京天坛医院', '从事耳鼻喉科临床工作23年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 23, 0, '2025-04-30 12:45:22', '2025-04-30 12:45:22');
INSERT INTO `doctors` VALUES (55, NULL, '医生19', '口腔科', NULL, '主任医师', '北京安贞医院', '从事口腔科临床工作24年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 24, 0, '2025-04-30 12:45:23', '2025-04-30 12:45:23');
INSERT INTO `doctors` VALUES (56, NULL, '医生20', '皮肤科', NULL, '住院医师', '北京儿童医院', '从事皮肤科临床工作5年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 5, 0, '2025-04-30 12:45:23', '2025-04-30 12:45:23');
INSERT INTO `doctors` VALUES (57, NULL, '医生21', '中医科', NULL, '主治医师', '北京妇产医院', '从事中医科临床工作6年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 6, 0, '2025-04-30 12:45:23', '2025-04-30 12:45:23');
INSERT INTO `doctors` VALUES (58, NULL, '医生22', '康复科', NULL, '副主任医师', '北京中医医院', '从事康复科临床工作7年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 7, 0, '2025-04-30 12:45:23', '2025-04-30 12:45:23');
INSERT INTO `doctors` VALUES (59, NULL, '医生23', '急诊科', NULL, '主任医师', '北京肿瘤医院', '从事急诊科临床工作8年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 8, 0, '2025-04-30 12:45:24', '2025-04-30 12:45:24');
INSERT INTO `doctors` VALUES (60, NULL, '医生24', '麻醉科', NULL, '住院医师', '北京协和医院', '从事麻醉科临床工作9年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 9, 0, '2025-04-30 12:45:24', '2025-04-30 12:45:24');
INSERT INTO `doctors` VALUES (61, NULL, '医生25', '放射科', NULL, '主治医师', '北京大学第一医院', '从事放射科临床工作10年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 10, 0, '2025-04-30 12:45:24', '2025-04-30 12:45:24');
INSERT INTO `doctors` VALUES (62, NULL, '医生26', '内科', NULL, '副主任医师', '北京大学人民医院', '从事内科临床工作11年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 11, 0, '2025-04-30 12:45:24', '2025-04-30 12:45:24');
INSERT INTO `doctors` VALUES (63, NULL, '医生27', '外科', NULL, '主任医师', '北京大学第三医院', '从事外科临床工作12年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 12, 0, '2025-04-30 12:45:24', '2025-04-30 12:45:24');
INSERT INTO `doctors` VALUES (64, NULL, '医生28', '儿科', NULL, '住院医师', '北京友谊医院', '从事儿科临床工作13年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 13, 0, '2025-04-30 12:45:24', '2025-04-30 12:45:24');
INSERT INTO `doctors` VALUES (65, NULL, '医生29', '妇产科', NULL, '主治医师', '北京朝阳医院', '从事妇产科临床工作14年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 14, 0, '2025-04-30 12:45:25', '2025-04-30 12:45:25');
INSERT INTO `doctors` VALUES (66, NULL, '医生30', '眼科', NULL, '副主任医师', '北京天坛医院', '从事眼科临床工作15年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 15, 0, '2025-04-30 12:45:25', '2025-04-30 12:45:25');
INSERT INTO `doctors` VALUES (67, NULL, '医生31', '耳鼻喉科', NULL, '主任医师', '北京安贞医院', '从事耳鼻喉科临床工作16年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 16, 0, '2025-04-30 12:45:25', '2025-04-30 12:45:25');
INSERT INTO `doctors` VALUES (68, NULL, '医生32', '口腔科', NULL, '住院医师', '北京儿童医院', '从事口腔科临床工作17年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 17, 0, '2025-04-30 12:45:25', '2025-04-30 12:45:25');
INSERT INTO `doctors` VALUES (69, NULL, '医生33', '皮肤科', NULL, '主治医师', '北京妇产医院', '从事皮肤科临床工作18年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 18, 0, '2025-04-30 12:45:25', '2025-04-30 12:45:25');
INSERT INTO `doctors` VALUES (70, NULL, '医生34', '中医科', NULL, '副主任医师', '北京中医医院', '从事中医科临床工作19年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 19, 0, '2025-04-30 12:45:26', '2025-04-30 12:45:26');
INSERT INTO `doctors` VALUES (71, NULL, '医生35', '康复科', NULL, '主任医师', '北京肿瘤医院', '从事康复科临床工作20年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 20, 0, '2025-04-30 12:45:26', '2025-04-30 12:45:26');
INSERT INTO `doctors` VALUES (72, NULL, '医生36', '急诊科', NULL, '住院医师', '北京协和医院', '从事急诊科临床工作21年，具有丰富的临床经验。', '医学博士', '[\"执业医师资格证\", \"医师执业证\"]', '[]', 21, 0, '2025-04-30 12:45:26', '2025-04-30 12:45:26');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '真实姓名',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码（BCrypt加密）',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '手机号',
  `roles` json NOT NULL COMMENT '角色列表（JSON数组）',
  `active` tinyint(1) NULL DEFAULT 1 COMMENT '是否激活',
  `created_at` datetime NOT NULL COMMENT '创建时间',
  `updated_at` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 167 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (78, 'admin', '系统管理员', '$2a$10$5kxU2pi4ZK.VJi4stQ3A5uqiiwVcfhQ8qdoVpa.IgCgprfxHrkNc2', 'admin@example.com', '13800000000', '[\"ROLE_ADMIN\"]', 1, '2025-04-30 12:45:18', '2025-04-30 12:45:18');
INSERT INTO `users` VALUES (79, 'doctor1', '医生1', '$2a$10$Yhu0dDasczT9OIQGgci4.uIOQqCkbUvLxKjw3XZtxzUKnMjpZvgTa', 'doctor1@example.com', '13800000001', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:19', '2025-05-22 16:16:19');
INSERT INTO `users` VALUES (80, 'doctor2', '医生2', '$2a$10$y7JkPpD3mWqkKmuDozTHh.OcJ2dOEALeth.ol4LvgW1mV9oU5VCCq', 'doctor2@example.com', '13800000002', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:19', '2025-05-18 16:29:39');
INSERT INTO `users` VALUES (81, 'doctor3', '医生3', '$2a$10$SMdRLf/r/cF1SKQW9lo7yuyqC9u5MD/7o6VglBKnrMwOjYzJI6nqK', 'doctor3@example.com', '13800000003', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:19', '2025-04-30 12:45:19');
INSERT INTO `users` VALUES (82, 'doctor4', '医生4', '$2a$10$tWfP7wMt995SoI3lPlANLefxq..owxx7jAYv4kZfFum7TWbAa6ZUK', 'doctor4@example.com', '13800000004', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:20', '2025-04-30 12:45:20');
INSERT INTO `users` VALUES (83, 'doctor5', '医生5', '$2a$10$jZg0ZHuJAIVuH6JHiTSA5erH6p3pJs8T2.irQM8m72qSswYynoxMG', 'doctor5@example.com', '13800000005', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:20', '2025-04-30 12:45:20');
INSERT INTO `users` VALUES (84, 'doctor6', '医生6', '$2a$10$vvOHvDuerhF3HxXqL2EMvu5z7xr3hXcWFf73L4.sS3FayJDMOTDra', 'doctor6@example.com', '13800000006', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:20', '2025-04-30 12:45:20');
INSERT INTO `users` VALUES (85, 'doctor7', '医生7', '$2a$10$vNPEkN1O/zURnjhb1vqu4.c3HYSeGEn7DBu9AmPp81o0HtLLR2kBG', 'doctor7@example.com', '13800000007', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:20', '2025-04-30 12:45:20');
INSERT INTO `users` VALUES (86, 'doctor8', '医生8', '$2a$10$dngJ8JNbWI00C7UMDRO3suNPuzdrCdM7rZ6AsAdcVJkIPqtmwB8uC', 'doctor8@example.com', '13800000008', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:20', '2025-04-30 12:45:20');
INSERT INTO `users` VALUES (87, 'doctor9', '医生9', '$2a$10$7IZ.OzMPKqMAmB5/QKFYs.FpS5Iw.0bOFYQIl4r87E0UW8v7/hsHi', 'doctor9@example.com', '13800000009', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:21', '2025-04-30 12:45:21');
INSERT INTO `users` VALUES (88, 'doctor10', '医生10', '$2a$10$ZcrFLNrDmfKLOv6.Ey1ZoOvW8ESv/v3EzUdQ.FxePMtOWI/9aVWja', 'doctor10@example.com', '13800000010', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:21', '2025-04-30 12:45:21');
INSERT INTO `users` VALUES (89, 'doctor11', '医生11', '$2a$10$VLv0bmsuMByw2ZguvSoKa.IxlRwa.BUyWeh.JWeZjsWwiUhMBoUzO', 'doctor11@example.com', '13800000011', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:21', '2025-04-30 12:45:21');
INSERT INTO `users` VALUES (90, 'doctor12', '医生12', '$2a$10$9YKoe5LPJ3MMxE6.Ic7VnuQoHuYX4n1UgGLU038GRY5Ptoa5m0O0m', 'doctor12@example.com', '13800000012', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:21', '2025-04-30 12:45:21');
INSERT INTO `users` VALUES (91, 'doctor13', '医生13', '$2a$10$LrQhvSPLqoUefCpFcq4.m.feijhqmPPRIURvLy.4XmhCA4RLr6zL6', 'doctor13@example.com', '13800000013', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:21', '2025-04-30 12:45:21');
INSERT INTO `users` VALUES (92, 'doctor14', '医生14', '$2a$10$5J.etUPuxqF/X2c2YxESWur6Iil6SViOdwonvNHqyF5JIBjeRReCe', 'doctor14@example.com', '13800000014', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:22', '2025-04-30 12:45:22');
INSERT INTO `users` VALUES (93, 'doctor15', '医生15', '$2a$10$stc9WY9JJlBvhbSBamE/GuOBH209f5wlT2sfSWiKXBxbIIc3/MSHC', 'doctor15@example.com', '13800000015', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:22', '2025-04-30 12:45:22');
INSERT INTO `users` VALUES (94, 'doctor16', '医生16', '$2a$10$SCOOV9RbpmTD8/Nky2duJ.LrujSmJerUiEYIp/SxT6bElmOUhzJja', 'doctor16@example.com', '13800000016', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:22', '2025-04-30 12:45:22');
INSERT INTO `users` VALUES (95, 'doctor17', '医生17', '$2a$10$V1foEqrMPkMyqpjRPUK9ne/avr78G/cnV91gs3D08aJvJaNskV6tK', 'doctor17@example.com', '13800000017', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:22', '2025-04-30 12:45:22');
INSERT INTO `users` VALUES (96, 'doctor18', '医生18', '$2a$10$bnLyVE22dVsu96614hzl5udKEPwGqDySPyN7VxXfe87obpB29tvFS', 'doctor18@example.com', '13800000018', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:22', '2025-04-30 12:45:22');
INSERT INTO `users` VALUES (97, 'doctor19', '医生19', '$2a$10$vJD3KlPSkC4vnZKNubk68u9XdqrgRIbPjyEHApqjpb18zphmBP/n.', 'doctor19@example.com', '13800000019', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:23', '2025-04-30 12:45:23');
INSERT INTO `users` VALUES (98, 'doctor20', '医生20', '$2a$10$y86fIFsDNtpOvMNyizwZl.jM0l8AIOKppSGP6dlUFM8MI0ypvNhU6', 'doctor20@example.com', '13800000020', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:23', '2025-04-30 12:45:23');
INSERT INTO `users` VALUES (99, 'doctor21', '医生21', '$2a$10$Awz/ORUwd/4fqRNh5keAveVNT/OQcYBfLTI9oPusnu7LQFGaC/Y/y', 'doctor21@example.com', '13800000021', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:23', '2025-04-30 12:45:23');
INSERT INTO `users` VALUES (100, 'doctor22', '医生22', '$2a$10$msbORI1KfacXH8xU6FNLUenlyiX9yjJqGMxTfU8Tg3hLoIjEIKrbm', 'doctor22@example.com', '13800000022', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:23', '2025-04-30 12:45:23');
INSERT INTO `users` VALUES (101, 'doctor23', '医生23', '$2a$10$siloPo2py1h1xWPRMrRAOuk7DscbYx4KsnlBeK2bgs.NL/HYAeQiy', 'doctor23@example.com', '13800000023', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:23', '2025-04-30 12:45:23');
INSERT INTO `users` VALUES (102, 'doctor24', '医生24', '$2a$10$VacaoOlfFKsVdksWNfm2Uu3wO57h8iJZAmoO2I2kfzrA.Xe7CGZZq', 'doctor24@example.com', '13800000024', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:24', '2025-04-30 12:45:24');
INSERT INTO `users` VALUES (103, 'doctor25', '医生25', '$2a$10$nzdc/vQIMN40Hcoee/0E..yX05JTHFLOkOIgNTjS5UaT.8RNECQ/2', 'doctor25@example.com', '13800000025', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:24', '2025-04-30 12:45:24');
INSERT INTO `users` VALUES (104, 'doctor26', '医生26', '$2a$10$8JA6cw/8R7QKqSnbuwrSdOm0Ym5m424X8M8XxJ6kgcsjrYzxKOKxa', 'doctor26@example.com', '13800000026', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:24', '2025-04-30 12:45:24');
INSERT INTO `users` VALUES (105, 'doctor27', '医生27', '$2a$10$rHvjrBys0L1SNwNAB2OgIux75W.JkIb1BTjnehn5jzq5rp1Jfqsni', 'doctor27@example.com', '13800000027', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:24', '2025-04-30 12:45:24');
INSERT INTO `users` VALUES (106, 'doctor28', '医生28', '$2a$10$LcbhIvykH.5SrGlHP3khneN65YCaWEjkZX6nmmjYq2RdrFto/woa2', 'doctor28@example.com', '13800000028', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:24', '2025-04-30 12:45:24');
INSERT INTO `users` VALUES (107, 'doctor29', '医生29', '$2a$10$D6VG4UvjsvbW1CkHWayWpuY9Z2aKzBM2xg5yuSvoEfc7AP3bNxvH2', 'doctor29@example.com', '13800000029', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:25', '2025-04-30 12:45:25');
INSERT INTO `users` VALUES (108, 'doctor30', '医生30', '$2a$10$tIB5kG4HF6yL0rJtwwx.CecN8Q1swG.3aOGNtVBSWOLnfTYnZvRnu', 'doctor30@example.com', '13800000030', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:25', '2025-04-30 12:45:25');
INSERT INTO `users` VALUES (109, 'doctor31', '医生31', '$2a$10$r8YR7Tq1ODXYgAvSTxZXn.SuX/TYCC9Mvko1wobjjBviP0IFPNtmy', 'doctor31@example.com', '13800000031', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:25', '2025-04-30 12:45:25');
INSERT INTO `users` VALUES (110, 'doctor32', '医生32', '$2a$10$ouWUwXROjx11ZaZfadfOIujDc1eoRk7pjNgflBEeKXj0C6xWB3am.', 'doctor32@example.com', '13800000032', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:25', '2025-04-30 12:45:25');
INSERT INTO `users` VALUES (111, 'doctor33', '医生33', '$2a$10$FReaAg2C7D60s7RQmzN1AOSefpJEJuoE9teiyt82AAzWOXDYQtSF6', 'doctor33@example.com', '13800000033', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:25', '2025-04-30 12:45:25');
INSERT INTO `users` VALUES (112, 'doctor34', '医生34', '$2a$10$zeEf4P3.0Z50ku32fOK2A.aGwuadRcCwqzH4pXGBkpGExpuIrhCja', 'doctor34@example.com', '13800000034', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:26', '2025-04-30 12:45:26');
INSERT INTO `users` VALUES (113, 'doctor35', '医生35', '$2a$10$0ZyT.RWN7Wa4X/7UMZQODe/AQLiLDApeNoAh31qm7mMhgk2Ldctxm', 'doctor35@example.com', '13800000035', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:26', '2025-04-30 12:45:26');
INSERT INTO `users` VALUES (114, 'doctor36', '医生36', '$2a$10$tUBNLwszUjx9RKE9zPtsYOT8UMQhFh8pIt8mYIvAZqxZIDpxPX7gW', 'doctor36@example.com', '13800000036', '[\"ROLE_DOCTOR\"]', 1, '2025-04-30 12:45:26', '2025-04-30 12:45:26');
INSERT INTO `users` VALUES (115, 'user1', '患者1', '$2a$10$cQE0cG6kMrqieQMbI8QpZemWNVizqbTtCBvUVEQpqgsax38pyht9W', 'user1@example.com', '13900000001', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:26', '2025-05-18 02:03:37');
INSERT INTO `users` VALUES (116, 'user2', '患者2', '$2a$10$65lCccLNG9.0K1HjBTsLNuUOhEqS.Y1HCXYqk5yMDMcP7Xa85dSg2', 'user2@example.com', '13900000002', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:26', '2025-04-30 12:45:26');
INSERT INTO `users` VALUES (117, 'user3', '患者3', '$2a$10$kHvzsHzNmCFG5oxcaf8R4el6dyoWp/FZhGF4DdyG3uYUIsOE9rIxa', 'user3@example.com', '13900000003', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:27', '2025-04-30 12:45:27');
INSERT INTO `users` VALUES (118, 'user4', '患者4', '$2a$10$3fsWoft4cJ/XMcAhL4FEoeeV00wVFvF5fixj3xRZ2SoVOHO0EGJ5y', 'user4@example.com', '13900000004', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:27', '2025-04-30 12:45:27');
INSERT INTO `users` VALUES (119, 'user5', '患者5', '$2a$10$v1DToMm/hdiGExXqwTAtVuRVORL3Aw6wau5vc8KaSv8AuKZkJflrC', 'user5@example.com', '13900000005', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:27', '2025-04-30 12:45:27');
INSERT INTO `users` VALUES (120, 'user7', '患者7', '$2a$10$1jNi.US6oGRSykNJobP7weuGg.OF.EUiv1Kh.n00AZgH3.RL4OcPa', 'user7@example.com', '13900000007', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:27', '2025-04-30 12:45:27');
INSERT INTO `users` VALUES (121, 'user8', '患者8', '$2a$10$H4mRv3pNtjaXZIPKtGeEeux2FrgPZbh04tMgl2EjMsq0XNc6agM/2', 'user8@example.com', '13900000008', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:28', '2025-04-30 12:45:28');
INSERT INTO `users` VALUES (122, 'user9', '患者9', '$2a$10$hp9zjavXmKiYGYPtPCgFMujb0NtCzjFzgG0DZ.bpNtXzSV4LYjqju', 'user9@example.com', '13900000009', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:28', '2025-04-30 12:45:28');
INSERT INTO `users` VALUES (123, 'user10', '患者10', '$2a$10$Xgm1.bTjPt6Z1.jy9xwBFO.5CSF4e.CbYXsMnu2H/glT.aGvl9o0S', 'user10@example.com', '13900000010', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:28', '2025-04-30 12:45:28');
INSERT INTO `users` VALUES (124, 'user11', '患者11', '$2a$10$mOBv1Xo1RsWVWOmBvS/38uRztl2dQGw4YCour5KlFgOUj.BnuwrAu', 'user11@example.com', '13900000011', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:29', '2025-04-30 12:45:29');
INSERT INTO `users` VALUES (125, 'user12', '患者12', '$2a$10$8akJ.PP5u273DjxFYoafpu/EcmexYuzYdpwhOxWeP4twwyogpmc4.', 'user12@example.com', '13900000012', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:29', '2025-04-30 12:45:29');
INSERT INTO `users` VALUES (126, 'user13', '患者13', '$2a$10$QwHWuYqxVMAST7n1RYtNrOLhJxVO891LaFByUtTg.gdaF7UVwv2QC', 'user13@example.com', '13900000013', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:29', '2025-04-30 12:45:29');
INSERT INTO `users` VALUES (127, 'user14', '患者14', '$2a$10$FDMwZNbkk8pBzRX8W3kyyORc5EhdjdWaT2iFpvdIu8k0.hkQ0ocVO', 'user14@example.com', '13900000014', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:29', '2025-04-30 12:45:29');
INSERT INTO `users` VALUES (128, 'user15', '患者15', '$2a$10$laH0eZXfEl/HEtRBrDlcG.oI0xYlfwETW99qjyoK/GO42ArCmvThS', 'user15@example.com', '13900000015', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:29', '2025-04-30 12:45:29');
INSERT INTO `users` VALUES (129, 'user16', '患者16', '$2a$10$hGW7mLLzu2McsuXIaD0ywOcuGDtQvzh1xXqxlNhCDD0NdbYK58S32', 'user16@example.com', '13900000016', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:29', '2025-04-30 12:45:29');
INSERT INTO `users` VALUES (130, 'user17', '患者17', '$2a$10$ShxCcrb/u/vWImKgVuz2BuvPU0hMfZZJdlD9hvArtX14ktaYGxbRe', 'user17@example.com', '13900000017', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:30', '2025-04-30 12:45:30');
INSERT INTO `users` VALUES (131, 'user18', '患者18', '$2a$10$bkf5RX2RIgQDSUjV0UK.FOLT7c7oiie1B0ShLD6WM5VfNiAYKyNny', 'user18@example.com', '13900000018', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:30', '2025-04-30 12:45:30');
INSERT INTO `users` VALUES (132, 'user19', '患者19', '$2a$10$h1f9sMVMOzZUCns.YN3skOQMDIPLbNkzgOxwFFg3ZmZyFQatT7YM6', 'user19@example.com', '13900000019', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:30', '2025-04-30 12:45:30');
INSERT INTO `users` VALUES (133, 'user20', '患者20', '$2a$10$0vG5rq0mmu6l6cz5ZtqYCe9.3U2pP3OFSpIAbNH9Ff5KcEbvXGWPG', 'user20@example.com', '13900000020', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:30', '2025-04-30 12:45:30');
INSERT INTO `users` VALUES (134, 'user21', '患者21', '$2a$10$bcZvVlMAbfDEyggBFdML7.QTQCE8Cvrg5W1ieYPxshCzCTjGaq00O', 'user21@example.com', '13900000021', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:30', '2025-04-30 12:45:30');
INSERT INTO `users` VALUES (135, 'user22', '患者22', '$2a$10$7wSzqhwspxgZUnA98V5/h.Ig4ZEgWJa/sqfimMiHG9cTStOeC1lee', 'user22@example.com', '13900000022', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:30', '2025-04-30 12:45:30');
INSERT INTO `users` VALUES (136, 'user23', '患者23', '$2a$10$mg1f76wKeJ0AsKNuyicBv.QYF2Cjm5XNi2ARRQLHBlWLnwXxE86by', 'user23@example.com', '13900000023', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:31', '2025-04-30 12:45:31');
INSERT INTO `users` VALUES (137, 'user24', '患者24', '$2a$10$qQbLR.nZW3MkMia0J0ZeM.hTXnzYmPQmwWGl5OueYzaGlbF/S4wFW', 'user24@example.com', '13900000024', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:31', '2025-04-30 12:45:31');
INSERT INTO `users` VALUES (138, 'user25', '患者25', '$2a$10$ctvIxaZZDwvCwgIeXVbgZeK.sqG9QVuI4b1CBJCmyISpWBeSiWLqO', 'user25@example.com', '13900000025', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:31', '2025-04-30 12:45:31');
INSERT INTO `users` VALUES (139, 'user26', '患者26', '$2a$10$Ob/FR.Gv19kStbs.qeNdfuGG0/pQ5kDR2GWvleQ/7.4EEQE.TuEb.', 'user26@example.com', '13900000026', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:31', '2025-04-30 12:45:31');
INSERT INTO `users` VALUES (140, 'user27', '患者27', '$2a$10$y0tCQAYIjhnhwXGEgb2UDe2nS38Y3a5/CDnJslOEJyuHKNu52MOIe', 'user27@example.com', '13900000027', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:31', '2025-04-30 12:45:31');
INSERT INTO `users` VALUES (141, 'user28', '患者28', '$2a$10$fX/r.J.h5qE3o4P/6ckALOYtVSmr.uOu3id2.DOZfxNUouc4W.Pru', 'user28@example.com', '13900000028', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:31', '2025-04-30 12:45:31');
INSERT INTO `users` VALUES (142, 'user29', '患者29', '$2a$10$eGebFTLFXgJ8xYq02E65n.Zm8kMk2qlf4ZH7CC8J6vl5AapNZOpEq', 'user29@example.com', '13900000029', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:32', '2025-04-30 12:45:32');
INSERT INTO `users` VALUES (143, 'user30', '患者30', '$2a$10$boXO2jHcVYyywycsaB2V5u2E9WHTt2mxygC74L7QfOuioJZim27Gy', 'user30@example.com', '13900000030', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:32', '2025-04-30 12:45:32');
INSERT INTO `users` VALUES (144, 'user31', '患者31', '$2a$10$C2OBSxs5Y0ddN3Qm6HDWxuVV.dAY8rDCR6o9RjMenBWyhXVtHgCQW', 'user31@example.com', '13900000031', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:32', '2025-04-30 12:45:32');
INSERT INTO `users` VALUES (145, 'user32', '患者32', '$2a$10$EvzCNxEl9mTxmdsQJwFTWuxuaYBG72tOlKYBh7v94Kz4Y7Eb5bCem', 'user32@example.com', '13900000032', '[\"ROLE_USER\"]', 1, '2025-04-30 12:45:32', '2025-04-30 12:45:32');
INSERT INTO `users` VALUES (146, 'topcx', '陈迅', '$2a$10$an2M8IF5XBPXNsQmGxj/xeAtAjboQIUZzIWCE3fUdUFl6eGcawAOa', '1677940751@qq.com', '18351589652', '[\"ROLE_USER\"]', 1, '2025-04-30 12:47:37', '2025-05-14 15:09:05');
INSERT INTO `users` VALUES (147, 'naln', '纳兰医生', '$2a$10$tks.3a8Baf0saaAViXotSuvNecZlFpfcDPo9VFrz8J04auRWMGeyi', NULL, '13122222222', '[\"ROLE_USER\"]', 1, '2025-05-02 10:48:33', '2025-05-02 10:48:33');
INSERT INTO `users` VALUES (148, 'q1w2e3', '大法师', '$2a$10$ZlDbLvF04Up3LtouIW3q6.njsPAzqgZzFN7/JZ6t.oNSrmqLs9z7C', NULL, '13400033333', '[\"ROLE_USER\"]', 1, '2025-05-05 02:43:52', '2025-05-18 02:06:45');
INSERT INTO `users` VALUES (149, 'zxy', '午夜心碎小熊', '$2a$10$IxeWCvBVtJYW8cVELAi80uocY..wJ0AUwOCOwkdYwQM2/yVn8P.jC', NULL, '13400000007', '[\"ROLE_USER\"]', 1, '2025-05-18 16:36:38', '2025-05-18 16:39:27');
INSERT INTO `users` VALUES (157, 'doctor陈', '陈奕迅', '$2a$10$cZHsRnc87arFejLg5ByWbext2iOYJHp9oPBPTxnGxheOrOgnVnx3O', '1677940752@qq.com', '18351589653', '[\"ROLE_USER\"]', 1, '2025-05-19 12:43:01', '2025-05-19 12:45:03');
INSERT INTO `users` VALUES (164, 'gzw', '骨折为', '$2a$10$ZBrGSolwt0koCX17bRmFDe/ei9jvfSJ8mrPVDKRzddU1ZbbeSSdH2', NULL, '16351589652', '[\"ROLE_USER\"]', 1, '2025-05-20 01:46:13', '2025-05-20 01:46:13');
INSERT INTO `users` VALUES (165, 'wk1', '王凯', '$2a$10$4CVaXia03PJiI4VgHwUMuuUdmCzBlsOU.WhvZS32jJaj25furPQIa', NULL, '13600037045', '[\"ROLE_USER\"]', 1, '2025-05-22 02:19:50', '2025-05-22 02:19:50');
INSERT INTO `users` VALUES (166, '王凯doctor', '王凯', '$2a$10$pBiVGonVfm.Xr4v797y4zOagrXdx43zRG9Ru3AazNIiHh7DJFwvmW', '1577980651@qq.com', '16451589652', '[\"ROLE_USER\"]', 1, '2025-05-22 14:54:21', '2025-05-22 14:54:21');
INSERT INTO `users` VALUES (167, 'lyc', '路议程', '$2a$10$dOV0BrA8zVD6UnO6CdLQ3OU0Q8npGzhLY.IXE7fGXqkN7uNzIPjAW', '1677940756@qq.com', '19351589652', '[\"ROLE_USER\"]', 1, '2025-05-19 17:36:37', '2025-05-22 07:24:19');

SET FOREIGN_KEY_CHECKS = 1;
