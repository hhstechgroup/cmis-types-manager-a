import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TypeDTO implements Serializable {
    private String displayName;
    private String description;
    private boolean isQueryable;
    private boolean isCreateble;
    private boolean isFileable;
    private boolean isFulltextIndexed;

    private List<TypeDTO> childs= new ArrayList<TypeDTO>();

    public List<TypeDTO> getChilds() {
        return childs;
    }

    public void setChilds(List<TypeDTO> childs) {
        this.childs = childs;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isQueryable() {
        return isQueryable;
    }

    public void setQueryable(boolean queryable) {
        isQueryable = queryable;
    }

    public boolean isCreateble() {
        return isCreateble;
    }

    public void setCreateble(boolean createble) {
        isCreateble = createble;
    }

    public boolean isFileable() {
        return isFileable;
    }

    public void setFileable(boolean fileable) {
        isFileable = fileable;
    }

    public boolean isFulltextIndexed() {
        return isFulltextIndexed;
    }

    public void setFulltextIndexed(boolean fulltextIndexed) {
        isFulltextIndexed = fulltextIndexed;
    }
}