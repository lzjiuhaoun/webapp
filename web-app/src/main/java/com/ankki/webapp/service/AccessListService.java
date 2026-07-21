package com.ankki.webapp.service;

import com.ankki.webapp.model.config.AccessList;
import com.ankki.webapp.model.config.AccessListUser;

import java.util.List;
import java.util.Map;

public interface AccessListService {

    Map<String, Object> page(String keyword, Byte type, Integer pageNo, Integer pageSize);

    AccessList detail(Integer id);

    AccessList getById(Integer id);

    List<AccessList> getEnabledLists(Byte type);

    Integer add(AccessList record);

    Integer update(AccessList record);

    Integer delete(Integer id);

    Integer batchDelete(List<Integer> ids);

    Integer toggleStatus(Integer id);
}
