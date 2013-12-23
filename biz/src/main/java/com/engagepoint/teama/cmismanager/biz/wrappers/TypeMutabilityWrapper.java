package com.engagepoint.teama.cmismanager.biz.wrappers;

import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class TypeMutabilityWrapper implements Serializable, TypeMutability {

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
        return Collections.emptyList();
    }

    @Override
    public void setExtensions(List<CmisExtensionElement> cmisExtensionElements) {
        //todo complete method, todo this code is just for sonar
        return;
    }
}