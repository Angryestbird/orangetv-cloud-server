CREATE TABLE IF NOT EXISTS `video` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(500) DEFAULT NULL COMMENT '视频名称',
    `path` varchar(200) DEFAULT NULL COMMENT '存储路径',
    `cover` varchar(45) DEFAULT NULL COMMENT '封面ID',
    `length` bigint DEFAULT NULL COMMENT '播放时长',
    `play` bigint DEFAULT 0 COMMENT '播放量',
    `repo_id` int NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `FULLPATH` (`repo_id`,`path`,`name`),
    FULLTEXT KEY `FULLTEXT` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Video metadata extracted from FS.';
