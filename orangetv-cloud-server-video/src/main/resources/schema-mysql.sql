CREATE TABLE IF NOT EXISTS `video` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(500) DEFAULT NULL COMMENT '视频名称',
    `path` varchar(200) DEFAULT NULL COMMENT '存储路径',
    `cover` varchar(45) DEFAULT NULL COMMENT '封面ID',
    `length` bigint DEFAULT NULL COMMENT '播放时长',
    PRIMARY KEY (`id`),
    UNIQUE KEY `FULLPATH` (`path`,`name`),
    FULLTEXT KEY `FULLTEXT` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='Video metadata extracted from FS.';
