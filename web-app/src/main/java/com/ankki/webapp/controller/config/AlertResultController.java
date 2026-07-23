package com.ankki.webapp.controller.config;

import com.ankki.webapp.common.WebResult;
import com.ankki.webapp.controller.BaseController;
import com.ankki.webapp.model.alert.AlertResult;
import com.ankki.webapp.service.AlertResultService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 告警事件管理 REST 控制器.
 * <p>
 * 提供告警事件的查询和删除操作。告警事件的创建由规则引擎驱动，不对外提供新增接口。
 * </p>
 *
 * @author AAS-SIMP
 */
@Slf4j
@RestController
@RequestMapping("/alert-result")
public class AlertResultController extends BaseController {

    @Autowired
    private AlertResultService alertResultService;

    /**
     * 分页查询告警事件.
     *
     * @param pageNo     页码，从1开始，默认1
     * @param pageSize   每页条数，默认10
     * @param keyword    规则名称关键词
     * @param alertLevel 告警等级
     * @param ruleType   规则类型
     * @return 分页结果
     */
    @GetMapping
    public WebResult page(@RequestParam(defaultValue = "1") Integer pageNo,
                          @RequestParam(defaultValue = "10") Integer pageSize,
                          @RequestParam(required = false) String keyword,
                          @RequestParam(required = false) Byte alertLevel,
                          @RequestParam(required = false) Byte ruleType) {
        log.info("分页查询告警事件: pageNo={}, pageSize={}, keyword={}, alertLevel={}, ruleType={}",
                pageNo, pageSize, keyword, alertLevel, ruleType);
        Map<String, Object> result = alertResultService.page(keyword, alertLevel, ruleType, pageNo, pageSize);
        return WebResult.success(result);
    }

    /**
     * 查询告警事件详情.
     *
     * @param id 主键ID
     * @return 告警事件
     */
    @GetMapping("/detail")
    public WebResult detail(@RequestParam Integer id) {
        log.info("查询告警事件详情: id={}", id);
        AlertResult record = alertResultService.detail(id);
        if (record == null) {
            log.warn("告警事件不存在: id={}", id);
            return WebResult.error("告警事件不存在");
        }
        return WebResult.success(record);
    }

    /**
     * 删除告警事件（批量）.
     *
     * @param ids ID列表
     * @return 成功删除的条数
     */
    @PostMapping("/delete")
    public WebResult delete(@RequestBody List<Integer> ids) {
        log.info("删除告警事件: ids={}", ids);
        if (ids == null || ids.isEmpty()) {
            log.warn("删除告警事件失败: 未选择条目");
            return WebResult.error("请选择要删除的条目");
        }
        int count;
        if (ids.size() == 1) {
            count = alertResultService.delete(ids.get(0));
        } else {
            count = alertResultService.batchDelete(ids);
        }
        log.info("删除告警事件成功: count={}", count);
        return WebResult.success("成功删除" + count + "条", count);
    }
}
