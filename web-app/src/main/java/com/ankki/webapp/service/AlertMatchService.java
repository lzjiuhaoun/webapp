package com.ankki.webapp.service;

import com.ankki.webapp.model.alert.AlertRawLog;

/**
 * 告警规则匹配引擎服务接口.
 * <p>
 * 接收一条标准化后的告警事件源数据（AlertRawLog），
 * 按日志来源筛选已启用的普通规则，逐条进行条件匹配和黑白名单校验，
 * 命中后写入告警事件表。
 * </p>
 *
 * @author AAS-SIMP
 */
public interface AlertMatchService {

    /**
     * 匹配并处理一条告警事件源数据.
     * <p>
     * 同步执行：查询匹配的规则 → 条件校验 → 黑白名单校验 → 写入告警事件表。
     * </p>
     *
     * @param rawLog 标准化后的告警事件源数据
     * @return 命中的规则数量（写入 alert_result 的记录数）
     */
    int match(AlertRawLog rawLog);
}
