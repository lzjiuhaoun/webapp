package com.ankki.webapp.service.impl;

import com.ankki.webapp.dao.config.AccessListMapper;
import com.ankki.webapp.dao.config.AlertRuleMapper;
import com.ankki.webapp.model.config.AccessList;
import com.ankki.webapp.model.config.AlertRule;
import com.ankki.webapp.service.AlertRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 告警规则服务实现.
 * <p>
 * 实现告警规则的业务逻辑，包括：
 * <ul>
 *   <li>普通规则创建时校验关联黑白名单的有效性</li>
 *   <li>组合规则引用的普通规则受保护：删除/禁用前检查引用关系</li>
 * </ul>
 * </p>
 *
 * @author AAS-SIMP
 */
@Slf4j
@Service
public class AlertRuleServiceImpl implements AlertRuleService {

    /** 告警规则数据访问层 */
    @Autowired
    private AlertRuleMapper alertRuleMapper;

    /** 黑白名单数据访问层 */
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

    /**
     * 校验关联的黑白名单是否存在且已启用.
     *
     * @param accessListId 黑白名单ID
     * @throws IllegalArgumentException 黑白名单不存在或已禁用/删除
     */
    private void validateAccessList(Integer accessListId) {
        if (accessListId == null) return;
        AccessList list = accessListMapper.selectEnabledById(accessListId);
        if (list == null) {
            log.warn("关联的黑白名单不存在或已禁用: accessListId={}", accessListId);
            throw new IllegalArgumentException("关联的黑白名单不存在或已禁用/删除");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
        log.info("新增告警规则: id={}, name={}, ruleType={}", record.getId(), record.getName(), record.getRuleType());
        return record.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer update(AlertRule record) {
        validateAccessList(record.getBlacklistId());
        validateAccessList(record.getWhitelistId());
        record.setUpdateTime(System.currentTimeMillis());
        alertRuleMapper.update(record);
        log.info("更新告警规则: id={}, name={}", record.getId(), record.getName());
        return record.getId();
    }

    /**
     * 检查普通规则是否被组合规则引用.
     *
     * @param normalRuleId 普通规则ID
     * @throws IllegalArgumentException 已被引用
     */
    private void checkNotReferenced(Integer normalRuleId) {
        List<AlertRule> refs = alertRuleMapper.selectByCombineRuleId(normalRuleId);
        if (!refs.isEmpty()) {
            log.warn("普通规则已被组合规则引用，无法操作: normalRuleId={}, 引用规则={}", normalRuleId, refs.get(0).getName());
            throw new IllegalArgumentException("该普通规则已被组合规则「" + refs.get(0).getName() + "」引用，无法操作");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delete(Integer id) {
        checkNotReferenced(id);
        long now = System.currentTimeMillis();
        int count = alertRuleMapper.deleteById(id, now);
        log.info("删除告警规则: id={}, result={}", id, count);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDelete(List<Integer> ids) {
        for (Integer id : ids) {
            checkNotReferenced(id);
        }
        long now = System.currentTimeMillis();
        int count = alertRuleMapper.batchDelete(ids, now);
        log.info("批量删除告警规则: ids={}, count={}", ids, count);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer toggleStatus(Integer id) {
        AlertRule record = alertRuleMapper.selectById(id);
        if (record == null) {
            log.warn("切换状态失败: 告警规则不存在, id={}", id);
            return 0;
        }
        // 普通规则被组合规则引用时，禁止切换状态
        if (record.getRuleType() == AlertRule.RULE_TYPE_NORMAL) {
            checkNotReferenced(id);
        }
        byte newStatus = record.getStatus() == AlertRule.STATUS_ENABLED
                ? AlertRule.STATUS_DISABLED : AlertRule.STATUS_ENABLED;
        record.setStatus(newStatus);
        record.setUpdateTime(System.currentTimeMillis());
        int count = alertRuleMapper.update(record);
        log.info("切换告警规则状态: id={}, name={}, newStatus={}", id, record.getName(), newStatus);
        return count;
    }
}
