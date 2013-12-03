import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean
@SessionScoped
public class TreeBean implements Serializable {

    private static final long serialVersionUID = 2023524722101427935L;
    private TreeNode root;
    private TreeNode selected = null;
    private TypeDTO  currentDTO = null;
    private TypeDTO newDTO = new TypeDTO();
    ArrayList<PropertyRow> propertyRows = new ArrayList<PropertyRow>();

    public ArrayList<PropertyRow> getPropertyRows() {
        return propertyRows;
    }

    public void setPropertyRows(ArrayList<PropertyRow> propertyRows) {
        this.propertyRows = propertyRows;
    }

    private boolean attributesVisible = false;
    private boolean metadataVisible = false;

    public void setTreeVisible(){
        attributesVisible = false;
        metadataVisible = false;
    }
    public void setAttributesVisibleTrue(){
        attributesVisible = true;
        metadataVisible = false;
    }

    public void setMetadataVisibleTrue(){
        metadataVisible = true;
        attributesVisible = false;
    }

    public boolean isAttributesVisible() {
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