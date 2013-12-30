package com.engagepoint.teama.cmismanager.view.beans;

import com.engagepoint.teama.cmismanager.common.model.PropertyRow;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import org.primefaces.event.NodeSelectEvent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "showBean")
@ViewScoped
public class ShowBean {

    private PropertyRow newPropertyRow = new PropertyRow();

    public PropertyRow getNewPropertyRow() {
        return newPropertyRow;
    }

    public void setNewPropertyRowShow(PropertyRow propertyRow) {
        this.newPropertyRow = propertyRow;
    }

    ///////////////////

    private boolean treeRender = true;

    public boolean isTreeRender() {
        return treeRender;
    }

    public void setTreeRender(boolean treeRender) {
        this.treeRender = treeRender;
    }

    public void treeEnable() {
        treeRender = !treeRender;
    }

    /////////////

    private boolean attributesVisible = false;

    public boolean isAttributesVisible() {
        attributesVisible = currentDTO != null;
        return attributesVisible;
    }

    public void setAttributesVisible(boolean attributesVisible) {
        this.attributesVisible = attributesVisible;
    }

    ////////////////

    private List<PropertyRow> propertyRowList = new ArrayList<PropertyRow>();

    public List<PropertyRow> getPropertyRowList() {
        return propertyRowList;
    }

    public void setPropertyRowList(List<PropertyRow> propertyRowList) {
        this.propertyRowList = propertyRowList;
    }

    /////////////

    private TypeDTO currentDTO;

    public TypeDTO getCurrentDTO() {
        return currentDTO;
    }

    public void setCurrentDTO(TypeDTO currentDTO) {
        this.currentDTO = currentDTO;
    }

    public void giveMeSelectedWhenShow(NodeSelectEvent event) {
        currentDTO = (TypeDTO) event.getTreeNode().getData();

        treeEnable();
    }
}
