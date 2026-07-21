package com.ankki.webapp.model.config;

import java.util.List;

public class AccessList {

    public static final byte TYPE_WHITELIST = 0;
    public static final byte TYPE_BLACKLIST = 1;

    public static final byte STATUS_DISABLED = 0;
    public static final byte STATUS_ENABLED = 1;

    private Integer id;
    private String name;
    private Byte type;
    private Byte status;
    private String remark;
    private Long createTime;
    private Long updateTime;
    private Byte isDelete;

    private Integer userCount;
    private List<AccessListUser> users;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Byte getType() { return type; }
    public void setType(Byte type) { this.type = type; }

    public String getTypeName() {
        if (type == null) return "";
        return type == TYPE_WHITELIST ? "白名单" : "黑名单";
    }

    public Byte getStatus() { return status; }
    public void setStatus(Byte status) { this.status = status; }

    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }

    public Long getCreateTime() { return createTime; }
    public void setCreateTime(Long createTime) { this.createTime = createTime; }

    public Long getUpdateTime() { return updateTime; }
    public void setUpdateTime(Long updateTime) { this.updateTime = updateTime; }

    public Byte getIsDelete() { return isDelete; }
    public void setIsDelete(Byte isDelete) { this.isDelete = isDelete; }

    public Integer getUserCount() { return userCount; }
    public void setUserCount(Integer userCount) { this.userCount = userCount; }

    public List<AccessListUser> getUsers() { return users; }
    public void setUsers(List<AccessListUser> users) { this.users = users; }
}
