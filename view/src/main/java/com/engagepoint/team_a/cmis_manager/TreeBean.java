package com.engagepoint.team_a.cmis_manager;

import com.engagepoint.team_a.cmis_manager.exceptions.TMBaseException;
import com.engagepoint.team_a.cmis_manager.model.PropertyRow;
import com.engagepoint.team_a.cmis_manager.model.TypeDTO;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@SessionScoped
public class TreeBean implements Serializable {

    private static final long serialVersionUID = 2023524722101427935L;
    private TreeNode root;
    private TreeNode selected = null;
    private TypeDTO currentDTO = null;
    private TypeDTO newDTO = new TypeDTO();
    private ArrayList<PropertyRow> propertyRows = new ArrayList<PropertyRow>();
    private PropertyRow propertyRow1;
    private PropertyRow newProperty = new PropertyRow();
    private String errorMessage;
    private String errorVisibility;
    private boolean disableDeleteBtn = true;
    private String errorDialogMsg;
    private boolean attributesVisible;
    private boolean metadataVisible = false;

    public TreeBean() {
        try {
            List<TypeDTO> list = CMISTypeManagerService.getInstance().getTypes();
            root = new DefaultTreeNode("Root", null);
            render(list, root);
        } catch (TMBaseException t) {
            errorMessage = t.getMessage();
            errorVisibility = "true";
        }
    }

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
                errorDialogMsg = "Can't delete "+currentDTO.getDisplayName()+" type!!";
                disableDeleteBtn = false;
            }

        } else {
            errorDialogMsg = "You haven't chosen type";
            disableDeleteBtn = false;
        }

    }

    public void deleteType(){
        try {
            CMISTypeManagerService.getInstance().deleteType(currentDTO);
            List<TypeDTO> list = CMISTypeManagerService.getInstance().getTypes();
            root = new DefaultTreeNode("Root", null);
            render(list, root);
            newDTO = new TypeDTO();
        } catch (TMBaseException t) {
            errorMessage = t.getMessage();
            errorVisibility = "true";
        }

    }

    public boolean isDisableDeleteBtn() {
        return disableDeleteBtn;
    }

    public void setDisableDeleteBtn(boolean disableDeleteBtn) {
        this.disableDeleteBtn = disableDeleteBtn;
    }

    public boolean isDisableBtn() {
        return disableBtn;
    }

    public void setDisableBtn(boolean disableBtn) {
        this.disableBtn = disableBtn;
    }

    public String getErrorDialogMsg() {
        return errorDialogMsg;
    }

    public void setErrorDialogMsg(String errorDialogMsg) {
        this.errorDialogMsg = errorDialogMsg;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorVisibility() {
        return errorVisibility;
    }

    public void setErrorVisibility(String errorVisibility) {
        this.errorVisibility = errorVisibility;
    }

    public String getErrorVisible() {
        return String.valueOf(currentDTO == null);
    }

    public PropertyRow getNewProperty() {
        return newProperty;
    }

    public void setNewProperty(PropertyRow newProperty) {
        this.newProperty = newProperty;
    }

    public PropertyRow getPropertyRow1() {
        return propertyRow1;
    }

    public void setPropertyRow1(PropertyRow propertyRow1) {
        this.propertyRow1 = propertyRow1;
    }

    public ArrayList<PropertyRow> getPropertyRows() {
        return propertyRows;
    }

    public void setPropertyRows(ArrayList<PropertyRow> propertyRows) {
        this.propertyRows = propertyRows;
    }


    public boolean isAttributesVisible() {
        return attributesVisible;
    }

    public void AttributesVisibleTrue() {
        attributesVisible = true;
    }

    public void setAttributesVisible(boolean attributesVisible) {
        this.attributesVisible = attributesVisible;
    }

    public void setAttributesVisibleTest() {
        attributesVisible = (currentDTO != null);
    }

    public TypeDTO getNewDTO() {
        return newDTO;
    }

    public void setNewDTO(TypeDTO newDTO) {
        this.newDTO = newDTO;
    }

    private boolean disableBtn = true;

    public TypeDTO getCurrentDTO() {
        return currentDTO;
    }

    public void setCurrentDTO(TypeDTO currentDTO) {
        this.currentDTO = currentDTO;
    }


    public TreeNode getRoot() {
        return root;
    }

    public TreeNode getSelected() {
        return selected;
    }

    public void setSelected(TreeNode selectedNode) {
        this.selected = selectedNode;
    }

    public Boolean getDisableBtn() {
        return disableBtn;
    }

    public void onNodeSelect(NodeSelectEvent event) {
        currentDTO = (TypeDTO) selected.getData();
        if (selected != null && currentDTO.isMutabilityCanCreate()) {
            disableBtn = false;
        } else {
            disableBtn = true;
        }

        newDTO = new TypeDTO();
        newDTO.setParentTypeId(currentDTO.getId());
        newDTO.setBaseTypeId(currentDTO.getBaseTypeId());
        //newDTO.setChildren(null);

        newDTO.setCreatable(currentDTO.isCreatable());
        newDTO.setFileable(currentDTO.isFileable());
        newDTO.setQueryable(currentDTO.isQueryable());

        newDTO.setIncludedInSupertypeQuery(currentDTO.isIncludedInSupertypeQuery());
        newDTO.setControllableAcl(currentDTO.isControllableAcl());
        newDTO.setControllablePolicy(currentDTO.isControllablePolicy());
        newDTO.setFulltextIndexed(currentDTO.isFulltextIndexed());

        newDTO.setMutabilityCanCreate(currentDTO.isMutabilityCanCreate());
        newDTO.setMutabilityCanDelete(currentDTO.isMutabilityCanDelete());
        newDTO.setMutabilityCanUpdate(currentDTO.isMutabilityCanUpdate());
    }

    private void render(List<TypeDTO> tree, TreeNode parent) {
        for (TypeDTO data : tree) {
            TreeNode treeNode = new DefaultTreeNode(data, parent);
            if (data.getChildren() != null) {
                render(data.getChildren(), treeNode);
            } else {
                return;
            }
        }
        return;
    }

    public void createType() {
        newDTO = new TypeDTO();
        newDTO.setParentTypeId(currentDTO.getId());
        newDTO.setBaseTypeId(currentDTO.getBaseTypeId());
        //newDTO.setChildren(null);
        newDTO.setId(currentDTO.getParentTypeId());

        //newDTO.setDisplayName();
        //newDTO.setDescription();
        //newDTO.setQueryName();
        //newDTO.getLocalName();
        //newDTO.setLocalNamespace(currentDTO.getLocalNamespace());

        newDTO.setCreatable(currentDTO.isCreatable());
        newDTO.setFileable(currentDTO.isFileable());
        newDTO.setQueryable(currentDTO.isQueryable());

        newDTO.setIncludedInSupertypeQuery(currentDTO.isIncludedInSupertypeQuery());
        newDTO.setControllableAcl(currentDTO.isControllableAcl());
        newDTO.setControllablePolicy(currentDTO.isControllablePolicy());
        newDTO.setFulltextIndexed(currentDTO.isFulltextIndexed());

        newDTO.setMutabilityCanCreate(currentDTO.isMutabilityCanCreate());
        newDTO.setMutabilityCanDelete(currentDTO.isMutabilityCanDelete());
        newDTO.setMutabilityCanUpdate(currentDTO.isMutabilityCanUpdate());
    }

    public void addType() {
        try {
            CMISTypeManagerService.getInstance().createType(newDTO);
            List<TypeDTO> list = CMISTypeManagerService.getInstance().getTypes();
            root = new DefaultTreeNode("Root", null);
            render(list, root);
            newDTO = new TypeDTO();
        } catch (TMBaseException t) {
            errorMessage = t.getMessage();
            errorVisibility = "true";
        }

    }

    public void passProperty(PropertyRow propertyRow) {
        this.propertyRow1 = propertyRow;
    }

    public void addMetadata() {
        currentDTO.getPropertyRows().add(newProperty);
    }

    public void crea() {
        throw new TMBaseException("asd");
    }

    public void creat() {
        try {
            crea();
        } catch (TMBaseException t) {
            errorMessage = t.getMessage();
            errorVisibility = "true";
        }
    }
}