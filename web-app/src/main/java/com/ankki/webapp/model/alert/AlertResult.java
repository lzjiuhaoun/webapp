package com.ankki.webapp.model.alert;

/**
 * 告警事件 —— 规则引擎匹配成功时输出的持久化记录.
 *
 * <p>普通规则命中一条产生一条告警事件（存单个审计 ID），
 * 组合规则命中产生一条告警事件（存逗号分隔的多个审计 ID 及实际触发次数）。
 * 所有展示数据通过审计源数据 ID 从 ES 查询，不冗余存储日志字段。</p>
 *
 * @author AAS-SIMP
 */
public class AlertResult {

    /** 规则类型：普通规则 */
    public static final byte RULE_TYPE_NORMAL = 0;
    /** 规则类型：组合规则 */
    public static final byte RULE_TYPE_COMPOSITE = 1;

    /** 告警等级：一级（红） */
    public static final byte LEVEL_ONE = 1;
    /** 告警等级：二级（橙） */
    public static final byte LEVEL_TWO = 2;
    /** 告警等级：三级（黄） */
    public static final byte LEVEL_THREE = 3;

    /** 主键ID */
    private Integer id;
    /** 命中规则ID */
    private Integer alertRuleId;
    /** 规则类型：0=普通, 1=组合 */
    private Byte ruleType;
    /** 审计源数据全局ID，逗号分隔 */
    private String auditLogIds;
    /** 组合规则引用的普通规则ID */
    private Integer normalRuleId;
    /** 告警等级：1=红, 2=橙, 3=黄 */
    private Byte alertLevel;
    /** 实际触发次数 */
    private Integer triggerCount;
    /** 创建时间戳 */
    private Long createTime;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAlertRuleId() { return alertRuleId; }
    public void setAlertRuleId(Integer alertRuleId) { this.alertRuleId = alertRuleId; }

    public Byte getRuleType() { return ruleType; }
    public void setRuleType(Byte ruleType) { this.ruleType = ruleType; }

    public String getAuditLogIds() { return auditLogIds; }
    public void setAuditLogIds(String auditLogIds) { this.auditLogIds = auditLogIds; }

    public Integer getNormalRuleId() { return normalRuleId; }
    public void setNormalRuleId(Integer normalRuleId) { this.normalRuleId = normalRuleId; }

    public Byte getAlertLevel() { return alertLevel; }
    public void setAlertLevel(Byte alertLevel) { this.alertLevel = alertLevel; }

    public Integer getTriggerCount() { return triggerCount; }
    public void setTriggerCount(Integer triggerCount) { this.triggerCount = triggerCount; }

    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }
}
