package com.engagepoint.teama.cmismanager.view.beans;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@ManagedBean(name = "error")
@SessionScoped
public class ErrorBean implements Serializable {


    public void setErrorVisibility(String errorVisibility) {
        this.errorVisibility = errorVisibility;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void hideInfoBlock() {
        errorVisibility = "false";
        errorMessage = null;
    }

    public String getErrorVisibility() {
        return errorVisibility;
    }

    private String errorMessage;
    private String errorVisibility;

}
