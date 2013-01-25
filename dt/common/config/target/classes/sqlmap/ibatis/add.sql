alter table dt_tweet add reposts_count int unsigned NOT NULL COMMENT '转发数';
alter table dt_tweet add comments_count int unsigned NOT NULL COMMENT '评论数';
alter table dt_deleted_tweet add reposts_count int unsigned NOT NULL COMMENT '转发数';
alter table dt_deleted_tweet add comments_count int unsigned NOT NULL COMMENT '评论数';