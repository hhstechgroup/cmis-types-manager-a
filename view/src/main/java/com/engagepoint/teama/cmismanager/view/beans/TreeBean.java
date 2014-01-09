package com.engagepoint.teama.cmismanager.view.beans;

import com.engagepoint.teama.cmismanager.common.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.common.exceptions.ModificationException;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import com.engagepoint.teama.cmismanager.common.service.ServiceEJBRemote;
import org.apache.log4j.Logger;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean(name = "treeBean")
@SessionScoped
public class TreeBean implements Serializable {

    public static final Logger LOG = Logger.getLogger(TreeBean.class);
    private static final long serialVersionUID = 2023524722101427935L;
    private static final String TRUE = "true";
    private static final String ROOT = "Root";
    public static final String ERROR_PAGE = "/error";

    //move to import/export bean
    private TypeDTO currentDTO;

    private TreeNode root;
    private TreeNode selected;

    private TreeNode rootUpdate;

    @EJB(lookup = "java:global/ear-1.0-SNAPSHOT/biz-1.0-SNAPSHOT/ServiceEJB!com.engagepoint.teama.cmismanager.common.service.ServiceEJBRemote")
    private ServiceEJBRemote service;

    @ManagedProperty("#{error}")
    private ErrorBean errorBean;

    @ManagedProperty("#{login}")
    private LoginBean loginBean;

    private String sessionID;

    public String getSessionID() {
        return sessionID;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public ServiceEJBRemote getService() {
        return service;
    }

    public void setService(ServiceEJBRemote service) {
        this.service = service;
    }


    public void setErrorBean(ErrorBean errorBean) {
        this.errorBean = errorBean;
    }

    //move to import/export bean
    public TypeDTO getCurrentDTO() {
        return currentDTO;
    }

    //move to import/export bean
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

    @PostConstruct
    public void init() {
        try {
            sessionID = loginBean.getSessionID();
            List<TypeDTO> list = service.getAllTypes(sessionID);
            root = new DefaultTreeNode(ROOT, null);
            render(list, root);

            list = service.getAllTypes(sessionID);
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

    //move to import/export bean
    public void onNodeSelect(NodeSelectEvent event) {
        currentDTO = (TypeDTO) selected.getData();
    }

    public void updateTree() {
        List<TypeDTO> list;
        try {
            list = service.getAllTypes(sessionID);
            root = new DefaultTreeNode(ROOT, null);
            render(list, root);
        } catch (BaseException e) {
            LOG.error(e.getMessage(), e);
            errorBean.setErrorMessage(e.getMessage());
            errorBean.setErrorVisibility(TRUE);
        }

    }

    public String createType(TypeDTO newType) {
        try {
            service.createType(newType, sessionID);
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

    public void deleteType(TypeDTO typeDTO) {
        try {
            service.deleteType(typeDTO, sessionID);
            updateTree();
        } catch (BaseException t) {
            LOG.error(t.getMessage(), t);
            errorBean.setErrorMessage(t.getMessage());
            errorBean.setErrorVisibility(TRUE);
        }
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
}