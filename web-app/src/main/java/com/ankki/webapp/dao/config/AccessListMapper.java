package com.ankki.webapp.dao.config;

import com.ankki.webapp.model.config.AccessList;
import com.ankki.webapp.model.config.AccessListUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccessListMapper {

    // ===== 主表 CRUD =====
    List<AccessList> selectPage(@Param("keyword") String keyword,
                                @Param("type") Byte type,
                                @Param("offset") Integer offset,
                                @Param("limit") Integer limit);

    Integer countPage(@Param("keyword") String keyword,
                      @Param("type") Byte type);

    AccessList selectById(@Param("id") Integer id);

    List<AccessList> selectAllEnabled(@Param("type") Byte type);

    int insert(AccessList record);

    int update(AccessList record);

    int deleteById(@Param("id") Integer id, @Param("updateTime") Long updateTime);

    // ===== 用户明细 CRUD =====
    List<AccessListUser> selectUsersByListId(@Param("listId") Integer listId);

    int insertUsers(@Param("listId") Integer listId,
                    @Param("users") List<AccessListUser> users);

    int deleteUsersByListId(@Param("listId") Integer listId);
}
