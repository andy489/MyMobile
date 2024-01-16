# CREATE SCHEMA IF NOT EXISTS `mobilele`;
USE `mobilele`;

CREATE TABLE `users`
(
    `id`         bigint       NOT NULL AUTO_INCREMENT,
    `email`      varchar(255) NOT NULL,
    `first_name` varchar(255)          DEFAULT NULL,
    `image_url`  varchar(255)          DEFAULT NULL,
    `is_active`  bit(1)       NOT NULL DEFAULT TRUE,
    `last_name`  varchar(255)          DEFAULT NULL,
    `password`   varchar(255)          DEFAULT NULL,
    `username`   varchar(255) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
    UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `activation_tokens`
(
    `id`               bigint NOT NULL AUTO_INCREMENT,
    `activation_token` varchar(255) DEFAULT NULL,
    `user_id`          bigint       DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_gcvbc60fw9x3ehvlxdsq2kvrn` (`user_id`),
    CONSTRAINT `FKgtry5rof27b5hdur1a11y0gsn` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `user_roles`
(
    `id`             bigint                            NOT NULL AUTO_INCREMENT,
    `user_role_enum` enum ('ADMIN','MODERATOR','USER') NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `users_roles`
(
    `user_id` bigint NOT NULL,
    `role_id`  bigint NOT NULL,
    KEY `FKdwlsjl9336fne9vntsddpf6xs` (`role_id`),
    KEY `FKmch2wehr2qllj0ub9y4anjs9i` (`user_id`),
    CONSTRAINT `FKdwlsjl9336fne9vntsddpf6xs` FOREIGN KEY (`role_id`) REFERENCES `user_roles` (`id`),
    CONSTRAINT `FKmch2wehr2qllj0ub9y4anjs9i` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `brands`
(
    `id`   bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `models`
(
    `id`         bigint                                  NOT NULL AUTO_INCREMENT,
    `category`   enum ('BUS','CAR','MOTORCYCLE','TRUCK') NOT NULL,
    `end_year`   int    DEFAULT NULL,
    `image_url`  varchar(255)                            NOT NULL,
    `name`       varchar(255)                            NOT NULL,
    `start_year` int                                     NOT NULL,
    `brand_id`   bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `FK95s72g5hnsl3o0bqeuhnokdxu` (`brand_id`),
    CONSTRAINT `FK95s72g5hnsl3o0bqeuhnokdxu` FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `offers`
(
    `id`                   varchar(127)                                   NOT NULL,
    `created`              datetime(6) DEFAULT NULL,
    `description`          mediumtext                                     NOT NULL,
    `engine`               enum ('DIESEL','ELECTRIC','GASOLINE','HYBRID') NOT NULL,
    `image_url`            text        DEFAULT NULL,
    `mileage`              int         DEFAULT NULL,
    `modified`             datetime(6) DEFAULT NULL,
    `price`                decimal(38, 2)                                 NOT NULL,
    `transmission`         enum ('MT','AT','AM','CVT')                    NOT NULL,
    `year`                 int         DEFAULT FALSE,
    `awd`                  boolean     DEFAULT FALSE,
    `climatic`             boolean     DEFAULT FALSE,
    `parktronic`           boolean     DEFAULT FALSE,
    `navi`                 boolean     DEFAULT FALSE,
    `leasing`              boolean     DEFAULT FALSE,
    `xenon_lights`         boolean     DEFAULT FALSE,
    `isofix_system`        boolean     DEFAULT FALSE,
    `anti_blocking_system` boolean     DEFAULT FALSE,
    `cabriolet`            boolean     DEFAULT FALSE,
    `bluetooth`            boolean     DEFAULT FALSE,
    `model_id`             bigint      DEFAULT FALSE,
    `seller_id`            bigint      DEFAULT FALSE,
    PRIMARY KEY (`id`),
    KEY `FK3dbbkoli06hqmi3e8qidufetr` (`model_id`),
    KEY `FK64dv88mu37r6tcmd4cgu5sve` (`seller_id`),
    CONSTRAINT `FK3dbbkoli06hqmi3e8qidufetr` FOREIGN KEY (`model_id`) REFERENCES `models` (`id`),
    CONSTRAINT `FK64dv88mu37r6tcmd4cgu5sve` FOREIGN KEY (`seller_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `exchange_rates`
(
    `currency` varchar(255)   NOT NULL,
    `rate`     decimal(38, 2) NOT NULL,
    PRIMARY KEY (`currency`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci