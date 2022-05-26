insert t_user(name) value ("test3");

show  variables like '%binlog_format%';
show  variables like '%log_bin%';

## --还是声明分隔符 --若存储过程存在，则删除
## --创建存储过程，参数为插入数据条数n
##

DELIMITER //
DROP PROCEDURE IF EXISTS `auto_gen_record`;
CREATE PROCEDURE `auto_gen_record`(in n INT)
BEGIN
  DECLARE i INT DEFAULT 0;
  WHILE i < n DO
    insert t_user(user_no,name,addr) value (UUID(),CONCAT("name",uuid_short()),CONCAT("addr",uuid_short()));
    SET i = i + 1;
  END WHILE;
END //

CALL auto_gen_record(10);

show create table t_user;
show create table t_user_addr;
show create table t_user_info;
show index from t_user;
show index from t_user_addr;
show index from t_user_info;


SHOW VARIABLES LIKE 'event_scheduler';
SET GLOBAL event_scheduler = ON;

select count(*) from t_user;
select * from t_user;


create event auto_gen_record_event on schedule every 30 second do call auto_gen_record(1);

SELECT event_name,event_definition,interval_value,interval_field,status FROM information_schema.EVENTS;


-- 删除事件
DROP EVENT IF EXISTS auto_gen_record_event;
alter event auto_gen_record_event on completion preserve disable; -- 关闭定时任务
alter event auto_gen_record_event on completion preserve enable; -- 开启定时任务


CREATE TABLE `t_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_no` varchar(128) NOT NULL COMMENT '用户号',
  `name` varchar(128) NOT NULL COMMENT '名称',
  `addr` varchar(128) NOT NULL COMMENT '名称',
  `c_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `m_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_userno` (`user_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户信息表(宽表)';


CREATE TABLE `t_user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_no` varchar(128) NOT NULL COMMENT '用户号',
  `name` varchar(128) NOT NULL COMMENT '名称',
  `c_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `m_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_userno` (`user_no`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户信息表';


CREATE TABLE `t_user_addr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_no` varchar(128) NOT NULL COMMENT '用户号',
  `addr` varchar(128) NOT NULL COMMENT '地址',
  `c_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `m_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户地址表';


show master status ;

select * from mysql.user where user='canal';
show grants for 'canal'@'%';

