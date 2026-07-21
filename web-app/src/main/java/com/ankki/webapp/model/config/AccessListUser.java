package com.ankki.webapp.model.config;

public class AccessListUser {

    private Integer id;
    private Integer listId;
    private String userName;
    private String userAccount;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getListId() { return listId; }
    public void setListId(Integer listId) { this.listId = listId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserAccount() { return userAccount; }
    public void setUserAccount(String userAccount) { this.userAccount = userAccount; }
}
