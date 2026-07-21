-- 告警规则表
-- 对应实体: AlertRule
CREATE TABLE alert_rule (
    id               INT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    name             VARCHAR(64) NOT NULL COMMENT '规则名称',
    description      VARCHAR(255) DEFAULT '' COMMENT '描述',
    log_type         TINYINT NOT NULL COMMENT '日志类型: 0=平台登录日志, 1=IM登录日志, 2=DLP日志',
    rule_type        TINYINT NOT NULL COMMENT '规则类型: 0=普通规则, 1=组合规则',
    level            TINYINT DEFAULT NULL COMMENT '普通规则告警等级: 1=一级红, 2=二级橙, 3=三级黄',
    link_type        TINYINT DEFAULT 0 COMMENT '关联方式: 0=与, 1=或',
    conditions       TEXT DEFAULT NULL COMMENT '匹配条件 JSON: [{"field":"","operator":"","value":""},...]',
    combine_duration INT DEFAULT NULL COMMENT '组合规则时间窗口(分钟), 预留',
    combine_count    INT DEFAULT NULL COMMENT '组合规则触发次数阈值',
    combine_rule_id  INT DEFAULT NULL COMMENT '组合规则引用的普通规则 ID',
    whitelist_id     INT DEFAULT NULL COMMENT '白名单 ID',
    blacklist_id     INT DEFAULT NULL COMMENT '黑名单 ID',
    status           TINYINT DEFAULT 1 COMMENT '状态: 0=禁用, 1=启用',
    create_time      BIGINT NOT NULL COMMENT '创建时间',
    update_time      BIGINT NOT NULL COMMENT '更新时间',
    is_delete        TINYINT DEFAULT 0 COMMENT '软删除: 0=未删, 1=已删'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='告警规则表';
