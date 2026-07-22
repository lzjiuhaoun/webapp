package com.ankki.webapp.dao.config;

import com.ankki.webapp.model.config.AccessList;
import com.ankki.webapp.model.config.AccessListUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 黑白名单 MyBatis Mapper.
 * <p>
 * 提供黑白名单主表及用户明细表的数据访问操作，支持 MySQL 与 DM8 双数据源。
 * </p>
 *
 * @author AAS-SIMP
 */
@Mapper
public interface AccessListMapper {

    // ===== 主表 CRUD =====

    /**
     * 分页查询黑白名单.
     *
     * @param keyword 名称关键词
     * @param type    类型（0=白名单, 1=黑名单）
     * @param offset  偏移量
     * @param limit   每页条数
     * @return 黑白名单列表（含用户数）
     */
    List<AccessList> selectPage(@Param("keyword") String keyword,
                                @Param("type") Byte type,
                                @Param("offset") Integer offset,
                                @Param("limit") Integer limit);

    /**
     * 统计黑白名单总数.
     *
     * @param keyword 名称关键词
     * @param type    类型
     * @return 总记录数
     */
    Integer countPage(@Param("keyword") String keyword,
                      @Param("type") Byte type);

    /**
     * 根据ID查询.
     *
     * @param id 黑白名单ID
     * @return 黑白名单实体
     */
    AccessList selectById(@Param("id") Integer id);

    /**
     * 查询所有已启用的黑白名单.
     *
     * @param type 类型筛选
     * @return 已启用列表
     */
    List<AccessList> selectAllEnabled(@Param("type") Byte type);

    /**
     * 查询指定ID且已启用的黑白名单.
     *
     * @param id 黑白名单ID
     * @return 黑白名单实体
     */
    AccessList selectEnabledById(@Param("id") Integer id);

    /**
     * 统计黑白名单被告警规则引用的次数.
     *
     * @param id 黑白名单ID
     * @return 引用该名单的告警规则数
     */
    Integer countByListId(@Param("id") Integer id);

    /**
     * 新增黑白名单.
     *
     * @param record 黑白名单实体
     * @return 受影响行数
     */
    int insert(AccessList record);

    /**
     * 更新黑白名单.
     *
     * @param record 黑白名单实体
     * @return 受影响行数
     */
    int update(AccessList record);

    /**
     * 软删除黑白名单.
     *
     * @param id         黑白名单ID
     * @param updateTime 删除时的更新时间戳
     * @return 受影响行数
     */
    int deleteById(@Param("id") Integer id, @Param("updateTime") Long updateTime);

    // ===== 用户明细 CRUD =====

    /**
     * 查询指定黑白名单下的用户列表.
     *
     * @param listId 黑白名单ID
     * @return 用户列表
     */
    List<AccessListUser> selectUsersByListId(@Param("listId") Integer listId);

    /**
     * 批量插入用户明细.
     *
     * @param listId 黑白名单ID
     * @param users  用户列表
     * @return 受影响行数
     */
    int insertUsers(@Param("listId") Integer listId,
                    @Param("users") List<AccessListUser> users);

    /**
     * 删除指定黑白名单下的所有用户明细.
     *
     * @param listId 黑白名单ID
     * @return 受影响行数
     */
    int deleteUsersByListId(@Param("listId") Integer listId);
}
