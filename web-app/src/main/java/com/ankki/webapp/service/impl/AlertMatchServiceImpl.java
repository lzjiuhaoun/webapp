package com.ankki.webapp.service.impl;

import com.ankki.webapp.dao.config.AccessListMapper;
import com.ankki.webapp.dao.config.AlertResultAuditMapper;
import com.ankki.webapp.dao.config.AlertResultMapper;
import com.ankki.webapp.dao.config.AlertRuleMapper;
import com.ankki.webapp.model.alert.AlertRawLog;
import com.ankki.webapp.model.alert.AlertResult;
import com.ankki.webapp.model.alert.AlertResultAudit;
import com.ankki.webapp.model.config.AccessListUser;
import com.ankki.webapp.model.config.AlertRule;
import com.ankki.webapp.service.AlertMatchService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 告警规则匹配引擎服务实现.
 *
 * @author AAS-SIMP
 */
@Slf4j
@Service
public class AlertMatchServiceImpl implements AlertMatchService {

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private AccessListMapper accessListMapper;

    @Autowired
    private AlertResultMapper alertResultMapper;

    @Autowired
    private AlertResultAuditMapper alertResultAuditMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public int match(AlertRawLog rawLog) {
        if (rawLog == null) {
            log.warn("规则匹配: 输入的 AlertRawLog 为空");
            return 0;
        }

        Byte logType = mapLogSource(rawLog.getLogSource());
        if (logType == null) {
            log.warn("规则匹配: 不支持的日志来源 logSource={}", rawLog.getLogSource());
            return 0;
        }

        List<AlertRule> rules = alertRuleMapper.selectEnabledNormalByLogSource(logType);
        if (rules == null || rules.isEmpty()) {
            log.debug("规则匹配: 无匹配的启用规则, logSource={}", rawLog.getLogSource());
            return 0;
        }

        int hitCount = 0;
        for (AlertRule rule : rules) {
            try {
                if (evaluateRule(rule, rawLog) && checkAccessList(rule, rawLog)) {
                    writeAlertResult(rule, rawLog);
                    hitCount++;
                    log.info("规则匹配命中: ruleId={}, ruleName={}, operator={}, recordId={}",
                            rule.getId(), rule.getName(), rawLog.getOperatorAccount(), rawLog.getRecordId());
                }
            } catch (Exception e) {
                log.error("规则匹配异常: ruleId={}, ruleName={}", rule.getId(), rule.getName(), e);
            }
        }

        log.debug("规则匹配完成: logSource={}, operator={}, hitCount={}", rawLog.getLogSource(), rawLog.getOperatorAccount(), hitCount);
        return hitCount;
    }

    private Byte mapLogSource(String logSource) {
        if (AlertRawLog.SOURCE_PLATFORM_LOGIN.equals(logSource)) {
            return AlertRule.LOG_TYPE_PLATFORM;
        } else if (AlertRawLog.SOURCE_IM.equals(logSource)) {
            return AlertRule.LOG_TYPE_IM;
        } else if (AlertRawLog.SOURCE_DLP.equals(logSource)) {
            return AlertRule.LOG_TYPE_DLP;
        }
        return null;
    }

    private boolean evaluateRule(AlertRule rule, AlertRawLog rawLog) {
        String conditionsJson = rule.getConditions();
        if (conditionsJson == null || conditionsJson.isEmpty()) {
            log.warn("规则条件为空: ruleId={}, ruleName={}", rule.getId(), rule.getName());
            return false;
        }

        List<Map<String, String>> conditions;
        try {
            conditions = objectMapper.readValue(conditionsJson,
                    new TypeReference<List<Map<String, String>>>() {});
        } catch (Exception e) {
            log.error("解析规则条件JSON失败: ruleId={}", rule.getId(), e);
            return false;
        }

        if (conditions == null || conditions.isEmpty()) {
            log.warn("规则条件列表为空: ruleId={}, ruleName={}", rule.getId(), rule.getName());
            return false;
        }

        boolean isAnd = rule.getLinkType() == null || rule.getLinkType() == AlertRule.LINK_TYPE_AND;

        // 检测登录时间跨午夜圆环范围（gte > lte）
        Boolean circularResult = detectLoginTimeCircular(rawLog, conditions);
        if (Boolean.FALSE.equals(circularResult) && isAnd) return false;
        if (Boolean.TRUE.equals(circularResult) && !isAnd) return true;

        for (Map<String, String> cond : conditions) {
            String field = cond.get("field");
            // 登录时间已由圆环逻辑处理，跳过
            if (circularResult != null && "loginTime".equals(field)) continue;

            String operator = cond.get("operator");
            String expectedValue = cond.get("value");

            if (field == null || operator == null || expectedValue == null) {
                log.warn("规则条件字段不完整: ruleId={}, condition={}", rule.getId(), cond);
                if (isAnd) return false;
                continue;
            }

            String actualValue = getFieldValue(rawLog, field);
            boolean matched = evaluateCondition(operator, actualValue, expectedValue, rawLog, field);

            if (isAnd) {
                if (!matched) return false;
            } else {
                if (matched) return true;
            }
        }

        return isAnd;
    }

