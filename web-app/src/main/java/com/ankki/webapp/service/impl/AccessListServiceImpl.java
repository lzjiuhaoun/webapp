package com.ankki.webapp.service.impl;

import com.ankki.webapp.dao.config.AccessListMapper;
import com.ankki.webapp.model.config.AccessList;
import com.ankki.webapp.model.config.AccessListUser;
import com.ankki.webapp.service.AccessListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 黑白名单服务实现.
 * <p>
 * 实现黑白名单的业务逻辑。黑白名单被删除或禁用时，
 * 需检查是否被告警规则引用，防止产生脏数据。
 * </p>
 *
 * @author AAS-SIMP
 */
@Slf4j
@Service
public class AccessListServiceImpl implements AccessListService {

    /** 黑白名单数据访问层 */
    @Autowired
    private AccessListMapper accessListMapper;

    @Override
    public Map<String, Object> page(String keyword, Byte type, Integer pageNo, Integer pageSize) {
        if (pageNo == null || pageNo < 1) pageNo = 1;
        if (pageSize == null || pageSize < 1) pageSize = 10;
        Integer offset = (pageNo - 1) * pageSize;

        List<AccessList> list = accessListMapper.selectPage(keyword, type, offset, pageSize);
        Integer total = accessListMapper.countPage(keyword, type);

        Map<String, Object> result = new HashMap<>();
        result.put("pageNo", pageNo);
        result.put("pageSize", pageSize);
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    @Override
    public AccessList detail(Integer id) {
        AccessList record = accessListMapper.selectById(id);
        if (record != null) {
            List<AccessListUser> users = accessListMapper.selectUsersByListId(id);
            record.setUsers(users);
        }
        return record;
    }

    @Override
    public AccessList getById(Integer id) {
        return accessListMapper.selectById(id);
    }

    @Override
    public List<AccessList> getEnabledLists(Byte type) {
        return accessListMapper.selectAllEnabled(type);
    }

    /**
     * 检查黑白名单是否被告警规则引用.
     *
     * @param listId 黑白名单ID
     * @throws IllegalArgumentException 已被引用
     */
    private void checkNotReferenced(Integer listId) {
        Integer count = accessListMapper.countByListId(listId);
        if (count != null && count > 0) {
            log.warn("黑白名单已被规则引用，无法操作: listId={}", listId);
            throw new IllegalArgumentException("该黑白名单已被告警规则引用，无法操作");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer add(AccessList record) {
        long now = System.currentTimeMillis();
        record.setStatus(AccessList.STATUS_ENABLED);
        record.setCreateTime(now);
        record.setUpdateTime(now);
        accessListMapper.insert(record);
        Integer listId = record.getId();
        List<AccessListUser> users = record.getUsers();
        if (users != null && !users.isEmpty()) {
            for (AccessListUser u : users) {
                u.setListId(listId);
            }
            accessListMapper.insertUsers(listId, users);
        }
        log.info("新增黑白名单: id={}, name={}, type={}, userCount={}", listId, record.getName(), record.getType(),
                users != null ? users.size() : 0);
        return listId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer update(AccessList record) {
        record.setUpdateTime(System.currentTimeMillis());
        accessListMapper.update(record);
        // 全量替换用户明细：先删后插
        accessListMapper.deleteUsersByListId(record.getId());
        List<AccessListUser> users = record.getUsers();
        if (users != null && !users.isEmpty()) {
            for (AccessListUser u : users) {
                u.setListId(record.getId());
            }
            accessListMapper.insertUsers(record.getId(), users);
        }
        log.info("更新黑白名单: id={}, name={}, userCount={}", record.getId(), record.getName(),
                users != null ? users.size() : 0);
        return record.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer delete(Integer id) {
        checkNotReferenced(id);
        long now = System.currentTimeMillis();
        accessListMapper.deleteUsersByListId(id);
        int count = accessListMapper.deleteById(id, now);
        log.info("删除黑白名单: id={}, result={}", id, count);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDelete(List<Integer> ids) {
        long now = System.currentTimeMillis();
        int count = 0;
        for (Integer id : ids) {
            checkNotReferenced(id);
            accessListMapper.deleteUsersByListId(id);
            count += accessListMapper.deleteById(id, now);
        }
        log.info("批量删除黑白名单: ids={}, count={}", ids, count);
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer toggleStatus(Integer id) {
        checkNotReferenced(id);
        AccessList record = accessListMapper.selectById(id);
        if (record == null) {
            log.warn("切换状态失败: 黑白名单不存在, id={}", id);
            return 0;
        }
        byte newStatus = record.getStatus() == AccessList.STATUS_ENABLED
                ? AccessList.STATUS_DISABLED : AccessList.STATUS_ENABLED;
        record.setStatus(newStatus);
        record.setUpdateTime(System.currentTimeMillis());
        int count = accessListMapper.update(record);
        log.info("切换黑白名单状态: id={}, name={}, newStatus={}", id, record.getName(), newStatus);
        return count;
    }
}
