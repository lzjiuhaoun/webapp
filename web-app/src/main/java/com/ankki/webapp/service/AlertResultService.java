package com.ankki.webapp.service;

import com.ankki.webapp.model.alert.AlertResult;

import java.util.List;
import java.util.Map;

/**
 * 告警事件业务服务接口.
 * <p>
 * 提供告警事件的查询、删除等管理操作。
 * 告警事件的创建由规则引擎驱动，不对外提供新增接口。
 * </p>
 *
 * @author AAS-SIMP
 */
public interface AlertResultService {

    /**
     * 分页查询告警事件.
     *
     * @param keyword   规则名称关键词
     * @param alertLevel 告警等级
     * @param ruleType  规则类型
     * @param pageNo    页码
     * @param pageSize  每页条数
     * @return 分页结果
     */
    Map<String, Object> page(String keyword, Byte alertLevel, Byte ruleType,
                             Integer pageNo, Integer pageSize);

    /**
     * 查询告警事件详情.
     *
     * @param id 主键ID
     * @return 告警事件
     */
    AlertResult detail(Integer id);

    /**
     * 删除告警事件.
     *
     * @param id 主键ID
     * @return 受影响行数
     */
    Integer delete(Integer id);

    /**
     * 批量删除告警事件.
     *
     * @param ids ID列表
     * @return 受影响行数
     */
    Integer batchDelete(List<Integer> ids);

    /**
     * 统计指定普通规则在时间范围内的命中次数.
     *
     * @param normalRuleId 普通规则ID
     * @param startTime    起始时间戳
     * @return 命中次数
     */
    Integer countByNormalRuleIdAfter(Integer normalRuleId, Long startTime);
}