    /**
     * 检测登录时间跨午夜圆环范围.
     * <p>
     * 当规则中 loginTime 同时存在 gte 和 lte，且 gte > lte 时，
     * 表示跨午夜时段（如 20:00~次日09:00），按圆环逻辑判断：
     * 实际值 >= gte OR 实际值 <= lte。
     * 不满足跨午夜条件时返回 null，走普通数值比较。
     * </p>
     */
    private Boolean detectLoginTimeCircular(AlertRawLog rawLog, List<Map<String, String>> conditions) {
        String gteValue = null;
        String lteValue = null;

        for (Map<String, String> cond : conditions) {
            if ("loginTime".equals(cond.get("field"))) {
                String op = cond.get("operator");
                String val = cond.get("value");
                if ("gte".equals(op) && val != null) gteValue = val;
                if ("lte".equals(op) && val != null) lteValue = val;
            }
        }

        if (gteValue == null || lteValue == null) return null;

        try {
            long gte = Long.parseLong(gteValue);
            long lte = Long.parseLong(lteValue);
            if (gte <= lte) return null;

            String actualStr = getFieldValue(rawLog, "loginTime");
            long actual = Long.parseLong(actualStr);
            return actual >= gte || actual <= lte;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getFieldValue(AlertRawLog rawLog, String field) {
        try {
            String getterName = "get" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
            Method getter;
            try {
                getter = AlertRawLog.class.getMethod(getterName);
            } catch (NoSuchMethodException e) {
                getter = null;
            }

            if (getter != null && getter.getReturnType() == String.class) {
                Object value = getter.invoke(rawLog);
                return value != null ? (String) value : "";
            }
        } catch (Exception e) {
            log.trace("反射获取字段值失败: field={}", field, e);
        }

        if (rawLog.getExtensions() != null) {
            Object extValue = rawLog.getExtensions().get(field);
            if (extValue != null) {
                return extValue.toString();
            }
        }

        return "";
    }

    private boolean evaluateCondition(String operator, String actualValue,
                                      String expectedValue, AlertRawLog rawLog, String field) {
        if (actualValue == null) actualValue = "";

        // fileName 字段多值匹配：规则预期值以英文逗号分隔时，拆分后逐项匹配
        if ("fileName".equals(field) && expectedValue != null && expectedValue.contains(",")) {
            String[] parts = expectedValue.split(",");
            boolean hasValidPart = false;

            if ("notEquals".equals(operator) || "notContains".equals(operator)) {
                // 否定运算符：实际值必须不匹配所有拆分项（AND）
                for (String part : parts) {
                    String trimmed = part.trim();
                    if (trimmed.isEmpty()) continue;
                    hasValidPart = true;
                    if (!evaluateSingleCondition(operator, actualValue, trimmed, rawLog)) {
                        return false;
                    }
                }
                return hasValidPart;
            } else {
                // 肯定运算符：实际值匹配任一拆分项即命中（OR）
                for (String part : parts) {
                    String trimmed = part.trim();
                    if (trimmed.isEmpty()) continue;
                    hasValidPart = true;
                    if (evaluateSingleCondition(operator, actualValue, trimmed, rawLog)) {
                        return true;
                    }
                }
                return false;
            }
        }

        return evaluateSingleCondition(operator, actualValue, expectedValue, rawLog);
    }

    private boolean evaluateSingleCondition(String operator, String actualValue,
                                            String expectedValue, AlertRawLog rawLog) {
        switch (operator) {
            case "equals":
                return actualValue.equals(expectedValue);
            case "notEquals":
                return !actualValue.equals(expectedValue);
            case "gte":
                return compareNumericOrTime(actualValue, expectedValue) >= 0;
            case "lte":
                return compareNumericOrTime(actualValue, expectedValue) <= 0;
            case "lt":
                return compareNumericOrTime(actualValue, expectedValue) < 0;
            case "gt":
                return compareNumericOrTime(actualValue, expectedValue) > 0;
            case "contains":
                return actualValue.contains(expectedValue);
            case "notContains":
                return !actualValue.contains(expectedValue);
            case "crossCamp":
                return evaluateCrossCamp(actualValue, expectedValue, rawLog);
            default:
                log.warn("不支持的运算符: operator={}", operator);
                return false;
        }
    }

    private int compareNumericOrTime(String actual, String expected) {
        try {
            long actualLong = Long.parseLong(actual.trim());
            long expectedLong = Long.parseLong(expected.trim());
            return Long.compare(actualLong, expectedLong);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean evaluateCrossCamp(String senderParty, String campGroupsDef, AlertRawLog rawLog) {
        if (senderParty == null || senderParty.isEmpty()) return false;
        if (campGroupsDef == null || campGroupsDef.isEmpty()) return false;
        if (rawLog.getReceivers() == null || rawLog.getReceivers().isEmpty()) return false;

        String[] groups = campGroupsDef.split("、");
        List<Set<String>> campGroups = Arrays.stream(groups)
                .map(g -> Arrays.stream(g.split("\\|"))
                        .map(String::trim)
                        .collect(Collectors.toSet()))
                .collect(Collectors.toList());

        int senderGroupIndex = -1;
        for (int i = 0; i < campGroups.size(); i++) {
            if (campGroups.get(i).contains(senderParty)) {
                senderGroupIndex = i;
                break;
            }
        }
        if (senderGroupIndex < 0) return true;

        for (AlertRawLog.AlertRawLogReceiver receiver : rawLog.getReceivers()) {
            String receiverParty = receiver.getParty();
            if (receiverParty == null || receiverParty.isEmpty()) continue;

            int receiverGroupIndex = -1;
            for (int i = 0; i < campGroups.size(); i++) {
                if (campGroups.get(i).contains(receiverParty)) {
                    receiverGroupIndex = i;
                    break;
                }
            }
            if (receiverGroupIndex >= 0 && receiverGroupIndex != senderGroupIndex) {
                return true;
            }
        }

        return false;
    }

    private boolean checkAccessList(AlertRule rule, AlertRawLog rawLog) {
        String account = rawLog.getOperatorAccount();
        String name = rawLog.getOperatorName();

        Integer blacklistId = rule.getBlacklistId();
        Integer whitelistId = rule.getWhitelistId();

        if (blacklistId != null) {
            return isUserInList(blacklistId, account, name);
        }

        if (whitelistId != null) {
            return !isUserInList(whitelistId, account, name);
        }

        return true;
    }

    private boolean isUserInList(Integer listId, String account, String name) {
        List<AccessListUser> users = accessListMapper.selectUsersByListId(listId);
        if (users == null || users.isEmpty()) return false;

        for (AccessListUser user : users) {
            if (user.getUserAccount() != null && user.getUserAccount().equals(account)) {
                return true;
            }
            if (user.getUserName() != null && user.getUserName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void writeAlertResult(AlertRule rule, AlertRawLog rawLog) {
        AlertResult result = new AlertResult();
        result.setAlertRuleId(rule.getId());
        result.setRuleType(AlertResult.RULE_TYPE_NORMAL);
        result.setNormalRuleId(null);
        result.setAlertLevel(rule.getLevel());
        result.setTriggerCount(1);
        result.setCreateTime(System.currentTimeMillis());
        alertResultMapper.insert(result);

        AlertResultAudit audit = new AlertResultAudit();
        audit.setAlertResultId(result.getId());
        audit.setAuditLogId(rawLog.getRecordId());
        audit.setIpAddress(rawLog.getIpAddress());
        alertResultAuditMapper.insert(audit);

        log.info("告警事件已写入: ruleId={}, ruleName={}, level={}, recordId={}, ip={}, resultId={}",
                rule.getId(), rule.getName(), rule.getLevel(), rawLog.getRecordId(),
                rawLog.getIpAddress(), result.getId());
    }
}
