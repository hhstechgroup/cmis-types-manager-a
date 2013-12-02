import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.List;

@ManagedBean
@SessionScoped
public class TreeBean implements Serializable {

    private static final long serialVersionUID = 2023524722101427935L;
    private TreeNode root;
    private TreeNode selected = null;
    private TypeDTO  currentDTO = null;
    private TypeDTO newDTO = new TypeDTO();

    private boolean attributesVisible = false;
    private boolean metadataVisible = false;

    private boolean creatable = false;
    private boolean updateable = false;

    public boolean isUpdateable() {
        return updateable;
    }

    public String getErrorVisible(){
        return String.valueOf(currentDTO==null);
    }

    public void setUpdateable(boolean updateable) {
        this.updateable = updateable;
    }

    public boolean isCreatable() {
        return creatable;
    }

    public void setCreatable(boolean creatable) {
        this.creatable = creatable;
    }

    public boolean isAttributesVisible() {
        return currentDTO!=null;
    }

    public void setAttributesVisible(boolean attributesVisible) {
        this.attributesVisible = attributesVisible;
    }

    public boolean isMetadataVisible() {
        return currentDTO!=null;
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

    public String create() {
        return "/error";
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


    public void addType(){
        CMISTypeManagerService.getInstance().createType(newDTO);
        List<TypeDTO> list = CMISTypeManagerService.getInstance().getTypes();
        root = new DefaultTreeNode("Root", null);
        render(list, root);
        newDTO = new TypeDTO();
    }

}  