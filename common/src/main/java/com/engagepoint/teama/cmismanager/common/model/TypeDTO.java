package com.engagepoint.teama.cmismanager.common.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class TypeDTO implements Serializable, Cloneable {


    private BaseTypeEnum baseTypeId;
    //private List<CmisExtensionElement> extensions; // wtf?

    private String parentTypeId;
    /**
     * This 3 fields contains TypeMutability interface from CMIS API
     */
    private boolean mutabilityCanCreate;
    private boolean mutabilityCanUpdate;
    private boolean mutabilityCanDelete;

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

    ////////////
    private List<PropertyRow> propertyRows = new ArrayList<PropertyRow>();

    public List<PropertyRow> getPropertyRows() {
        return propertyRows;
    }

    public void setPropertyRows(List<PropertyRow> propertyRows) {
        this.propertyRows = propertyRows;
    }

    public void addPropertyRow(PropertyRow propertyRow) {
        propertyRows.add(propertyRow);
    }

    public Collection<PropertyRow> getPropertyList() {
        return propertyRows;
    }
    ////////////////

    public TypeDTO() {
    }

    private List<TypeDTO> children = new ArrayList<TypeDTO>();

    public List<TypeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<TypeDTO> children) {
        this.children = children;
    }

    public BaseTypeEnum getBaseTypeId() {
        return baseTypeId;
    }

    public void setBaseTypeId(BaseTypeEnum baseTypeId) {
        this.baseTypeId = baseTypeId;
    }

    public String getParentTypeId() {
        return parentTypeId;
    }

    public void setParentTypeId(String parentTypeId) {
        this.parentTypeId = parentTypeId;
    }

    public boolean isMutabilityCanCreate() {
        return mutabilityCanCreate;
    }

    public void setMutabilityCanCreate(boolean mutabilityCanCreate) {
        this.mutabilityCanCreate = mutabilityCanCreate;
    }

    public boolean isMutabilityCanUpdate() {
        return mutabilityCanUpdate;
    }

    public void setMutabilityCanUpdate(boolean mutabilityCanUpdate) {
        this.mutabilityCanUpdate = mutabilityCanUpdate;
    }

    public boolean isMutabilityCanDelete() {
        return mutabilityCanDelete;
    }

    public void setMutabilityCanDelete(boolean mutabilityCanDelete) {
        this.mutabilityCanDelete = mutabilityCanDelete;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getLocalNamespace() {
        return localNamespace;
    }

    public void setLocalNamespace(String localNamespace) {
        this.localNamespace = localNamespace;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
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

    public boolean isCreatable() {
        return creatable;
    }

    public void setCreatable(boolean creatable) {
        this.creatable = creatable;
    }

    public boolean isFileable() {
        return fileable;
    }

    public void setFileable(boolean fileable) {
        this.fileable = fileable;
    }

    public boolean isQueryable() {
        return queryable;
    }

    public void setQueryable(boolean queryable) {
        this.queryable = queryable;
    }

    public boolean isIncludedInSupertypeQuery() {
        return includedInSupertypeQuery;
    }

    public void setIncludedInSupertypeQuery(boolean includedInSupertypeQuery) {
        this.includedInSupertypeQuery = includedInSupertypeQuery;
    }

    public boolean isFulltextIndexed() {
        return fulltextIndexed;
    }

    public void setFulltextIndexed(boolean fulltextIndexed) {
        this.fulltextIndexed = fulltextIndexed;
    }

    public boolean isControllableAcl() {
        return controllableAcl;
    }

    public void setControllableAcl(boolean controllableAcl) {
        this.controllableAcl = controllableAcl;
    }

    public boolean isControllablePolicy() {
        return controllablePolicy;
    }

    public void setControllablePolicy(boolean controllablePolicy) {
        this.controllablePolicy = controllablePolicy;
    }

    @Override
    public String toString() {
        return displayName;
    }

}