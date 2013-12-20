package com.engagepoint.teama.cmismanager;

/**
 * Created with IntelliJ IDEA.
 * User: v.kondratenko
 * Date: 12/19/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserProperty {
    private String userName;
    private String userPassword;
    private String repositoryName;

    public UserProperty(String userName, String userPassword, String repositoryName) {
        this.userName = userName;
        this.repositoryName = repositoryName;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
    }

}
