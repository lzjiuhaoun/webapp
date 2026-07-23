package com.ankki.webapp.controller.config;

import com.ankki.webapp.common.WebResult;
import com.ankki.webapp.controller.BaseController;
import com.ankki.webapp.model.alert.AlertRawLog;
import com.ankki.webapp.service.AlertMatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 规则引擎测试入口（临时接口，上线前移除）.
 *
 * @author AAS-SIMP
 */
@Slf4j
@RestController
@RequestMapping("/alert-match")
public class AlertMatchTestController extends BaseController {

    @Autowired
    private AlertMatchService alertMatchService;

    /**
     * 测试规则匹配引擎.
     * <p>
     * 接收一条 AlertRawLog JSON，执行规则匹配并返回命中结果。
     * 匹配成功的规则会写入 alert_result 表。
     * </p>
     *
     * @param rawLog 标准化告警事件源数据
     * @return 匹配结果
     */
    @PostMapping("/test")
    public WebResult test(@RequestBody AlertRawLog rawLog) {
        log.info("规则引擎测试: recordId={}, logSource={}, operator={}, loginTime={}",
                rawLog != null ? rawLog.getRecordId() : null,
                rawLog != null ? rawLog.getLogSource() : null,
                rawLog != null ? rawLog.getOperatorAccount() : null,
                rawLog != null ? rawLog.getLoginTime() : null);

        if (rawLog == null) {
            log.warn("规则引擎测试失败: 请求体为空");
            return WebResult.error("请求体不能为空");
        }
        if (!StringUtils.hasText(rawLog.getLogSource())) {
            log.warn("规则引擎测试失败: logSource 为空");
            return WebResult.error("logSource 不能为空");
        }
        if (rawLog.getRecordId() == null) {
            log.warn("规则引擎测试失败: recordId 为空");
            return WebResult.error("recordId 不能为空");
        }

        try {
            int hitCount = alertMatchService.match(rawLog);

            Map<String, Object> data = new HashMap<>();
            data.put("hitCount", hitCount);
            data.put("matched", hitCount > 0);
            data.put("message", hitCount > 0
                    ? "命中 " + hitCount + " 条规则，结果已写入 alert_result 表"
                    : "未命中任何规则");

            log.info("规则引擎测试完成: recordId={}, matched={}, hitCount={}",
                    rawLog.getRecordId(), hitCount > 0, hitCount);
            return WebResult.success(data);
        } catch (Exception e) {
            log.error("规则引擎测试异常: recordId={}", rawLog.getRecordId(), e);
            return WebResult.error("规则引擎执行异常: " + e.getMessage());
        }
    }
}
