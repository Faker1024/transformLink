package com.unnamed.transformLink.admin.test;

public class UserTableShardingTest {

    private static final String sql = """
            create table t_user_%d
            (
                id            bigint auto_increment comment 'ID'
                    primary key,
                username      varchar(256) null comment '用户名',
                password      varchar(512) null comment '密码',
                real_name     varchar(256) null comment '真实姓名',
                phone         varchar(128) null comment '手机号',
                mail          varchar(512) null comment '邮箱',
                deletion_time bigint       null comment '注销时间戳',
                create_time   datetime     null comment '创建时间',
                update_time   datetime     null comment '修改时间',
                del_flag      tinyint(1)   null comment '删除标识 0：未删除 1：已删除',
                constraint t_user_username_uindex
                    unique (username)
            )
                charset = utf8mb4;
                        
                        
            """;

    public static void main(String[] args) {
        for (int i = 0; i < 16; i++) {
            System.out.println(String.format(sql, i));
        }
    }
}
