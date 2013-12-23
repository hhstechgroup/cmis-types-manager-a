package com.engagepoint.teama.cmismanager.view.beans;

import javax.faces.bean.*;
import java.io.Serializable;

@ManagedBean(name = "error")
@SessionScoped
public class ErrorBean implements Serializable {

    private String errorMessage;
    private String errorVisibility;

    public String getErrorVisibility() {
        return errorVisibility;
    }

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
}