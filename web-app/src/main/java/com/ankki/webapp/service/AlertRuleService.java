package com.ankki.webapp.service;

import com.ankki.webapp.model.config.AlertRule;

import java.util.List;
import java.util.Map;

/**
 * 告警规则服务接口.
 * <p>
 * 定义告警规则的业务操作契约，包括分页查询、详情、新增、更新、
 * 删除（含批量）和状态切换。
 * </p>
 *
 * @author AAS-SIMP
 */
public interface AlertRuleService {

    /**
     * 分页查询告警规则.
     *
     * @param keyword  名称关键词（可选）
     * @param logType  日志类型筛选（可选）
     * @param ruleType 规则类型筛选（可选）
     * @param status   状态筛选（可选）
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return 包含 pageNo, pageSize, total, list 的分页Map
     */
    Map<String, Object> page(String keyword, Byte logType, Byte ruleType, Byte status,
                             Integer pageNo, Integer pageSize);

    /**
     * 查询告警规则详情.
     *
     * @param id 规则ID
     * @return 告警规则实体，不存在时返回null
     */
    AlertRule detail(Integer id);

    /**
     * 新增告警规则.
     *
     * @param record 告警规则实体
     * @return 新规则的ID
     * @throws IllegalArgumentException 关联的黑白名单不存在或已禁用
     */
    Integer add(AlertRule record);

    /**
     * 更新告警规则.
     *
     * @param record 告警规则实体（必须包含ID）
     * @return 更新后的规则ID
     * @throws IllegalArgumentException 关联的黑白名单不存在或已禁用
     */
    Integer update(AlertRule record);

    /**
     * 删除告警规则.
     *
     * @param id 规则ID
     * @return 受影响行数
     * @throws IllegalArgumentException 普通规则已被组合规则引用
     */
    Integer delete(Integer id);

    /**
     * 批量删除告警规则.
     *
     * @param ids 规则ID列表
     * @return 受影响行数
     * @throws IllegalArgumentException 其中某条普通规则已被组合规则引用
     */
    Integer batchDelete(List<Integer> ids);

    /**
     * 切换告警规则启用/禁用状态.
     *
     * @param id 规则ID
     * @return 受影响行数
     * @throws IllegalArgumentException 普通规则已被组合规则引用
     */
    Integer toggleStatus(Integer id);
}
