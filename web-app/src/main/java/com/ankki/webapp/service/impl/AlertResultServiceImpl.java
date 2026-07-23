package com.ankki.webapp.service.impl;

import com.ankki.webapp.dao.config.AlertResultMapper;
import com.ankki.webapp.model.alert.AlertResult;
import com.ankki.webapp.service.AlertResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 告警事件业务服务实现.
 *
 * @author AAS-SIMP
 */
@Slf4j
@Service
public class AlertResultServiceImpl implements AlertResultService {

    @Autowired
    private AlertResultMapper alertResultMapper;

    @Override
    public Map<String, Object> page(String keyword, Byte alertLevel, Byte ruleType,
                                    Integer pageNo, Integer pageSize) {
        if (pageNo == null || pageNo < 1) pageNo = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        Integer offset = (pageNo - 1) * pageSize;

        List<AlertResult> list = alertResultMapper.selectPage(keyword, alertLevel, ruleType, offset, pageSize);
        Integer total = alertResultMapper.countPage(keyword, alertLevel, ruleType);

        Map<String, Object> result = new HashMap<>();
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    @Override
    public AlertResult detail(Integer id) {
        return alertResultMapper.selectById(id);
    }

    @Override
    public Integer delete(Integer id) {
        int count = alertResultMapper.deleteById(id);
        log.info("删除告警事件: id={}, result={}", id, count);
        return count;
    }

    @Override
    public Integer batchDelete(List<Integer> ids) {
        int count = 0;
        for (Integer id : ids) {
            count += alertResultMapper.deleteById(id);
        }
        log.info("批量删除告警事件: ids={}, count={}", ids, count);
        return count;
    }

    @Override
    public Integer countByNormalRuleIdAfter(Integer normalRuleId, Long startTime) {
        return alertResultMapper.countByNormalRuleIdAfter(normalRuleId, startTime);
    }
}
