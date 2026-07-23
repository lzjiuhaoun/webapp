-- 告警事件表（告警规则匹配结果）
-- 对应实体: AlertResult
CREATE TABLE alert_result (
    id              INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    alert_rule_id   INT NOT NULL COMMENT '命中规则 ID',
    rule_type       TINYINT NOT NULL COMMENT '规则类型: 0=普通, 1=组合',
    normal_rule_id  INT DEFAULT NULL COMMENT '组合规则引用的普通规则 ID',
    alert_level     TINYINT NOT NULL COMMENT '告警等级: 1=红, 2=橙, 3=黄',
    trigger_count   INT DEFAULT 1 COMMENT '实际触发次数',
    create_time     BIGINT NOT NULL COMMENT '创建时间',
    INDEX idx_alert_rule_id (alert_rule_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警事件表';

-- 告警事件审计日志明细表（审计日志ID ↔ 席位IP 多对多关联）
-- 对应实体: AlertResultAudit
CREATE TABLE alert_result_audit (
    id              INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    alert_result_id INT NOT NULL COMMENT '告警事件ID',
    audit_log_id    VARCHAR(36) NOT NULL COMMENT '审计源数据全局ID（UUID）',
    ip_address      VARCHAR(45) NOT NULL COMMENT '席位IP',
    INDEX idx_alert_result_id (alert_result_id),
    INDEX idx_ip_address (ip_address)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警事件审计日志明细表';
