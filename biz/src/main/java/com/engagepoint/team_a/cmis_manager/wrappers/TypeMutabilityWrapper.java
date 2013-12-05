package com.engagepoint.team_a.cmis_manager.wrappers;

import com.engagepoint.team_a.cmis_manager.model.TypeDTO;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;

import java.io.Serializable;
import java.util.List;

public class TypeMutabilityWrapper implements Serializable, TypeMutability {

    private static final long serialVersionUID = 20235247221121235L;

    private boolean create;
    private boolean update;
    private boolean delete;

    public TypeMutabilityWrapper(TypeDTO typeDTO) {
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
        return null;
    }

    @Override
    public void setExtensions(List<CmisExtensionElement> cmisExtensionElements) {

    }
}