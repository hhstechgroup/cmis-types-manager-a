package com.engagepoint.teama.cmismanager.common.model;

import java.io.Serializable;
import java.util.List;

public class PropertyRow implements Serializable, Cloneable {

    private String id;
    private String localName;
    private String localNamespace;
    private String displayName;
    private String queryName;
    private String description;

    private boolean inherited;
    private boolean required;
    private boolean queryable;
    private boolean orderable;
    private boolean openChoice;

    //enum
    private PropertyTypeEnum propertyType = PropertyTypeEnum.STRING;
    private CardinalityEnum cardinality = CardinalityEnum.SINGLE;
    private UpdatabilityEnum updatability = UpdatabilityEnum.READWRITE;

    //not use
    private List choiseList;
    private List defaultValue;
    private List extensions;

    public List getChoiseList() {
        return choiseList;
    }

    public void setChoiseList(List choiseList) {
        this.choiseList = choiseList;
    }

    public List getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(List defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List getExtensions() {
        return extensions;
    }

    public void setExtensions(List extensions) {
        this.extensions = extensions;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getQueryName() {
        return queryName;
    }

    public void setQueryName(String queryName) {
        this.queryName = queryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isInherited() {
        return inherited;
    }

    public void setInherited(boolean inherited) {
        this.inherited = inherited;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isQueryable() {
        return queryable;
    }

    public void setQueryable(boolean queryable) {
        this.queryable = queryable;
    }

    public boolean isOrderable() {
        return orderable;
    }

    public void setOrderable(boolean orderable) {
        this.orderable = orderable;
    }

    public boolean isOpenChoice() {
        return openChoice;
    }

    public void setOpenChoice(boolean openChoice) {
        this.openChoice = openChoice;
    }

    public PropertyTypeEnum getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(PropertyTypeEnum propertyType) {
        this.propertyType = propertyType;
    }

    public CardinalityEnum getCardinality() {
        return cardinality;
    }

    public void setCardinality(CardinalityEnum cardinality) {
        this.cardinality = cardinality;
    }

    public UpdatabilityEnum getUpdatability() {
        return updatability;
    }

    public void setUpdatability(UpdatabilityEnum updatability) {
        this.updatability = updatability;
    }

    @Override
    public String toString() {
        return "com.engagepoint.teama.cmismanager.common.model.PropertyRow{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", queryName='" + queryName + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", cardinality='" + cardinality + '\'' +
                ", updatability='" + updatability + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}