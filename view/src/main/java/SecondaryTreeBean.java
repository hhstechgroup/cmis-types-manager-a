

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="secType")
@ViewScoped
public class SecondaryTreeBean implements Serializable{
    private static final long serialVersionUID = 2023524722101427935L;
    private TreeNode root;
    private TreeNode selected = null;
    private TypeDTO typeDTO;
    private TreeNode[] selectedNodes;
    private String selectedNodesStr;
    private List<TypeDTO> secondaryTypes;

    public SecondaryTreeBean() {
        typeDTO = CMISTypeManagerService.getInstance().getSecondaryTypes();
        root = new DefaultTreeNode("Secondary Types Root", null);
        render(typeDTO, root);
    }

    public TreeNode[] getSelectedNodes() {
        return selectedNodes;
    }

    public void setSelectedNodes(TreeNode[] selectedNodes) {
        this.selectedNodes = selectedNodes;
    }

    public List<TypeDTO> getSecondaryTypes() {
        secondaryTypes = new ArrayList<TypeDTO>();
        if (selectedNodes!=null){
            for(TreeNode node:selectedNodes){
                TypeDTO data = (TypeDTO)node.getData();
                secondaryTypes.add(data);
            }
        }
        return secondaryTypes;
    }

    public String deleteSelectedSecondaryType(TypeDTO typeDTO) {
        if (secondaryTypes !=null) {
            secondaryTypes.remove(typeDTO);
        }
        return null;
    }

    public TreeNode getRoot() {
        return root;
    }
    private void render(TypeDTO tree, TreeNode parent) {
        TreeNode treeNode = new DefaultTreeNode(tree, parent);

        if (tree.getChildren() != null) {
            for (TypeDTO temp : tree.getChildren() ) {
                render(temp, treeNode);
            }
        }
    }
}