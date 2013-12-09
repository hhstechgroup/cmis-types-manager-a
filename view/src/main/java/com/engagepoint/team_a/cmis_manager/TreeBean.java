package com.engagepoint.team_a.cmis_manager;

import com.engagepoint.team_a.cmis_manager.exceptions.BaseException;
import com.engagepoint.team_a.cmis_manager.exceptions.ConnectionException;
import com.engagepoint.team_a.cmis_manager.exceptions.ModificationException;
import com.engagepoint.team_a.cmis_manager.model.PropertyRow;
import com.engagepoint.team_a.cmis_manager.model.TypeDTO;
import com.engagepoint.team_a.cmis_manager.wrappers.TypeDefinitionWrapper;
import org.apache.log4j.Logger;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
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
    ArrayList<PropertyRow> propertyRows = new ArrayList<PropertyRow>();
    private PropertyRow propertyRow1;
    private PropertyRow newProperty = new PropertyRow();
    @ManagedProperty("#{error}")
    private ErrorBean errorBean;
    public static final Logger LOG=Logger.getLogger(TreeBean.class);
    private boolean attributesVisible = false;
    private boolean metadataVisible = false;

    public void setErrorBean(ErrorBean errorBean) {
        this.errorBean = errorBean;
    }

    public String getErrorVisible(){
        return String.valueOf(currentDTO==null);
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
        return currentDTO!=null;
    }

    public void setAttributesVisible(boolean attributesVisible) {
        this.attributesVisible = attributesVisible;
    }

    public boolean isMetadataVisible() {
        return metadataVisible;
    }

    public void setMetadataVisible(boolean metadataVisible) {
        this.metadataVisible = metadataVisible;
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

    public TreeBean() {
        try {
            List<TypeDTO> list = CMISTypeManagerService.getInstance().getTypes();
            root = new DefaultTreeNode("Root", null);
            render(list, root);
        } catch (ModificationException m) {
            errorBean.setErrorMessage(m.getMessage());
            errorBean.setErrorVisibility("true");
        } catch (ConnectionException tp) {
            errorBean.setErrorMessage(tp.getMessage());
            errorBean.setErrorVisibility("true");
        } catch (BaseException t) {
            errorBean.setErrorMessage(t.getMessage());
            errorBean.setErrorVisibility("true");
        }
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
        if (selected != null && currentDTO.isMutabilityCanCreate()){
            disableBtn = false;
        }
        else {
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

    public void createType(){
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

    public String addType(){
        try {
            CMISTypeManagerService.getInstance().createType(newDTO);
            List<TypeDTO> list = CMISTypeManagerService.getInstance().getTypes();
            root = new DefaultTreeNode("Root", null);
            render(list, root);
            newDTO = new TypeDTO();
        } catch (ModificationException m) {
            errorBean.setErrorMessage(m.getMessage());
            errorBean.setErrorVisibility("true");
        } catch (ConnectionException tp) {
            errorBean.setErrorMessage(tp.getMessage());
            errorBean.setErrorVisibility("true");
            return "/error";
        } catch (BaseException t) {
            errorBean.setErrorMessage(t.getMessage());
            errorBean.setErrorVisibility("true");
            return "/error";
        }
        return null;
    }

    public void passProperty(PropertyRow propertyRow){
        this.propertyRow1 = propertyRow;
    }

    public void addMetadata(){
        currentDTO.getPropertyRows().add(newProperty);
    }
    public void crea() throws BaseException {
        throw new BaseException("asd");
    }
    public String creat(){
        String page = null;
        try {
            crea();
        } catch (ModificationException me) {
            errorBean.setErrorMessage(me.getMessage());
            errorBean.setErrorVisibility("true");
            page = "/error?faces-redirect=true";
        } catch (BaseException t) {
            LOG.error("BaseException catched");
            LOG.info(errorBean.getErrorMessage());
            errorBean.setErrorMessage(t.getMessage());
            LOG.info(errorBean.getErrorMessage());
            LOG.info(errorBean.getErrorVisibility());
//            errorBean.setErrorVisibility("true");
            LOG.info(errorBean.getErrorVisibility());
            page = "/error?faces-redirect=true";
        }
        return page;
    }
}