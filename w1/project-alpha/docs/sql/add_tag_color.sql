-- 为 tag 表添加 color 字段
-- 执行此脚本以添加标签颜色功能

USE ticket_system;

-- 添加 color 字段，默认值为 #86868b（灰色）
ALTER TABLE tag 
ADD COLUMN color VARCHAR(7) NOT NULL DEFAULT '#86868b' COMMENT '标签颜色（十六进制颜色代码）' 
AFTER name;

-- 为现有标签设置默认颜色（可选，如果需要为现有标签设置不同颜色）
-- UPDATE tag SET color = '#0071e3' WHERE name = '重要';
-- UPDATE tag SET color = '#ff3b30' WHERE name = '紧急';
-- UPDATE tag SET color = '#ff9500' WHERE name = '待办';
-- UPDATE tag SET color = '#34c759' WHERE name = '已完成';

