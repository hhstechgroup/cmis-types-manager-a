import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@ViewScoped
public class TreeBean implements Serializable {

    private static final long serialVersionUID = 2023524722101427935L;
    private TreeNode root;
    private TreeNode selected =null;
    private TypeDTO  currentDTO = null;

    private boolean disableBtn = true;

    //    @PostConstruct

    public TypeDTO getCurrentDTO() {
        return currentDTO;
    }

    public void setCurrentDTO(TypeDTO currentDTO) {
        this.currentDTO = currentDTO;
    }


    //    public void init() {
//        List<TypeDTO> list = CMISTypeManagerService.getInstance().getTypes();
//
//        root = new DefaultTreeNode("Root", null);
//        for(TypeDTO i: list) {
//            TreeNode node01 = new DefaultTreeNode(i, root);
//            if(!i.getChilds().isEmpty()){
//                for(TypeDTO j: i.getChilds()) {
//                    TreeNode node01 = new DefaultTreeNode(j, node01);
//                        for(TypeDTO k: j.getChilds()) {
//                            new DefaultTreeNode(j, node01);
//                        }
//                }
//            }
//        }
//    }

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
       //can test functionality with this code
       //if (selected != null && currentDTO.isMutabilityCanUpdate())
       //as all types are can create - true
             if (selected != null && currentDTO.isMutabilityCanCreate())
            disableBtn = false;
        else
            disableBtn = true;
    }

    public String create() {
        //for testing
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