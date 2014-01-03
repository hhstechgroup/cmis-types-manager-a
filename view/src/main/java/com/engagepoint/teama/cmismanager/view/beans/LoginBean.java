package com.engagepoint.teama.cmismanager.view.beans;

import com.engagepoint.teama.cmismanager.common.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.common.service.ServiceEJBRemote;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@ManagedBean(name = "login")
@SessionScoped
public class LoginBean implements Serializable {

    public static final Logger LOG = Logger.getLogger(LoginBean.class);

    public static final String ERROR_PAGE_REDIRECT = "/error?faces-redirect=true";
    public static final String SESSION_ID = "sessionID";
    public static final String INDEX_PAGE_REDIRECT = "/show/index?faces-redirect=true";
    public static final String LOGIN_PAGE_REDIRECT = "/dif/settings?faces-redirect=true";

    private String username;
    private String password;
    private String sessionID;
    @ManagedProperty("#{error}")
    private ErrorBean errorBean;
    @NotNull(message = "Please enter url")
    private String url;
    private String chosenRepo;
    private String[] availableReposList;
    private boolean logoutVisibility = false;

    public boolean isLogoutVisibility() {
        return logoutVisibility;
    }

    public void setLogoutVisibility(boolean logoutVisibility) {
        this.logoutVisibility = logoutVisibility;
    }

    @EJB(lookup = "java:global/ear-1.0-SNAPSHOT/biz-1.0-SNAPSHOT/ServiceEJB!com.engagepoint.teama.cmismanager.common.service.ServiceEJBRemote")
    private ServiceEJBRemote service;

    public ServiceEJBRemote getService() {
        return service;
    }

    public void setService(ServiceEJBRemote service) {
        this.service = service;
    }

    public void setErrorBean(ErrorBean errorBean) {
        this.errorBean = errorBean;
    }

    public String getChosenRepo() {
        return chosenRepo;
    }

    public void setChosenRepo(String chosenRepo) {
        this.chosenRepo = chosenRepo;
    }

    public String[] getAvailableReposList() {
        return availableReposList;
    }

    public String getSessionID() {
        return sessionID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String doLogin() {
        String page;
        try {

            UUID uuid = UUID.randomUUID();
            sessionID = uuid.toString();
            service.connect(username, password, url, sessionID, chosenRepo);
            page = INDEX_PAGE_REDIRECT;
            HttpSession httpSession = getHttpSessionTrue();
            httpSession.setAttribute(SESSION_ID, sessionID);
            logoutVisibility = true;
        } catch (BaseException e) {
            LOG.error(e.getMessage(), e);
            sessionID = null;
            page = ERROR_PAGE_REDIRECT;
        }
        return page;
    }
    public HttpSession getHttpSessionTrue(){
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        return httpSession;
    }
    public HttpSession getHttpSessionFalse(){
        HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        return httpSession;
    }

    public String getRepoList() {

        try {
            availableReposList = service.getRepoList(username, password, url);
            chosenRepo = availableReposList[0];

        } catch (BaseException e) {
            LOG.error(e.getMessage(), e);
            errorBean.setErrorMessage(e.getMessage());
            sessionID = "error";
            return ERROR_PAGE_REDIRECT;
        }

        return null;
    }

    public String doLogout() {
        HttpSession session = getHttpSessionFalse();
        session.invalidate();

        try {
            service.disconnect(sessionID);
        } catch (ConnectionException e) {
            LOG.error(e.getMessage(), e);
        }

        username = null;
        password = null;
        url = null;
        availableReposList = null;
        chosenRepo = null;
        sessionID = null;
        logoutVisibility = false;

        return LOGIN_PAGE_REDIRECT;
    }

}