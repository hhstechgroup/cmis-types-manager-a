package com.engagepoint.teama.cmismanager.biz.wrappers;

import com.engagepoint.teama.cmismanager.common.model.PropertyRow;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;

import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class TypeDefinitionWrapper implements Serializable, TypeDefinition {

    private String id;
    private String localName;
    private String localNamespace;
    private String displayName;
    private String queryName;
    private String description;

    private boolean creatable;
    private boolean fileable;
    private boolean queryable;
    private boolean includedInSupertypeQuery;
    private boolean fulltextIndexed;
    private boolean controllableAcl;
    private boolean controllablePolicy;

    private String parentTypeId;
    private BaseTypeId baseTypeId;
    private List<CmisExtensionElement> extensions;
    private TypeMutability typeMutability;

    private Map<String, PropertyDefinition<?>> propertyDefinitionMap = new IdentityHashMap<String, PropertyDefinition<?>>();

    public TypeDefinitionWrapper(TypeDTO typeDTO) {
        id = typeDTO.getId();
        localName = typeDTO.getLocalName();
        localNamespace = typeDTO.getLocalNamespace();
        displayName = typeDTO.getDisplayName();
        queryName = typeDTO.getQueryName();
        description = typeDTO.getDescription();
        parentTypeId = typeDTO.getParentTypeId();

        creatable = typeDTO.isCreatable();
        fileable = typeDTO.isFileable();
        queryable = typeDTO.isQueryable();
        includedInSupertypeQuery = typeDTO.isIncludedInSupertypeQuery();
        fulltextIndexed = typeDTO.isFulltextIndexed();
        controllableAcl = typeDTO.isControllableAcl();
        controllablePolicy = typeDTO.isControllablePolicy();
        typeMutability = new TypeMutabilityWrapper(typeDTO);

        baseTypeId = BaseTypeId.fromValue(typeDTO.getBaseTypeId().toString());

        for (PropertyRow row : typeDTO.getPropertyRows()) {
            propertyDefinitionMap.put(row.getId(), new PropertyDefinitionWrapper(row));
        }
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getLocalName() {
        return localName;
    }

    @Override
    public String getLocalNamespace() {
        return localNamespace;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getQueryName() {
        return queryName;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public BaseTypeId getBaseTypeId() {
        return baseTypeId;
    }

    @Override
    public String getParentTypeId() {
        return parentTypeId;
    }

    @Override
    public Boolean isCreatable() {
        return creatable;
    }

    @Override
    public Boolean isFileable() {
        return fileable;
    }

    @Override
    public Boolean isQueryable() {
        return queryable;
    }

    @Override
    public Boolean isFulltextIndexed() {
        return fulltextIndexed;
    }

    @Override
    public Boolean isIncludedInSupertypeQuery() {
        return includedInSupertypeQuery;
    }

    @Override
    public Boolean isControllablePolicy() {
        return controllablePolicy;
    }

    @Override
    public Boolean isControllableAcl() {
        return controllableAcl;
    }

    @Override
    public Map<String, PropertyDefinition<?>> getPropertyDefinitions() {
        return propertyDefinitionMap;
    }

    @Override
    public TypeMutability getTypeMutability() {
        return typeMutability;
    }

    @Override
    public List<CmisExtensionElement> getExtensions() {
        return extensions;
    }

    @Override
    public void setExtensions(List<CmisExtensionElement> cmisExtensionElements) {
        extensions = cmisExtensionElements;
    }

    @Override
    public String toString() {
        return "ID: " + this.getId() + "LocalName: " + this.getLocalName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof TypeDefinitionWrapper)) return false;
        TypeDefinitionWrapper other = (TypeDefinitionWrapper) obj;
        if (other.getId().equals(this.getId()) && other.getQueryName().equals(this.getQueryName())
                && other.getDisplayName().equals(this.getDisplayName()) &&
                other.getLocalName().equals(this.getLocalName())) {
            return true;
        } else
            return false;
    }
}
