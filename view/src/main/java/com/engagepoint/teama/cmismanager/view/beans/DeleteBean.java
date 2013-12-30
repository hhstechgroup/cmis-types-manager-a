package com.engagepoint.teama.cmismanager.view.beans;

import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import org.primefaces.event.NodeSelectEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "deleteBean")
@SessionScoped
public class DeleteBean {

    private String errorDialogMsg;

    public String getErrorDialogMsg() {
        return errorDialogMsg;
    }

    public void setErrorDialogMsg(String errorDialogMsg) {
        this.errorDialogMsg = errorDialogMsg;
    }

    ///////////////////////////

    private boolean disableDeleteBtn = true;

    public boolean isDisableDeleteBtn() {
        return disableDeleteBtn;
    }

    public void setDisableDeleteBtn(boolean disableDeleteBtn) {
        this.disableDeleteBtn = disableDeleteBtn;
    }

    ///////////////////////////

    private TypeDTO currentDTO;

    public TypeDTO getCurrentDTO() {
        return currentDTO;
    }

    public void setCurrentDTO(TypeDTO currentDTO) {
        this.currentDTO = currentDTO;
    }

    ////////////////////

    public void deleteConditions() {
        if (currentDTO != null) {
            if (currentDTO.isMutabilityCanDelete()) {
                if (!currentDTO.getChildren().isEmpty()) {
                    errorDialogMsg = "Type has children, are you sure ?";
                    disableDeleteBtn = true;
                } else {
                    errorDialogMsg = "Are you sure?";
                    disableDeleteBtn = true;
                }
            } else {
                errorDialogMsg = "Can't delete " + currentDTO.getDisplayName() + " type!!";
                disableDeleteBtn = false;
            }

        } else {
            errorDialogMsg = "You haven't chosen type";
            disableDeleteBtn = false;
        }

    }

    //////////////////////

    public void giveMeSelectedWhenDelete(NodeSelectEvent event) {
        currentDTO = (TypeDTO) event.getTreeNode().getData();
    }

}
