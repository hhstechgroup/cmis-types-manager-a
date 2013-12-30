package com.engagepoint.teama.cmismanager.view.beans;

import com.engagepoint.teama.cmismanager.common.model.*;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "createBean")
@ViewScoped
public class CreateBean {

    private PropertyTypeEnum[] propertyTypeEnums = PropertyTypeEnum.values();
    private UpdatabilityEnum[] updatabilityEnums = UpdatabilityEnum.values();
    private CardinalityEnum[] cardinalityEnums = CardinalityEnum.values();

    public PropertyTypeEnum[] getPropertyTypeEnums() {
        return propertyTypeEnums;
    }

    public UpdatabilityEnum[] getUpdatabilityEnums() {
        return updatabilityEnums;
    }

    public CardinalityEnum[] getCardinalityEnums() {
        return cardinalityEnums;
    }

    private TypeDTO newTypeDTO = new TypeDTO();

    public TypeDTO getNewTypeDTO() {
        return newTypeDTO;
    }

    public void setNewTypeDTO(TypeDTO newTypeDTO) {
        this.newTypeDTO = newTypeDTO;
    }

    private TypeDTO currentDTO;

    public TypeDTO getCurrentDTO() {
        return currentDTO;
    }

    public void setCurrentDTO(TypeDTO currentDTO) {
        this.currentDTO = currentDTO;
    }

    /////////////////////////////

    private String canCreate = null;

    public String getCanCreate() {
        return canCreate;
    }

    public void setCanCreate(String canCreate) {
        this.canCreate = canCreate;
    }

    ////////////////////////

    private TreeNode selected = null;

    public TreeNode getSelected() {
        return selected;
    }

    ////////////////

    private List<PropertyRow> propertyRowList = new ArrayList<PropertyRow>();

    public List<PropertyRow> getPropertyRowList() {
        return propertyRowList;
    }

    public void setPropertyRowList(List<PropertyRow> propertyRowList) {
        this.propertyRowList = propertyRowList;
    }

    ///////////

    private PropertyRow newPropertyRow = new PropertyRow();

    public PropertyRow getNewPropertyRow() {
        return newPropertyRow;
    }

    public void setNewPropertyRowCreate(PropertyRow propertyRow) {
        this.newPropertyRow = propertyRow;
    }

    public void clearNewPropertyRow() {
        newPropertyRow = new PropertyRow();
    }

    public void giveMeSelectedWhenCreate(NodeSelectEvent event) {
        selected = event.getTreeNode();
        currentDTO = (TypeDTO) selected.getData();

        newTypeDTO.setParentTypeId(currentDTO.getId());
        newTypeDTO.setBaseTypeId(currentDTO.getBaseTypeId());

        newTypeDTO.setCreatable(currentDTO.isCreatable());
        newTypeDTO.setFileable(currentDTO.isFileable());
        newTypeDTO.setQueryable(currentDTO.isQueryable());

        newTypeDTO.setIncludedInSupertypeQuery(currentDTO.isIncludedInSupertypeQuery());
        newTypeDTO.setControllableAcl(currentDTO.isControllableAcl());
        newTypeDTO.setControllablePolicy(currentDTO.isControllablePolicy());
        newTypeDTO.setFulltextIndexed(currentDTO.isFulltextIndexed());

        newTypeDTO.setMutabilityCanCreate(currentDTO.isMutabilityCanCreate());
        newTypeDTO.setMutabilityCanDelete(currentDTO.isMutabilityCanDelete());
        newTypeDTO.setMutabilityCanUpdate(currentDTO.isMutabilityCanUpdate());

        if (currentDTO.isMutabilityCanCreate()) {
            canCreate = "true";
        } else {
            canCreate = null;
        }
    }

    public void addProperty(){
        newTypeDTO.getPropertyRows().add(newPropertyRow);
    }
}
