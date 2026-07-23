package com.ankki.webapp.model.alert;

/**
 * 告警事件审计日志明细 —— 告警事件与审计源数据的多对多关联.
 *
 * <p>每条记录存储一条告警事件命中的单条审计日志及其席位 IP。
 * 普通规则每次写入一条，组合规则每次写入多条。
 * 通过 {@link #alertResultId} 关联到 {@link AlertResult}。</p>
 *
 * @author AAS-SIMP
 */
public class AlertResultAudit {

    /** 主键ID */
    private Integer id;
    /** 告警事件ID */
    private Integer alertResultId;
    /** 审计源数据全局ID（UUID） */
    private String auditLogId;
    /** 席位IP */
    private String ipAddress;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAlertResultId() { return alertResultId; }
    public void setAlertResultId(Integer alertResultId) { this.alertResultId = alertResultId; }

    public String getAuditLogId() { return auditLogId; }
    public void setAuditLogId(String auditLogId) { this.auditLogId = auditLogId; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}
