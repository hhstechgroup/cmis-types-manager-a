package com.engagepoint.teama.cmismanager;

import com.engagepoint.teama.cmismanager.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.exceptions.ModificationException;
import com.engagepoint.teama.cmismanager.model.PropertyRow;
import com.engagepoint.teama.cmismanager.model.TypeDTO;
import org.apache.log4j.Logger;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "treeBean")
@SessionScoped
public class TreeBean implements Serializable {

    public static final Logger LOG = Logger.getLogger(TreeBean.class);
    private static final long serialVersionUID = 2023524722101427935L;
    private  static final String TRUE = "true";
    private  static final String ROOT = "Root";
    public static final String ERROR_PAGE = "/error";

    private TreeNode root;
    private TreeNode selected = null;
    private TypeDTO currentDTO = null;
    private TypeDTO newDTO = new TypeDTO();
    private TreeNode rootUpdate;
    private boolean treeRender = true;
    private List<PropertyRow> propertyRows = new ArrayList<PropertyRow>();
    private UserProperty userProperty;


   @EJB(beanInterface = CMISTypeManagerServiceInterface.class, name="java:global/MultiMVNEAR/biz/com.engagepoint.teama.cmismanager.CMISTypeManagerService")

    private CMISTypeManagerServiceInterface service;
    private String mutability = null;
    private PropertyRow propertyRow1 = new PropertyRow();
    private PropertyRow newProperty = new PropertyRow();
    private String errorMessage;
    private String errorVisible = "false";
    private boolean disableDeleteBtn = true;
    private String errorDialogMsg;
    private String typeCreatable = null;
    @ManagedProperty("#{error}")
    private ErrorBean errorBean;

    public UserProperty getUserProperty() {
        return userProperty;
    }

    public void setUserProperty(UserProperty userProperty) {
        this.userProperty = userProperty;
    }

    private boolean attributesVisible = false;
    private boolean metadataVisible = false;
    private boolean disableBtn = true;


