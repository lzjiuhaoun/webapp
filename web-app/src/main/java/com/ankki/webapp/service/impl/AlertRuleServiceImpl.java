package com.ankki.webapp.service.impl;

import com.ankki.webapp.dao.config.AccessListMapper;
import com.ankki.webapp.dao.config.AlertRuleMapper;
import com.ankki.webapp.model.config.AccessList;
import com.ankki.webapp.model.config.AlertRule;
import com.ankki.webapp.service.AlertRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AlertRuleServiceImpl implements AlertRuleService {

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private AccessListMapper accessListMapper;

    @Override
    public Map<String, Object> page(String keyword, Byte logType, Byte ruleType, Byte status,
                                    Integer pageNo, Integer pageSize) {
        if (pageNo == null || pageNo < 1) pageNo = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        Integer offset = (pageNo - 1) * pageSize;

        List<AlertRule> list = alertRuleMapper.selectPage(keyword, logType, ruleType, status, offset, pageSize);
        Integer total = alertRuleMapper.countPage(keyword, logType, ruleType, status);

        Map<String, Object> result = new HashMap<>();
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    @Override
    public AlertRule detail(Integer id) {
        return alertRuleMapper.selectById(id);
    }

    private void validateAccessList(Integer accessListId) {
        if (accessListId == null) return;
        AccessList list = accessListMapper.selectEnabledById(accessListId);
        if (list == null) {
            throw new IllegalArgumentException("关联的黑白名单不存在或已禁用/删除");
        }
    }

    @Override
    @Transactional
    public Integer add(AlertRule record) {
        validateAccessList(record.getBlacklistId());
        validateAccessList(record.getWhitelistId());
        long now = System.currentTimeMillis();
        if (record.getStatus() == null) {
            record.setStatus(AlertRule.STATUS_ENABLED);
        }
        record.setCreateTime(now);
        record.setUpdateTime(now);
        alertRuleMapper.insert(record);
        return record.getId();
    }

    @Override
    @Transactional
    public Integer update(AlertRule record) {
        validateAccessList(record.getBlacklistId());
        validateAccessList(record.getWhitelistId());
        record.setUpdateTime(System.currentTimeMillis());
        alertRuleMapper.update(record);
        return record.getId();
    }

    private void checkNotReferenced(Integer normalRuleId) {
        List<AlertRule> refs = alertRuleMapper.selectByCombineRuleId(normalRuleId);
        if (!refs.isEmpty()) {
            throw new IllegalArgumentException("该普通规则已被组合规则「" + refs.get(0).getName() + "」引用，无法操作");
        }
    }

    @Override
    @Transactional
    public Integer delete(Integer id) {
        checkNotReferenced(id);
        long now = System.currentTimeMillis();
        return alertRuleMapper.deleteById(id, now);
    }

    @Override
    @Transactional
    public Integer batchDelete(List<Integer> ids) {
        for (Integer id : ids) {
            checkNotReferenced(id);
        }
        long now = System.currentTimeMillis();
        return alertRuleMapper.batchDelete(ids, now);
    }

    @Override
    @Transactional
    public Integer toggleStatus(Integer id) {
        AlertRule record = alertRuleMapper.selectById(id);
        if (record == null) return 0;
        if (record.getRuleType() == AlertRule.RULE_TYPE_NORMAL) {
            checkNotReferenced(id);
        }
        record.setStatus(record.getStatus() == AlertRule.STATUS_ENABLED
                ? AlertRule.STATUS_DISABLED : AlertRule.STATUS_ENABLED);
        record.setUpdateTime(System.currentTimeMillis());
        return alertRuleMapper.update(record);
    }
}
