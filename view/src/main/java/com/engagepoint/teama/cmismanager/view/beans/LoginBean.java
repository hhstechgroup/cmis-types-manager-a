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
    public static final String LOGIN_PAGE_REDIRECT = "/login?faces-redirect=true";

    private String username;
    private String password;
    private String sessionID;
    @ManagedProperty("#{error}")
    private ErrorBean errorBean;
    @NotNull(message = "Please enter url")
    private String url;
    private String chosenRepo;
    private String[] availableReposList;

    @EJB(beanInterface = ServiceEJBRemote.class, name = "java:global/MultiMVNEAR/biz/com.engagepoint.teama.cmismanager.biz.ejb.ServiceEJB")
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
            service.connect(username, password, url, sessionID, chosenRepo);
            page = INDEX_PAGE_REDIRECT;
        } catch (BaseException e) {
            LOG.error(e.getMessage(), e);
            sessionID = null;
            page = ERROR_PAGE_REDIRECT;
        }
        return page;
    }

    public String getRepoList() {

        try {
            UUID uuid = UUID.randomUUID();
            sessionID = uuid.toString();

            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            httpSession.setAttribute(SESSION_ID, sessionID);

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
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
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

        return LOGIN_PAGE_REDIRECT;
    }
}