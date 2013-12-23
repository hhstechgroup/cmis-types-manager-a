package com.engagepoint.teama.cmismanager.biz.util;

public class RepositoryInfo {

    private String username;
    private String password;
    private String url;
    private String repositoryName;

    public RepositoryInfo(String username, String password, String url, String repositoryName) {
        this.url = url;
        this.repositoryName = repositoryName;
        this.password = password;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RepositoryInfo that = (RepositoryInfo) o;

        if (password == null) {
            if (that.password != null) {
                return false;
            }
        } else {
            if ( !password.equals(that.password) ) {
                return false;
            }
        }

        if (username == null) {
            if (that.username != null) {
                return false;
            }
        } else {
            if ( !username.equals(that.username) ) {
                return false;
            }
        }

        if (repositoryName == null) {
            if (that.repositoryName != null) {
                return false;
            }
        } else {
            if ( !repositoryName.equals(that.repositoryName) ) {
                return false;
            }
        }

        if (url == null) {
            if (that.url != null) {
                return false;
            }
        } else {
            if ( !url.equals(that.url) ) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (repositoryName != null ? repositoryName.hashCode() : 0);
        return result;
    }
}
