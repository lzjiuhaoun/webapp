package com.ankki.webapp.model.alert;

import java.util.List;
import java.util.Map;

/**
 * 告警事件源数据 —— 规则引擎匹配的标准化输入.
 *
 * <p>各日志源（统一平台登录日志、IM即时通讯日志等）经过各自的解析器（Parser）转换后，
 * 统一填充为该实体并送入规则引擎。引擎逐条比对启用的告警规则（包括普通规则和组合规则），
 * 命中的记录进入告警流水表。</p>
 *
 * <p>{@link #rawData} 保留该日志来源的原始数据（如 IM 消息 JSON、平台登录日志 JSON），
 * 用于追踪溯源、调试以及后续重新解析。</p>
 *
 * <p>{@code extensions} 字段为 {@code Map<String, Object>}，用于承载未来新增的、
 * 当前实体显式字段尚未覆盖的匹配字段，无须修改实体定义即可扩展。</p>
 *
 * @author AAS-SIMP
 */
public class AlertRawLog {

    /** 日志来源：统一平台登录 */
    public static final String SOURCE_PLATFORM_LOGIN = "PLATFORM_LOGIN";
    /** 日志来源：IM即时通讯（含登录/聊天/文件传输） */
    public static final String SOURCE_IM = "IM";
    /** 日志来源：DLP */
    public static final String SOURCE_DLP = "DLP";

    /** 操作类型：登录 */
    public static final String LOG_TYPE_LOGIN = "登录";
    /** 操作类型：登出 */
    public static final String LOG_TYPE_LOGOUT = "登出";
    /** 操作类型：私聊 */
    public static final String LOG_TYPE_PRIVATE_CHAT = "私聊";
    /** 操作类型：群聊 */
    public static final String LOG_TYPE_GROUP_CHAT = "群聊";

    /** 操作结果：成功 */
    public static final String RESULT_SUCCESS = "成功";
    /** 操作结果：失败 */
    public static final String RESULT_FAILURE = "失败";

    /** 发送方式：单发 */
    public static final String SEND_SINGLE = "单发";
    /** 发送方式：群发 */
    public static final String SEND_BATCH = "群发";

    /** 日志来源 */
    private String logSource;

    /** 操作人名称 */
    private String operatorName;

    /** 操作人账号 */
    private String operatorAccount;

    /** 操作人ID */
    private String operatorId;

    /** 席位IP */
    private String ipAddress;

    /** 日志时间戳（毫秒） */
    private Long logTime;

    /** 操作类型：登录/登出/私聊/群聊 */
    private String logType;

    /** 操作结果：成功/失败 */
    private String operationResult;

    /** 发送方式：单发/群发（仅IM聊天相关日志有效，由解析层从logType派生） */
    private String sendMethod;

    /** 文件名称（文件传输相关日志） */
    private String fileName;

    /** 文件地址 */
    private String fileUrl;

    /** 发送方参与方/阵营 */
    private String senderParty;

    /** 接收人列表 */
    private List<AlertRawLogReceiver> receivers;

    /** 日志描述原文 */
    private String description;

    /** 原始日志数据（JSON 字符串），如 IM 消息的完整 JSON、平台登录日志的完整记录 */
    private String rawData;

    /** 扩展字段，用于未来新增匹配条件字段 */
    private Map<String, Object> extensions;

    public String getLogSource() { return logSource; }
    public void setLogSource(String logSource) { this.logSource = logSource; }

    public String getOperatorName() { return operatorName; }
    public void setOperatorName(String operatorName) { this.operatorName = operatorName; }

    public String getOperatorAccount() { return operatorAccount; }
    public void setOperatorAccount(String operatorAccount) { this.operatorAccount = operatorAccount; }

    public String getOperatorId() { return operatorId; }
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public Long getLogTime() { return logTime; }
    public void setLogTime(Long logTime) { this.logTime = logTime; }

    public String getLogType() { return logType; }
    public void setLogType(String logType) { this.logType = logType; }

    public String getOperationResult() { return operationResult; }
    public void setOperationResult(String operationResult) { this.operationResult = operationResult; }

    public String getSendMethod() { return sendMethod; }
    public void setSendMethod(String sendMethod) { this.sendMethod = sendMethod; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public String getSenderParty() { return senderParty; }
    public void setSenderParty(String senderParty) { this.senderParty = senderParty; }

    public List<AlertRawLogReceiver> getReceivers() { return receivers; }
    public void setReceivers(List<AlertRawLogReceiver> receivers) { this.receivers = receivers; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getRawData() { return rawData; }
    public void setRawData(String rawData) { this.rawData = rawData; }

    public Map<String, Object> getExtensions() { return extensions; }
    public void setExtensions(Map<String, Object> extensions) { this.extensions = extensions; }

    /**
     * 接收人信息.
     */
    public static class AlertRawLogReceiver {

        /** 接收人账号 */
        private String userName;
        /** 接收人姓名 */
        private String name;
        /** 接收人参与方/阵营 */
        private String party;

        public String getUserName() { return userName; }
        public void setUserName(String userName) { this.userName = userName; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getParty() { return party; }
        public void setParty(String party) { this.party = party; }
    }
}
