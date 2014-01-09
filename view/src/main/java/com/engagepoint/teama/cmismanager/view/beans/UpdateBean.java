package com.engagepoint.teama.cmismanager.view.beans;

import com.engagepoint.teama.cmismanager.common.model.*;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "updateBean")
@ViewScoped
public class UpdateBean {

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

    ///////////////////

    public String canUpdate = null;

    public String getCanUpdate() {
        return canUpdate;
    }

    public void setCanUpdate(String canUpdate) {
        this.canUpdate = canUpdate;
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

    public void clearNewPropertyRow() {
        newPropertyRow = new PropertyRow();
    }

    /////////////

    private PropertyRow linkToUpdatedPropertyRow;

    public void setNewPropertyRowUpdate(PropertyRow propertyRow) {
        linkToUpdatedPropertyRow = propertyRow;

        newPropertyRow = new PropertyRow();

        newPropertyRow.setDisplayName(new String(linkToUpdatedPropertyRow.getDisplayName()));
        newPropertyRow.setId(new String(linkToUpdatedPropertyRow.getId()));
        newPropertyRow.setDescription(new String(linkToUpdatedPropertyRow.getDescription()));

        newPropertyRow.setLocalNamespace(new String(linkToUpdatedPropertyRow.getLocalNamespace()));
        newPropertyRow.setLocalName(new String(linkToUpdatedPropertyRow.getLocalName()));
        newPropertyRow.setQueryName(new String(linkToUpdatedPropertyRow.getDisplayName()));

        newPropertyRow.setCardinality(
                CardinalityEnum.fromValue(linkToUpdatedPropertyRow.getCardinality().toString()));
        newPropertyRow.setUpdatability(
                UpdatabilityEnum.fromValue(linkToUpdatedPropertyRow.getUpdatability().toString()));
        newPropertyRow.setPropertyType(
                PropertyTypeEnum.fromValue(linkToUpdatedPropertyRow.getPropertyType().toString()));

        newPropertyRow.setQueryable(Boolean.valueOf(linkToUpdatedPropertyRow.isQueryable()));
        newPropertyRow.setOrderable(Boolean.valueOf(linkToUpdatedPropertyRow.isOrderable()));
        newPropertyRow.setRequired(Boolean.valueOf(linkToUpdatedPropertyRow.isRequired()));
        newPropertyRow.setInherited(Boolean.valueOf(linkToUpdatedPropertyRow.isInherited()));
        newPropertyRow.setOpenChoice(Boolean.valueOf(linkToUpdatedPropertyRow.isOpenChoice()));

    }

    public void submitChanges () {

        linkToUpdatedPropertyRow.setDisplayName(newPropertyRow.getDisplayName());
        linkToUpdatedPropertyRow.setId(newPropertyRow.getId());
        linkToUpdatedPropertyRow.setDescription(newPropertyRow.getDescription());
        linkToUpdatedPropertyRow.setLocalNamespace(newPropertyRow.getLocalNamespace());
        linkToUpdatedPropertyRow.setLocalName(newPropertyRow.getLocalName());
        linkToUpdatedPropertyRow.setQueryName(newPropertyRow.getDisplayName());

        linkToUpdatedPropertyRow.setCardinality( newPropertyRow.getCardinality() );
        linkToUpdatedPropertyRow.setUpdatability( newPropertyRow.getUpdatability() );
        linkToUpdatedPropertyRow.setPropertyType( newPropertyRow.getPropertyType() );

        linkToUpdatedPropertyRow.setQueryable(newPropertyRow.isQueryable());
        linkToUpdatedPropertyRow.setOrderable(newPropertyRow.isOrderable());
        linkToUpdatedPropertyRow.setRequired(newPropertyRow.isRequired());
        linkToUpdatedPropertyRow.setInherited(newPropertyRow.isInherited());
        linkToUpdatedPropertyRow.setOpenChoice(newPropertyRow.isOpenChoice());
    }

    public void giveMeSelectedWhenUpdate(NodeSelectEvent event) {
        selected = event.getTreeNode();
        currentDTO = (TypeDTO) selected.getData();

        if (currentDTO.isMutabilityCanUpdate()) {

            newTypeDTO = new TypeDTO();

            newTypeDTO.setId(new String(currentDTO.getId()));
            newTypeDTO.setLocalName(new String(currentDTO.getLocalName()));
            newTypeDTO.setLocalNamespace(new String(currentDTO.getLocalNamespace()));
            newTypeDTO.setDisplayName(new String(currentDTO.getDisplayName()));
            newTypeDTO.setQueryName(new String(currentDTO.getQueryName()));
            newTypeDTO.setDescription(new String(currentDTO.getDescription()));

            newTypeDTO.setParentTypeId(new String(currentDTO.getParentTypeId()));
            newTypeDTO.setBaseTypeId(BaseTypeEnum.fromValue(currentDTO.getBaseTypeId().toString()));

            newTypeDTO.setCreatable(Boolean.valueOf(currentDTO.isCreatable()));
            newTypeDTO.setFileable(Boolean.valueOf(currentDTO.isFileable()));
            newTypeDTO.setQueryable(Boolean.valueOf(currentDTO.isQueryable()));

            newTypeDTO.setIncludedInSupertypeQuery(Boolean.valueOf(currentDTO.isIncludedInSupertypeQuery()));
            newTypeDTO.setControllableAcl(Boolean.valueOf(currentDTO.isControllableAcl()));
            newTypeDTO.setControllablePolicy(Boolean.valueOf(currentDTO.isControllablePolicy()));
            newTypeDTO.setFulltextIndexed(Boolean.valueOf(currentDTO.isFulltextIndexed()));

            newTypeDTO.setMutabilityCanCreate(Boolean.valueOf(currentDTO.isMutabilityCanCreate()));
            newTypeDTO.setMutabilityCanDelete(Boolean.valueOf(currentDTO.isMutabilityCanDelete()));
            newTypeDTO.setMutabilityCanUpdate(Boolean.valueOf(currentDTO.isMutabilityCanUpdate()));
            canUpdate = "true";
        } else {
            canUpdate = null;
        }

    }

    public void addProperty(){
        newTypeDTO.getPropertyRows().add(newPropertyRow);
    }

    public void updateType() {

        currentDTO.setDescription(newTypeDTO.getDescription());
        currentDTO.setDisplayName(newTypeDTO.getDisplayName());
        currentDTO.setDescription(newTypeDTO.getDescription());
        currentDTO.setQueryName(newTypeDTO.getQueryName());
        currentDTO.setLocalName(newTypeDTO.getLocalName());
        currentDTO.setLocalNamespace(newTypeDTO.getLocalNamespace());

        currentDTO.setCreatable(newTypeDTO.isCreatable());
        currentDTO.setFileable(newTypeDTO.isFileable());
        currentDTO.setQueryable(newTypeDTO.isQueryable());

        currentDTO.setIncludedInSupertypeQuery(newTypeDTO.isIncludedInSupertypeQuery());
        currentDTO.setControllableAcl(newTypeDTO.isControllableAcl());
        currentDTO.setControllablePolicy(newTypeDTO.isControllablePolicy());
        currentDTO.setFulltextIndexed(newTypeDTO.isFulltextIndexed());

        currentDTO.setMutabilityCanCreate(newTypeDTO.isMutabilityCanCreate());
        currentDTO.setMutabilityCanDelete(newTypeDTO.isMutabilityCanDelete());
        currentDTO.setMutabilityCanUpdate(newTypeDTO.isMutabilityCanUpdate());

        currentDTO.getPropertyRows().addAll(newTypeDTO.getPropertyRows());

        currentDTO = null;
        selected = null;
        canUpdate = null;
    }
}
