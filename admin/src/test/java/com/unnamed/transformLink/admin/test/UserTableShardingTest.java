package com.unnamed.transformLink.admin.test;

public class UserTableShardingTest {

    private static final String sql = """
CREATE TABLE `t_link_%d` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `domain` varchar(128) DEFAULT NULL COMMENT '域名',
  `short_uri` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `full_short_url` varchar(128) DEFAULT NULL COMMENT '完整短链接',
  `origin_url` varchar(1024) DEFAULT NULL COMMENT '原始链接',
  `click_num` int DEFAULT '0' COMMENT '点击量',
  `gid` varchar(32) DEFAULT NULL COMMENT '分组标识',
  `enable_status` tinyint(1) DEFAULT NULL COMMENT '启用标识 0：未启用 1：已启用',
  `created_type` tinyint(1) DEFAULT NULL COMMENT '创建类型 0：控制台 1：接口',
  `valid_date_type` tinyint(1) DEFAULT NULL COMMENT '有效期类型 0：永久有效 1：用户自定义',
  `valid_date` datetime DEFAULT NULL COMMENT '有效期',
  `describe` varchar(1024) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `del_flag` tinyint(1) DEFAULT NULL COMMENT '删除标识 0：未删除 1：已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_unique_full_short_uri` (`full_short_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1802228980586303490 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
                        
            """;

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.println(String.format(sql, i));
        }
    }
}
