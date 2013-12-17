package com.engagepoint.team_a.cmis_manager;

import com.engagepoint.team_a.cmis_manager.exceptions.BaseException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.io.Serializable;

@ManagedBean(name = "login")
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;
    private String sessionID;
    @ManagedProperty("#{error}")
    private ErrorBean errorBean;
    @NotNull(message = "Please enter url")
    private String url;

    private String chosenRepo;

    private String[] availabeReposList;

    public void setErrorBean(ErrorBean errorBean) {
        this.errorBean = errorBean;
    }
    public String getChosenRepo() {
        return chosenRepo;
    }

    public void setChosenRepo(String chosenRepo) {
        this.chosenRepo = chosenRepo;
    }

    public String[] getAvailabeReposList() {
        return availabeReposList;
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
        CMISTypeManagerService service = CMISTypeManagerService.getInstance();
        try{

            service.connect(chosenRepo);
            sessionID = service.getSessionID();
            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            httpSession.setAttribute("sessionID",sessionID);
            page = "/show/index?faces-redirect=true";
        }catch (BaseException e){
            sessionID=null;
            page = "/error?faces-redirect=true";
        }
        return page;
    }

    public String getRepoList() {
        CMISTypeManagerService service = CMISTypeManagerService.getInstance();

        try{
            availabeReposList = service.getRepoList(username, password, url);
            chosenRepo = availabeReposList[0];

        }catch (BaseException e){
            errorBean.setErrorMessage(e.getMessage());
            sessionID = "error";
            HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
            httpSession.setAttribute("sessionID",sessionID);
            return "/error?faces-redirect=true";
        }
        return null;
    }

    public String doLogout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();

        username = null;
        password = null;
        url = null;
        sessionID=null;

        availabeReposList = null;
        chosenRepo = null;

        CMISTypeManagerService.getInstance().disconnect();
        return "/login?faces-redirect=true";
    }
}