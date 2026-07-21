package com.ankki.webapp.controller.config;

import com.ankki.webapp.common.WebResult;
import com.ankki.webapp.controller.BaseController;
import com.ankki.webapp.model.config.AlertRule;
import com.ankki.webapp.service.AlertRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alert-rule")
public class AlertRuleController extends BaseController {

    @Autowired
    private AlertRuleService alertRuleService;

    @GetMapping
    public WebResult page(@RequestParam(defaultValue = "1") Integer pageNo,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Byte logType,
                          @RequestParam(required = false) Byte ruleType,
                          @RequestParam(required = false) Byte status) {
        Map<String, Object> result = alertRuleService.page(keyword, logType, ruleType, status, pageNo, pageSize);
        return WebResult.success(result);
    }

    @GetMapping("/detail")
    public WebResult detail(@RequestParam Integer id) {
        AlertRule record = alertRuleService.detail(id);
        if (record == null) {
            return WebResult.error("告警规则不存在");
        }
        return WebResult.success(record);
    }

    @PostMapping
    public WebResult add(@RequestBody AlertRule record) {
        if (record == null || !StringUtils.hasText(record.getName())) {
            return WebResult.error("规则名称不能为空");
        }
        if (record.getLogType() == null) {
            return WebResult.error("请选择日志类型");
        }
        if (record.getRuleType() == null) {
            return WebResult.error("请选择规则类型");
        }
        if (record.getLevel() == null) {
            return WebResult.error("请选择告警等级");
        }
        try {
            Integer id = alertRuleService.add(record);
            return WebResult.success("创建成功", id);
        } catch (IllegalArgumentException e) {
            return WebResult.error(e.getMessage());
        }
    }

    @PutMapping
    public WebResult update(@RequestBody AlertRule record) {
        if (record == null || record.getId() == null) {
            return WebResult.error("参数错误");
        }
        if (!StringUtils.hasText(record.getName())) {
            return WebResult.error("规则名称不能为空");
        }
        try {
            alertRuleService.update(record);
            return WebResult.success("更新成功", record.getId());
        } catch (IllegalArgumentException e) {
            return WebResult.error(e.getMessage());
        }
    }

    @PostMapping("/delete")
    public WebResult delete(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return WebResult.error("请选择要删除的条目");
        }
        try {
            int count;
            if (ids.size() == 1) {
                count = alertRuleService.delete(ids.get(0));
            } else {
                count = alertRuleService.batchDelete(ids);
            }
            return WebResult.success("成功删除" + count + "条", count);
        } catch (IllegalArgumentException e) {
            return WebResult.error(e.getMessage());
        }
    }

    @PutMapping("/status")
    public WebResult toggleStatus(@RequestBody Map<String, Integer> body) {
        Integer id = body != null ? body.get("id") : null;
        if (id == null) {
            return WebResult.error("参数错误");
        }
        try {
            int count = alertRuleService.toggleStatus(id);
            return count > 0 ? WebResult.success("状态已更新") : WebResult.error("告警规则不存在");
        } catch (IllegalArgumentException e) {
            return WebResult.error(e.getMessage());
        }
    }
}
