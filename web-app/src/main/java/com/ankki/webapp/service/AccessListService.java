package com.ankki.webapp.service;

import com.ankki.webapp.model.config.AccessList;
import com.ankki.webapp.model.config.AccessListUser;

import java.util.List;
import java.util.Map;

/**
 * 黑白名单服务接口.
 * <p>
 * 定义黑白名单的业务操作契约。黑白名单可被告警规则引用，
 * 被引用时禁止删除和禁用。
 * </p>
 *
 * @author AAS-SIMP
 */
public interface AccessListService {

    /**
     * 分页查询黑白名单.
     *
     * @param keyword  名称关键词（可选）
     * @param type     类型筛选（可选：0=白名单, 1=黑名单）
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return 包含 pageNo, pageSize, total, list 的分页Map
     */
    Map<String, Object> page(String keyword, Byte type, Integer pageNo, Integer pageSize);

    /**
     * 查询黑白名单详情（含用户明细列表）.
     *
     * @param id 黑白名单ID
     * @return 黑白名单实体及关联用户
     */
    AccessList detail(Integer id);

    /**
     * 根据ID查询黑白名单（不含用户明细）.
     *
     * @param id 黑白名单ID
     * @return 黑白名单实体
     */
    AccessList getById(Integer id);

    /**
     * 查询所有已启用的黑白名单.
     *
     * @param type 类型筛选（可选）
     * @return 已启用的黑白名单列表
     */
    List<AccessList> getEnabledLists(Byte type);

    /**
     * 新增黑白名单（含用户列表）.
     *
     * @param record 黑白名单实体
     * @return 新记录的ID
     */
    Integer add(AccessList record);

    /**
     * 更新黑白名单（用户列表全量替换）.
     *
     * @param record 黑白名单实体（必须包含ID）
     * @return 更新后的ID
     */
    Integer update(AccessList record);

    /**
     * 删除黑白名单.
     *
     * @param id 黑白名单ID
     * @return 受影响行数
     * @throws IllegalArgumentException 该名单已被告警规则引用
     */
    Integer delete(Integer id);

    /**
     * 批量删除黑白名单.
     *
     * @param ids 黑白名单ID列表
     * @return 受影响行数
     * @throws IllegalArgumentException 其中某条名单已被告警规则引用
     */
    Integer batchDelete(List<Integer> ids);

    /**
     * 切换黑白名单启用/禁用状态.
     *
     * @param id 黑白名单ID
     * @return 受影响行数
     * @throws IllegalArgumentException 该名单已被告警规则引用
     */
    Integer toggleStatus(Integer id);
}
