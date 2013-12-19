package com.engagepoint.teama.cmismanager.wrappers;

import com.engagepoint.teama.cmismanager.model.PropertyRow;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.Choice;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.enums.Cardinality;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;
import org.apache.chemistry.opencmis.commons.enums.Updatability;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PropertyDefinitionWrapper implements PropertyDefinition, Serializable {

    private PropertyRow row;

    public PropertyDefinitionWrapper(PropertyRow row) {
        this.row = row;
    }

    @Override
    public String getId() {
        return row.getId();
    }

    @Override
    public String getLocalName() {
        return row.getLocalName();
    }

    @Override
    public String getLocalNamespace() {
        return row.getLocalNamespace();
    }

    @Override
    public String getDisplayName() {
        return row.getDisplayName();
    }

    @Override
    public String getQueryName() {
        return row.getQueryName();
    }

    @Override
    public String getDescription() {
        return row.getDescription();
    }

    @Override
    public PropertyType getPropertyType() {
        return PropertyType.STRING;
    }

    @Override
    public Cardinality getCardinality() {
        return Cardinality.SINGLE;
    }

    @Override
    public Updatability getUpdatability() {
        return Updatability.READWRITE;
    }

    @Override
    public Boolean isInherited() {
        return row.isInherited();
    }

    @Override
    public Boolean isRequired() {
        return row.isRequired();
    }

    @Override
    public Boolean isQueryable() {
        return row.isQueryable();
    }

    @Override
    public Boolean isOrderable() {
        return row.isOrderable();
    }

    @Override
    public Boolean isOpenChoice() {
        return row.isOpenChoice();
    }

    @Override
    public List getDefaultValue() {
        return new ArrayList();
    }

    @Override
    public List<Choice> getChoices() {
        return new ArrayList<Choice>();
    }

    @Override
    public List<CmisExtensionElement> getExtensions() {
        return new ArrayList<CmisExtensionElement>();
    }

    @Override
    public void setExtensions(List<CmisExtensionElement> cmisExtensionElements) {
        //todo complete method,  this code is just for sonar
        return;
    }
}
