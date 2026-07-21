package com.ankki.webapp.service.impl;

import com.ankki.webapp.dao.config.AccessListMapper;
import com.ankki.webapp.model.config.AccessList;
import com.ankki.webapp.model.config.AccessListUser;
import com.ankki.webapp.service.AccessListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AccessListServiceImpl implements AccessListService {

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

    @Override
    @Transactional
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
        return listId;
    }

    @Override
    @Transactional
    public Integer update(AccessList record) {
        record.setUpdateTime(System.currentTimeMillis());
        accessListMapper.update(record);
        accessListMapper.deleteUsersByListId(record.getId());
        List<AccessListUser> users = record.getUsers();
        if (users != null && !users.isEmpty()) {
            for (AccessListUser u : users) {
                u.setListId(record.getId());
            }
            accessListMapper.insertUsers(record.getId(), users);
        }
        return record.getId();
    }

    @Override
    @Transactional
    public Integer delete(Integer id) {
        long now = System.currentTimeMillis();
        accessListMapper.deleteUsersByListId(id);
        return accessListMapper.deleteById(id, now);
    }

    @Override
    @Transactional
    public Integer batchDelete(List<Integer> ids) {
        long now = System.currentTimeMillis();
        int count = 0;
        for (Integer id : ids) {
            accessListMapper.deleteUsersByListId(id);
            count += accessListMapper.deleteById(id, now);
        }
        return count;
    }

    @Override
    @Transactional
    public Integer toggleStatus(Integer id) {
        AccessList record = accessListMapper.selectById(id);
        if (record == null) return 0;
        record.setStatus(record.getStatus() == AccessList.STATUS_ENABLED
                ? AccessList.STATUS_DISABLED : AccessList.STATUS_ENABLED);
        record.setUpdateTime(System.currentTimeMillis());
        return accessListMapper.update(record);
    }
}
