package com.engagepoint.teama.cmismanager.biz.wrappers;

import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class TypeMutabilityWrapper implements Serializable, TypeMutability {

    private TypeDTO typeDTO;
    private List<CmisExtensionElement> cmisExtensionElements;

    public TypeMutabilityWrapper(TypeDTO typeDTO) {
        this.typeDTO = typeDTO;
    }

    @Override
    public Boolean canCreate() {
        return typeDTO.isMutabilityCanCreate();
    }

    @Override
    public Boolean canUpdate() {
        return typeDTO.isMutabilityCanUpdate();
    }

    @Override
    public Boolean canDelete() {
        return typeDTO.isMutabilityCanDelete();
    }

    @Override
    public List<CmisExtensionElement> getExtensions() {
        return cmisExtensionElements;
    }

    @Override
    public void setExtensions(List<CmisExtensionElement> cmisExtensionElements) {
        this.cmisExtensionElements = cmisExtensionElements;
    }
}