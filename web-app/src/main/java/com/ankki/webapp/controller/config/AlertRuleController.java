package com.ankki.webapp.controller.config;

import com.ankki.webapp.common.WebResult;
import com.ankki.webapp.controller.BaseController;
import com.ankki.webapp.model.config.AlertRule;
import com.ankki.webapp.service.AlertRuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 告警规则管理 REST 控制器.
 * <p>
 * 提供告警规则的增删改查、状态切换等接口。
 * </p>
 *
 * @author AAS-SIMP
 */
@Slf4j
@RestController
@RequestMapping("/alert-rule")
public class AlertRuleController extends BaseController {

    /** 告警规则业务服务 */
    @Autowired
    private AlertRuleService alertRuleService;

    /**
     * 分页查询告警规则列表.
     *
     * @param pageNo   页码，从1开始，默认1
     * @param pageSize 每页条数，默认10
     * @param keyword  规则名称关键词（模糊搜索）
     * @param logType  日志类型筛选
     * @param ruleType 规则类型筛选（0=普通规则, 1=组合规则）
     * @param status   状态筛选（0=禁用, 1=启用）
     * @return 分页结果，包含 pageNo, pageSize, total, list
     */
    @GetMapping
    public WebResult page(@RequestParam(defaultValue = "1") Integer pageNo,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Byte logType,
                          @RequestParam(required = false) Byte ruleType,
                          @RequestParam(required = false) Byte status) {
        log.info("分页查询告警规则: pageNo={}, pageSize={}, keyword={}, logType={}, ruleType={}, status={}",
                pageNo, pageSize, keyword, logType, ruleType, status);
        Map<String, Object> result = alertRuleService.page(keyword, logType, ruleType, status, pageNo, pageSize);
        return WebResult.success(result);
    }

    /**
     * 查询告警规则详情.
     *
     * @param id 规则ID
     * @return 告警规则实体
     */
    @GetMapping("/detail")
    public WebResult detail(@RequestParam Integer id) {
        log.info("查询告警规则详情: id={}", id);
        AlertRule record = alertRuleService.detail(id);
        if (record == null) {
            log.warn("告警规则不存在: id={}", id);
            return WebResult.error("告警规则不存在");
        }
        return WebResult.success(record);
    }

    /**
     * 新增告警规则.
     *
     * @param record 告警规则实体（JSON请求体）
     * @return 创建成功后的规则ID
     */
    @PostMapping
    public WebResult add(@RequestBody AlertRule record) {
        log.info("新增告警规则: name={}, logType={}, ruleType={}", record.getName(), record.getLogType(), record.getRuleType());
        if (record == null || !StringUtils.hasText(record.getName())) {
            log.warn("新增告警规则失败: 规则名称为空");
            return WebResult.error("规则名称不能为空");
        }
        if (record.getLogType() == null) {
            log.warn("新增告警规则失败: 未选择日志类型");
            return WebResult.error("请选择日志类型");
        }
        if (record.getRuleType() == null) {
            log.warn("新增告警规则失败: 未选择规则类型");
            return WebResult.error("请选择规则类型");
        }
        if (record.getLevel() == null) {
            log.warn("新增告警规则失败: 未选择告警等级");
            return WebResult.error("请选择告警等级");
        }
        try {
            Integer id = alertRuleService.add(record);
            log.info("新增告警规则成功: id={}, name={}", id, record.getName());
            return WebResult.success("创建成功", id);
        } catch (IllegalArgumentException e) {
            log.warn("新增告警规则失败: {}", e.getMessage());
            return WebResult.error(e.getMessage());
        }
    }

    /**
     * 更新告警规则.
     *
     * @param record 告警规则实体（JSON请求体，必须包含ID）
     * @return 更新后的规则ID
     */
    @PutMapping
    public WebResult update(@RequestBody AlertRule record) {
        log.info("更新告警规则: id={}, name={}", record != null ? record.getId() : null, record != null ? record.getName() : null);
        if (record == null || record.getId() == null) {
            log.warn("更新告警规则失败: 参数错误");
            return WebResult.error("参数错误");
        }
        if (!StringUtils.hasText(record.getName())) {
            log.warn("更新告警规则失败: 规则名称为空");
            return WebResult.error("规则名称不能为空");
        }
        try {
            alertRuleService.update(record);
            log.info("更新告警规则成功: id={}", record.getId());
            return WebResult.success("更新成功", record.getId());
        } catch (IllegalArgumentException e) {
            log.warn("更新告警规则失败: {}", e.getMessage());
            return WebResult.error(e.getMessage());
        }
    }

    /**
     * 删除告警规则（批量）.
     * <p>
     * 支持单条和批量删除。若规则已被引用（如普通规则被组合规则引用），则拒绝删除。
     * </p>
     *
     * @param ids 规则ID列表
     * @return 成功删除的条数
     */
    @PostMapping("/delete")
    public WebResult delete(@RequestBody List<Integer> ids) {
        log.info("删除告警规则: ids={}", ids);
        if (ids == null || ids.isEmpty()) {
            log.warn("删除告警规则失败: 未选择条目");
            return WebResult.error("请选择要删除的条目");
        }
        try {
            int count;
            if (ids.size() == 1) {
                count = alertRuleService.delete(ids.get(0));
            } else {
                count = alertRuleService.batchDelete(ids);
            }
            log.info("删除告警规则成功: count={}", count);
            return WebResult.success("成功删除" + count + "条", count);
        } catch (IllegalArgumentException e) {
            log.warn("删除告警规则失败: {}", e.getMessage());
            return WebResult.error(e.getMessage());
        }
    }

    /**
     * 切换告警规则启用/禁用状态.
     * <p>
     * 若普通规则已被组合规则引用，则禁止切换为禁用状态。
     * </p>
     *
     * @param body 包含 id 的JSON对象
     * @return 操作结果
     */
    @PutMapping("/status")
    public WebResult toggleStatus(@RequestBody Map<String, Integer> body) {
        Integer id = body != null ? body.get("id") : null;
        log.info("切换告警规则状态: id={}", id);
        if (id == null) {
            log.warn("切换告警规则状态失败: 参数错误");
            return WebResult.error("参数错误");
        }
        try {
            int count = alertRuleService.toggleStatus(id);
            if (count > 0) {
                log.info("切换告警规则状态成功: id={}", id);
                return WebResult.success("状态已更新");
            } else {
                log.warn("切换告警规则状态失败: 规则不存在, id={}", id);
                return WebResult.error("告警规则不存在");
            }
        } catch (IllegalArgumentException e) {
            log.warn("切换告警规则状态失败: {}", e.getMessage());
            return WebResult.error(e.getMessage());
        }
    }
}
