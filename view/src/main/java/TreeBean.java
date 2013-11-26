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
        TypeDTO testType = CMISTypeManagerService.getInstance().createType(testMethodCreateType("Test"));
        TreeNode treeNode = new DefaultTreeNode(testType, root);
        //render(list, root);
        render(list, treeNode);

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
            if (data.getChildren() != null) {
                render(data.getChildren(), treeNode);
            } else {
                return;
            }
        }
        return;
    }

    private TypeDTO testMethodCreateType(String name) {

        TypeDTO returnedType = new TypeDTO();

        returnedType.setDisplayName(name);
        returnedType.setId(name + "ID");
        returnedType.setParentTypeId("cmis:item");
        returnedType.setBaseTypeId("cmis:item");
        returnedType.setDescription(name + " Description");
        returnedType.setQueryName(name + "Query");
        returnedType.setLocalName("Local" + name);
        returnedType.setLocalNamespace("namecpace.com");

        returnedType.setMutabilityCanCreate(true);
        returnedType.setMutabilityCanDelete(true);
        returnedType.setMutabilityCanDelete(true);

        returnedType.setQueryable(true);
        returnedType.setFileable(true);
        returnedType.setCreatable(true);

        returnedType.setControllablePolicy(true);
        returnedType.setControllableAcl(true);
        returnedType.setFulltextIndexed(true);
        returnedType.setIncludedInSupertypeQuery(true);

        returnedType.addPropertyRow(toTestMethodCreateType("Property1"));
        returnedType.addPropertyRow(toTestMethodCreateType("Property2"));

        return returnedType;
    }

    private PropertyRow toTestMethodCreateType(String propertyName) {

        PropertyRow returnedRow = new PropertyRow();
        returnedRow.setId(propertyName + "ID");
        returnedRow.setDisplayName(propertyName);
        returnedRow.setDescription(propertyName + "Description");
        returnedRow.setLocalName(propertyName + "Local");
        returnedRow.setQueryName(propertyName + "Query");

        return returnedRow;
    }

}  