    @PostConstruct
    public void init() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            userProperty = (UserProperty) context.getExternalContext().getSessionMap().get("user");
            List<TypeDTO> list = service.getAllTypes(userProperty);
            root = new DefaultTreeNode(ROOT, null);
            render(list, root);
            rootUpdate = new DefaultTreeNode(ROOT, null);
            render(list, rootUpdate);
        } catch (ModificationException m) {
            LOG.error(m.getMessage(), m);
            errorBean.setErrorMessage(m.getMessage());
            errorBean.setErrorVisibility(TRUE);
        } catch (ConnectionException tp) {
            LOG.error(tp.getMessage(), tp);
            errorBean.setErrorMessage(tp.getMessage());
            errorBean.setErrorVisibility(TRUE);
        } catch (BaseException t) {
            LOG.error(t.getMessage(), t);
            errorBean.setErrorMessage(t.getMessage());
            errorBean.setErrorVisibility(TRUE);
        }
    }



    public CMISTypeManagerServiceInterface getService() {
        return service;
    }

    public void setService(CMISTypeManagerService service) {
        this.service = service;
    }


    public boolean isTreeRender() {
        return treeRender;
    }

    public void setTreeRender(boolean treeRender) {
        this.treeRender = treeRender;
    }

    public void treeEnable() {
        treeRender = !treeRender;
    }

    public String getTypeCreatable() {
        return typeCreatable;
    }

    public void setTypeCreatable(String typeCreatable) {
        this.typeCreatable = typeCreatable;
    }

    public void setErrorBean(ErrorBean errorBean) {
        this.errorBean = errorBean;
    }

    public String getErrorVisible() {
        return String.valueOf(errorVisible);
    }

    public void setErrorVisible(String errorVisible) {
        this.errorVisible = errorVisible;
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
                errorDialogMsg = "Can't delete " + currentDTO.getDisplayName() + " type!!";
                disableDeleteBtn = false;
            }

        } else {
            errorDialogMsg = "You haven't chosen type";
            disableDeleteBtn = false;
        }

    }

    public void deleteType() {
        try {
            service.deleteType(currentDTO, userProperty);
            List<TypeDTO> list = service.getAllTypes(userProperty);
            root = new DefaultTreeNode(ROOT, null);
            render(list, root);
            newDTO = new TypeDTO();
        } catch (BaseException t) {
            LOG.error(t.getMessage(), t);
            errorBean.setErrorMessage(t.getMessage());
            errorBean.setErrorVisibility(TRUE);
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

    public List<PropertyRow> getPropertyRows() {
        return propertyRows;
    }

    public void setPropertyRows(List<PropertyRow> propertyRows) {
        this.propertyRows = propertyRows;
    }

    public boolean isAttributesVisible() {
        attributesVisible = currentDTO != null;
        return attributesVisible;
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

    public TypeDTO getCurrentDTO() {
        return currentDTO;
    }

    public void setCurrentDTO(TypeDTO currentDTO) {
        this.currentDTO = currentDTO;
    }

    public TreeNode getRoot() {
        return root;
    }

    public TreeNode getRootUpdate() {
        return rootUpdate;
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

    public void setDisableBtn(boolean disableBtn) {
        this.disableBtn = disableBtn;
    }

    public void onNodeSelect(NodeSelectEvent event) {
        currentDTO = (TypeDTO) selected.getData();
        disableBtn = !(selected != null && currentDTO.isMutabilityCanCreate());

        newDTO = new TypeDTO();
        newDTO.setParentTypeId(currentDTO.getId());
        newDTO.setBaseTypeId(currentDTO.getBaseTypeId());

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

        if (currentDTO.isMutabilityCanCreate()) {
            typeCreatable = TRUE;
        } else {
            typeCreatable = null;
        }
        treeEnable();
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
    }

    public String addType() {
        try {
            //temporary stub
            newDTO.setPropertyRows(new ArrayList<PropertyRow>());

            service.createType(newDTO, userProperty);
            updateTree();
        } catch (ModificationException m) {
            LOG.error(m.getMessage(), m);
            errorBean.setErrorMessage(m.getMessage());
            errorBean.setErrorVisibility(TRUE);
        } catch (ConnectionException tp) {
            LOG.error(tp.getMessage(), tp);
            errorBean.setErrorMessage(tp.getMessage());
            errorBean.setErrorVisibility(TRUE);
            return ERROR_PAGE;
        } catch (BaseException t) {
            LOG.error(t.getMessage(), t);
            errorBean.setErrorMessage(t.getMessage());
            errorBean.setErrorVisibility(TRUE);
            return ERROR_PAGE;
        }
        return null;
    }

    public void updateTree() {
        List<TypeDTO> list;
        try {
            list = service.getAllTypes(userProperty);
            root = new DefaultTreeNode(ROOT, null);
            render(list, root);
            newDTO = new TypeDTO();
        } catch (BaseException e) {
            LOG.error(e.getMessage(), e);
            errorBean.setErrorMessage(e.getMessage());
            errorBean.setErrorVisibility(TRUE);
        }

    }

    public void passProperty(PropertyRow propertyRow) {
        this.propertyRow1 = propertyRow;
    }

    public void addMetadata() {
        currentDTO.getPropertyRows().add(newProperty);
    }

    public void addMetadataUpdate() {
        newDTO.getPropertyRows().add(newProperty);
    }

    public void onNodeSelectUpdate(NodeSelectEvent event) {
        currentDTO = (TypeDTO) selected.getData();

        if (currentDTO.isMutabilityCanUpdate()) {
            mutability = "allowed";

            newDTO = new TypeDTO();
            newDTO.setParentTypeId(currentDTO.getParentTypeId());
            newDTO.setBaseTypeId(currentDTO.getBaseTypeId());

            newDTO.setId(currentDTO.getId());
            newDTO.setDisplayName(currentDTO.getDisplayName());
            newDTO.setDescription(currentDTO.getDescription());
            newDTO.setQueryName(currentDTO.getQueryName());
            newDTO.setLocalName(currentDTO.getLocalName());
            newDTO.setLocalNamespace(currentDTO.getLocalNamespace());

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

            newDTO.setPropertyRows((ArrayList) currentDTO.getPropertyRows());
        } else {
            mutability = null;
        }
    }

    public void updateType() {

        currentDTO.setDescription(newDTO.getDescription());

        currentDTO.setDisplayName(newDTO.getDisplayName());
        currentDTO.setDescription(newDTO.getDescription());
        currentDTO.setQueryName(newDTO.getQueryName());
        currentDTO.setLocalName(newDTO.getLocalName());
        currentDTO.setLocalNamespace(newDTO.getLocalNamespace());

        currentDTO.setCreatable(newDTO.isCreatable());
        currentDTO.setFileable(newDTO.isFileable());
        currentDTO.setQueryable(newDTO.isQueryable());

        currentDTO.setIncludedInSupertypeQuery(newDTO.isIncludedInSupertypeQuery());
        currentDTO.setControllableAcl(newDTO.isControllableAcl());
        currentDTO.setControllablePolicy(newDTO.isControllablePolicy());
        currentDTO.setFulltextIndexed(newDTO.isFulltextIndexed());

        currentDTO.setMutabilityCanCreate(newDTO.isMutabilityCanCreate());
        currentDTO.setMutabilityCanDelete(newDTO.isMutabilityCanDelete());
        currentDTO.setMutabilityCanUpdate(newDTO.isMutabilityCanUpdate());


        currentDTO = null;
        selected = null;
        mutability = null;
    }

    public String getMutability() {
        return mutability;
    }

    public void setMutability(String mutability) {
        this.mutability = mutability;
    }
}