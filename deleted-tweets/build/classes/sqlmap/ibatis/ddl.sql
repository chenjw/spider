drop table dt_watched_user;
CREATE TABLE `dt_watched_user` (
  `user_id` varchar(128) DEFAULT NULL COMMENT '用户id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

drop table dt_tweet;
CREATE TABLE `dt_tweet` (
  `tid` varchar(128) NOT NULL COMMENT 'pk',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `post_date` datetime DEFAULT NULL COMMENT '微博发送时间',
  `user_id` varchar(128) DEFAULT NULL COMMENT '用户id',
  `content` blob COMMENT '微博内容',
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;

drop table dt_deleted_tweet;
CREATE TABLE `dt_deleted_tweet` (
  `tid` varchar(128) NOT NULL COMMENT 'pk',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `post_date` datetime DEFAULT NULL COMMENT '微博发送时间',
  `user_id` varchar(128) DEFAULT NULL COMMENT '用户id',
  `content` blob COMMENT '微博内容',
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ;
