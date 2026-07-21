package com.ankki.webapp.dao.config;

import com.ankki.webapp.model.config.AlertRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AlertRuleMapper {

    List<AlertRule> selectPage(@Param("keyword") String keyword,
                               @Param("logType") Byte logType,
                               @Param("ruleType") Byte ruleType,
                               @Param("status") Byte status,
                               @Param("offset") Integer offset,
                               @Param("limit") Integer limit);

    Integer countPage(@Param("keyword") String keyword,
                      @Param("logType") Byte logType,
                      @Param("ruleType") Byte ruleType,
                      @Param("status") Byte status);

    AlertRule selectById(@Param("id") Integer id);

    List<AlertRule> selectByCombineRuleId(@Param("combineRuleId") Integer combineRuleId);

    int insert(AlertRule record);

    int update(AlertRule record);

    int deleteById(@Param("id") Integer id, @Param("updateTime") Long updateTime);

    int batchDelete(@Param("ids") List<Integer> ids, @Param("updateTime") Long updateTime);
}
