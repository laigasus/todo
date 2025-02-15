CREATE TABLE task
(
    id            BIGINT                      NOT NULL COMMENT 'ID',
    title         VARCHAR(255)                NULL COMMENT '제목',
    `description` VARCHAR(255)                NULL COMMENT '설명',
    state         ENUM ('DONE','IN_PROGRESS','TODO') DEFAULT 'TODO' NULL COMMENT '상태',
    created_at    datetime     DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '생성일',
    updated_at    datetime     DEFAULT CURRENT_TIMESTAMP  NOT NULL COMMENT '수정일',
    version       BIGINT       DEFAULT 1      NULL,
    CONSTRAINT pk_task PRIMARY KEY (id)
) COMMENT ='태스크' engine = InnoDB;