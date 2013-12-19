package com.engagepoint.team_a.cmis_manager;

import com.engagepoint.team_a.cmis_manager.exceptions.BaseException;
import com.engagepoint.team_a.cmis_manager.model.TypeDTO;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "secType")
@ViewScoped
public class SecondaryTreeBean implements Serializable {
    public static final Logger LOG = Logger.getLogger(SecondaryTreeBean.class);
    private static final long serialVersionUID = 2023524722101427935L;
    private String errorMessage;
    private String errorVisibility;
    private TreeNode root;
    private TreeNode[] selectedNodes;

    public SecondaryTreeBean() {
        try {
            TypeDTO typeDTO = CMISTypeManagerService.getInstance().getSecondaryTypes();
            root = new DefaultTreeNode("Secondary Types Root", null);
            render(typeDTO, root);
        } catch (BaseException t) {
            LOG.error(t.getMessage(), t);
            errorMessage = t.getMessage();
            errorVisibility = "true";
        }
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

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes.clone();
    }

    public List<TypeDTO> getSecondaryTypes() {
        List<TypeDTO> secondaryTypes = new ArrayList<TypeDTO>();
        if (selectedNodes != null) {
            for (TreeNode node : selectedNodes) {
                TypeDTO data = (TypeDTO) node.getData();
                secondaryTypes.add(data);
            }
        }
        return secondaryTypes;
    }

    public TreeNode getRoot() {
        return root;
    }

    private void render(TypeDTO tree, TreeNode parent) {
        TreeNode treeNode = new DefaultTreeNode(tree, parent);

        if (tree.getChildren() != null) {
            for (TypeDTO temp : tree.getChildren()) {
                render(temp, treeNode);
            }
        }
    }
}