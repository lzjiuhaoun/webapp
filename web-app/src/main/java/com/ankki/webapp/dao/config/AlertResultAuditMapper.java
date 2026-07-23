package com.ankki.webapp.dao.config;

import com.ankki.webapp.model.alert.AlertResultAudit;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 告警事件审计日志明细 MyBatis Mapper.
 * <p>
 * 提供告警事件审计日志明细表的基础数据访问操作，支持 MySQL 与 DM8 双数据源。
 * </p>
 *
 * @author AAS-SIMP
 */
@Mapper
public interface AlertResultAuditMapper {

    /**
     * 新增审计日志明细.
     *
     * @param record 审计日志明细
     * @return 受影响行数
     */
    int insert(AlertResultAudit record);

    /**
     * 批量新增审计日志明细.
     *
     * @param records 审计日志明细列表
     * @return 受影响行数
     */
    int insertBatch(@Param("records") List<AlertResultAudit> records);

    /**
     * 根据告警事件ID查询审计日志明细列表.
     *
     * @param alertResultId 告警事件ID
     * @return 审计日志明细列表
     */
    List<AlertResultAudit> selectByAlertResultId(@Param("alertResultId") Integer alertResultId);

    /**
     * 根据告警事件ID删除审计日志明细.
     *
     * @param alertResultId 告警事件ID
     * @return 受影响行数
     */
    int deleteByAlertResultId(@Param("alertResultId") Integer alertResultId);
}
