import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class TestTypeMutability implements Serializable, TypeMutability {

    private static final long serialVersionUID = 20235247221121235L;

    private boolean create;
    private boolean update;
    private boolean delete;

    public TestTypeMutability(TypeDTO typeDTO) {
        create = typeDTO.isMutabilityCanCreate();
        update = typeDTO.isMutabilityCanUpdate();
        delete = typeDTO.isMutabilityCanDelete();
    }

    @Override
    public Boolean canCreate() {
        return create;
    }

    @Override
    public Boolean canUpdate() {
        return update;
    }

    @Override
    public Boolean canDelete() {
        return delete;
    }

    @Override
    public List<CmisExtensionElement> getExtensions() {
        return Collections.emptyList();
    }

    @Override
    public void setExtensions(List<CmisExtensionElement> cmisExtensionElements) {

    }
}