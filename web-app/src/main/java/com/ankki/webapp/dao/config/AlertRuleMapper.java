package com.ankki.webapp.dao.config;

import com.ankki.webapp.model.config.AlertRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 告警规则 MyBatis Mapper.
 * <p>
 * 提供告警规则表的基础数据访问操作，支持 MySQL 与 DM8 双数据源。
 * </p>
 *
 * @author AAS-SIMP
 */
@Mapper
public interface AlertRuleMapper {

    /**
     * 分页查询告警规则.
     *
     * @param keyword  名称关键词
     * @param logType  日志类型
     * @param ruleType 规则类型
     * @param status   状态
     * @param offset   偏移量
     * @param limit    每页条数
     * @return 告警规则列表
     */
    List<AlertRule> selectPage(@Param("keyword") String keyword,
                               @Param("logType") Byte logType,
                               @Param("ruleType") Byte ruleType,
                               @Param("status") Byte status,
                               @Param("offset") Integer offset,
                               @Param("limit") Integer limit);

    /**
     * 统计告警规则总数（分页用）.
     *
     * @param keyword  名称关键词
     * @param logType  日志类型
     * @param ruleType 规则类型
     * @param status   状态
     * @return 总记录数
     */
    Integer countPage(@Param("keyword") String keyword,
                      @Param("logType") Byte logType,
                      @Param("ruleType") Byte ruleType,
                      @Param("status") Byte status);

    /**
     * 根据ID查询（仅未删除的记录）.
     *
     * @param id 规则ID
     * @return 告警规则实体
     */
    AlertRule selectById(@Param("id") Integer id);

    /**
     * 查询引用指定普通规则的所有组合规则.
     *
     * @param combineRuleId 普通规则ID
     * @return 组合规则列表
     */
    List<AlertRule> selectByCombineRuleId(@Param("combineRuleId") Integer combineRuleId);

    /**
     * 新增告警规则.
     *
     * @param record 告警规则实体
     * @return 受影响行数
     */
    int insert(AlertRule record);

    /**
     * 更新告警规则（全字段覆盖）.
     *
     * @param record 告警规则实体
     * @return 受影响行数
     */
    int update(AlertRule record);

    /**
     * 软删除告警规则.
     *
     * @param id         规则ID
     * @param updateTime 删除时的更新时间戳
     * @return 受影响行数
     */
    int deleteById(@Param("id") Integer id, @Param("updateTime") Long updateTime);

    /**
     * 批量软删除告警规则.
     *
     * @param ids        规则ID列表
     * @param updateTime 删除时的更新时间戳
     * @return 受影响行数
     */
    int batchDelete(@Param("ids") List<Integer> ids, @Param("updateTime") Long updateTime);
}
