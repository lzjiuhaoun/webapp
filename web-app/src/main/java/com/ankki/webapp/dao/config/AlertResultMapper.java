package com.ankki.webapp.dao.config;

import com.ankki.webapp.model.alert.AlertResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 告警事件 MyBatis Mapper.
 * <p>
 * 提供告警事件表的基础数据访问操作，支持 MySQL 与 DM8 双数据源。
 * 审计日志明细（ID + 席位 IP）请使用 {@link AlertResultAuditMapper}。
 * </p>
 *
 * @author AAS-SIMP
 */
@Mapper
public interface AlertResultMapper {

    /**
     * 分页查询告警事件.
     *
     * @param keyword   规则名称关键词
     * @param alertLevel 告警等级
     * @param ruleType  规则类型
     * @param offset    偏移量
     * @param limit     每页条数
     * @return 告警事件列表
     */
    List<AlertResult> selectPage(@Param("keyword") String keyword,
                                 @Param("alertLevel") Byte alertLevel,
                                 @Param("ruleType") Byte ruleType,
                                 @Param("offset") Integer offset,
                                 @Param("limit") Integer limit);

    /**
     * 统计告警事件总数（分页用）.
     *
     * @param keyword   规则名称关键词
     * @param alertLevel 告警等级
     * @param ruleType  规则类型
     * @return 总记录数
     */
    Integer countPage(@Param("keyword") String keyword,
                      @Param("alertLevel") Byte alertLevel,
                      @Param("ruleType") Byte ruleType);

    /**
     * 根据ID查询.
     *
     * @param id 主键ID
     * @return 告警事件
     */
    AlertResult selectById(@Param("id") Integer id);

    /**
     * 新增告警事件.
     *
     * @param record 告警事件
     * @return 受影响行数
     */
    int insert(AlertResult record);

    /**
     * 删除告警事件.
     *
     * @param id 主键ID
     * @return 受影响行数
     */
    int deleteById(@Param("id") Integer id);

    /**
     * 统计指定普通规则在时间范围内的命中次数.
     *
     * @param normalRuleId 普通规则ID
     * @param startTime    起始时间戳
     * @return 命中次数
     */
    Integer countByNormalRuleIdAfter(@Param("normalRuleId") Integer normalRuleId,
                                     @Param("startTime") Long startTime);
}
