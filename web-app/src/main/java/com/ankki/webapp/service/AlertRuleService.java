package com.ankki.webapp.service;

import com.ankki.webapp.model.config.AlertRule;

import java.util.List;
import java.util.Map;

public interface AlertRuleService {

    Map<String, Object> page(String keyword, Byte logType, Byte ruleType, Byte status,
                             Integer pageNo, Integer pageSize);

    AlertRule detail(Integer id);

    Integer add(AlertRule record);

    Integer update(AlertRule record);

    Integer delete(Integer id);

    Integer batchDelete(List<Integer> ids);

    Integer toggleStatus(Integer id);
}
