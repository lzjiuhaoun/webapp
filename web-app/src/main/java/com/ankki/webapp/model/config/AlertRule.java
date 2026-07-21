package com.ankki.webapp.model.config;

public class AlertRule {

    public static final byte RULE_TYPE_NORMAL = 0;
    public static final byte RULE_TYPE_COMPOSITE = 1;

    public static final byte LINK_TYPE_AND = 0;
    public static final byte LINK_TYPE_OR = 1;

    public static final byte LEVEL_ONE = 1;
    public static final byte LEVEL_TWO = 2;
    public static final byte LEVEL_THREE = 3;

    public static final byte STATUS_DISABLED = 0;
    public static final byte STATUS_ENABLED = 1;

    public static final byte LOG_TYPE_PLATFORM = 0;
    public static final byte LOG_TYPE_IM = 1;
    public static final byte LOG_TYPE_DLP = 2;

    private Integer id;
    private String name;
    private String description;
    private Byte logType;
    private Byte ruleType;
    private Byte level;
    private Byte linkType;
    private String conditions;
    private Integer combineDuration;
    private Integer combineCount;
    private Integer combineRuleId;
    private Integer whitelistId;
    private Integer blacklistId;
    private Byte status;
    private Long createTime;
    private Long updateTime;
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
