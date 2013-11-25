import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@SessionScoped
public class TreeBean implements Serializable {

    private static final long serialVersionUID = 2023524722101427935L;
    private TreeNode root;
    private TreeNode selected = null;
    private TypeDTO  currentDTO = null;

    private boolean disableBtn = true;

    public TypeDTO getCurrentDTO() {
        return currentDTO;
    }

    public void setCurrentDTO(TypeDTO currentDTO) {
        this.currentDTO = currentDTO;
    }

    public TreeBean() {
        List<TypeDTO> list = CMISTypeManagerService.getInstance().getTypes();
        root = new DefaultTreeNode("Root", null);
        render(list, root);
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
    }

    public String create() {
        return "error";
    }

    private void render(List<TypeDTO> tree, TreeNode parent) {
        for (TypeDTO data : tree) {
            TreeNode treeNode = new DefaultTreeNode(data, parent);
            if (data.getChilds() != null) {
                render(data.getChilds(), treeNode);
            } else {
                return;
            }
        }
        return;
    }

    
}  