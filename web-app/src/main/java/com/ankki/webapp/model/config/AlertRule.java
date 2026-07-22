package com.ankki.webapp.model.config;

/**
 * 告警规则实体类.
 * <p>
 * 对应数据表 alert_rule，支持普通规则与组合规则两种类型。
 * 普通规则包含匹配条件集合，组合规则引用普通规则并配置触发次数阈值。
 * </p>
 *
 * @author AAS-SIMP
 */
public class AlertRule {

    /** 规则类型：普通规则 */
    public static final byte RULE_TYPE_NORMAL = 0;
    /** 规则类型：组合规则 */
    public static final byte RULE_TYPE_COMPOSITE = 1;

    /** 关联方式：与（全部条件满足） */
    public static final byte LINK_TYPE_AND = 0;
    /** 关联方式：或（满足任一条件） */
    public static final byte LINK_TYPE_OR = 1;

    /** 告警等级：一级（红） */
    public static final byte LEVEL_ONE = 1;
    /** 告警等级：二级（橙） */
    public static final byte LEVEL_TWO = 2;
    /** 告警等级：三级（黄） */
    public static final byte LEVEL_THREE = 3;

    /** 状态：禁用 */
    public static final byte STATUS_DISABLED = 0;
    /** 状态：启用 */
    public static final byte STATUS_ENABLED = 1;

    /** 日志类型：平台登录日志 */
    public static final byte LOG_TYPE_PLATFORM = 0;
    /** 日志类型：IM登录日志 */
    public static final byte LOG_TYPE_IM = 1;
    /** 日志类型：DLP日志 */
    public static final byte LOG_TYPE_DLP = 2;

    /** 主键ID */
    private Integer id;
    /** 规则名称 */
    private String name;
    /** 描述 */
    private String description;
    /** 日志来源：0=平台登录日志, 1=IM登录日志, 2=DLP日志（区别于原始日志的操作类型 logType） */
    private Byte logType;
    /** 规则类型：0=普通规则, 1=组合规则 */
    private Byte ruleType;
    /** 告警等级：1=一级红, 2=二级橙, 3=三级黄 */
    private Byte level;
    /** 关联方式：0=与, 1=或 */
    private Byte linkType;
    /** 匹配条件 JSON 字符串 */
    private String conditions;
    /** 组合规则时间范围（分钟） */
    private Integer combineDuration;
    /** 组合规则触发次数阈值 */
    private Integer combineCount;
    /** 组合规则引用的普通规则ID */
    private Integer combineRuleId;
    /** 关联白名单ID */
    private Integer whitelistId;
    /** 关联黑名单ID */
    private Integer blacklistId;
    /** 状态：0=禁用, 1=启用 */
    private Byte status;
    /** 创建时间戳 */
    private Long createTime;
    /** 更新时间戳 */
    private Long updateTime;
    /** 软删除标记：0=未删, 1=已删 */
    private Byte isDelete;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Byte getLogType() { return logType; }
    public void setLogType(Byte logType) { this.logType = logType; }

    public Byte getRuleType() { return ruleType; }
    public void setRuleType(Byte ruleType) { this.ruleType = ruleType; }

    public Byte getLevel() { return level; }
    public void setLevel(Byte level) { this.level = level; }

    /**
     * 获取告警等级的中文显示名称.
     *
     * @return 等级名称，如"一级（红）"；level为null时返回空字符串
     */
    public String getLevelName() {
        if (level == null) return "";
        switch (level) {
            case LEVEL_ONE: return "一级（红）";
            case LEVEL_TWO: return "二级（橙）";
            case LEVEL_THREE: return "三级（黄）";
            default: return "";
        }
    }

    public Byte getLinkType() { return linkType; }
    public void setLinkType(Byte linkType) { this.linkType = linkType; }

    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }

    public Integer getCombineDuration() { return combineDuration; }
    public void setCombineDuration(Integer combineDuration) { this.combineDuration = combineDuration; }

    public Integer getCombineCount() { return combineCount; }
    public void setCombineCount(Integer combineCount) { this.combineCount = combineCount; }

    public Integer getCombineRuleId() { return combineRuleId; }
    public void setCombineRuleId(Integer combineRuleId) { this.combineRuleId = combineRuleId; }

    public Integer getWhitelistId() { return whitelistId; }
    public void setWhitelistId(Integer whitelistId) { this.whitelistId = whitelistId; }

    public Integer getBlacklistId() { return blacklistId; }
    public void setBlacklistId(Integer blacklistId) { this.blacklistId = blacklistId; }

    public Byte getStatus() { return status; }
    public void setStatus(Byte status) { this.status = status; }

    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }

    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }

    public Byte getIsDelete() { return isDelete; }
    public void setIsDelete(Byte isDelete) { this.isDelete = isDelete; }
}
