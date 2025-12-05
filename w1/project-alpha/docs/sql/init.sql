-- Ticket 标签管理系统数据库初始化脚本
-- 数据库：ticket_system
-- 字符集：utf8mb4

-- 创建数据库
CREATE DATABASE IF NOT EXISTS ticket_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ticket_system;

-- 1. 创建 ticket 表
CREATE TABLE IF NOT EXISTS ticket (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    description TEXT COMMENT '描述',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态：0=未完成，1=已完成',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    completed_at DATETIME NULL COMMENT '完成时间',
    INDEX idx_ticket_title (title),
    INDEX idx_ticket_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Ticket表';

-- 2. 创建 tag 表
CREATE TABLE IF NOT EXISTS tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '标签名称',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_tag_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='标签表';

-- 3. 创建 ticket_tag 关联表
CREATE TABLE IF NOT EXISTS ticket_tag (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    ticket_id BIGINT NOT NULL COMMENT 'Ticket ID',
    tag_id BIGINT NOT NULL COMMENT 'Tag ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_ticket_tag (ticket_id, tag_id),
    INDEX idx_ticket_tag_ticket_id (ticket_id),
    INDEX idx_ticket_tag_tag_id (tag_id),
    FOREIGN KEY (ticket_id) REFERENCES ticket(id) ON DELETE CASCADE,
    FOREIGN KEY (tag_id) REFERENCES tag(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Ticket标签关联表';

-- 插入示例数据（可选）
-- 插入示例标签
INSERT INTO tag (name) VALUES 
    ('重要'),
    ('紧急'),
    ('待办'),
    ('已完成')
ON DUPLICATE KEY UPDATE name=name;

-- 插入示例 Ticket
INSERT INTO ticket (title, description, status) VALUES 
    ('示例Ticket 1', '这是一个示例Ticket，用于测试系统功能', 0),
    ('示例Ticket 2', '这是另一个示例Ticket', 1)
ON DUPLICATE KEY UPDATE title=title;

-- 关联示例数据（假设上面插入的 ticket id 为 1 和 2，tag id 为 1, 2, 3）
-- 注意：实际执行时需要根据实际插入的 ID 调整
-- INSERT INTO ticket_tag (ticket_id, tag_id) VALUES 
--     (1, 1),
--     (1, 2),
--     (2, 3);

