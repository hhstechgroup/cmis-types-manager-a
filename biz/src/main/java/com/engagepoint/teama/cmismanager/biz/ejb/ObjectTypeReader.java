package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.common.model.BaseTypeEnum;
import com.engagepoint.teama.cmismanager.common.model.PropertyRow;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

@Stateless
@LocalBean
public class ObjectTypeReader {

    public TypeDTO readIgnoreChildren(ObjectType objType) {

        TypeDTO dto = readTypeDefinition(objType);
        dto.setPropertyRows((ArrayList) this.readProperties(objType));

        return dto;
    }

    public TypeDTO readTree(Tree<ObjectType> objectTypeTree) {
        ObjectType objectType = objectTypeTree.getItem();
        return this.readWithChildren(objectType);
    }

    public TypeDTO readTypeDefinition(TypeDefinition typeDefinition) {
        TypeDTO dto = new TypeDTO();

        dto.setBaseTypeId(BaseTypeEnum.fromValue(typeDefinition.getBaseTypeId().value()));
        dto.setParentTypeId(typeDefinition.getParentTypeId());

        dto.setMutabilityCanCreate(typeDefinition.getTypeMutability().canCreate());
        dto.setMutabilityCanDelete(typeDefinition.getTypeMutability().canDelete());
        dto.setMutabilityCanUpdate(typeDefinition.getTypeMutability().canUpdate());

        dto.setId(typeDefinition.getId());
        dto.setLocalName(typeDefinition.getLocalName());
        dto.setLocalNamespace(typeDefinition.getLocalNamespace());
        dto.setQueryName(typeDefinition.getQueryName());
        dto.setDisplayName(typeDefinition.getDisplayName());
        dto.setDescription(typeDefinition.getDescription());

        dto.setCreatable(typeDefinition.isCreatable());
        dto.setFileable(typeDefinition.isFileable());
        dto.setQueryable(typeDefinition.isQueryable());

        dto.setIncludedInSupertypeQuery(typeDefinition.isIncludedInSupertypeQuery());
        dto.setFulltextIndexed(typeDefinition.isFulltextIndexed());
        dto.setControllableAcl(typeDefinition.isControllableAcl());
        dto.setControllablePolicy(typeDefinition.isControllablePolicy());

        return dto;
    }

    public TypeDTO readWithChildren(ObjectType objectType) {
        TypeDTO root = this.readIgnoreChildren(objectType);

        Iterator i = objectType.getChildren().iterator();
        List<TypeDTO> children = new ArrayList<TypeDTO>();
        while (i.hasNext()) {
            ObjectType child = (ObjectType) i.next();
            children.add(readWithChildren(child));
        }
        root.setChildren(children);

        return root;
    }

    private List<PropertyRow> readProperties(ObjectType objType) {
        List<PropertyRow> propertyList = new ArrayList<PropertyRow>();
        Map<String, PropertyDefinition<?>> propertyDefinitionMap = new HashMap<String, PropertyDefinition<?>>();
        propertyDefinitionMap.putAll(objType.getPropertyDefinitions());

        for (PropertyDefinition propertyDefinition : propertyDefinitionMap.values()) {

            PropertyRow propertyRow = this.readPropertyRow(propertyDefinition);
            propertyList.add(propertyRow);
        }

        return propertyList;
    }

    private PropertyRow readPropertyRow(PropertyDefinition definition) {
        PropertyRow propertyRow = new PropertyRow();
        propertyRow.setDisplayName(definition.getDisplayName());
        propertyRow.setId(definition.getId());
        propertyRow.setDescription(definition.getDescription());
        propertyRow.setLocalNamespace(definition.getLocalNamespace());
        propertyRow.setLocalName(definition.getLocalName());
        propertyRow.setQueryName(definition.getDisplayName());

        propertyRow.setCardinality(definition.getCardinality().value());
        propertyRow.setUpdatability(definition.getUpdatability().value());
        propertyRow.setPropertyType(definition.getPropertyType().value());

        propertyRow.setQueryable(definition.isQueryable());
        propertyRow.setOrderable(definition.isOrderable());
        propertyRow.setRequired(definition.isRequired());
        propertyRow.setInherited(definition.isInherited());
        if (definition.isOpenChoice() != null) {
            propertyRow.setOpenChoice(definition.isOpenChoice());
        } else {
            propertyRow.setOpenChoice(false);
        }

        return propertyRow;
    }
}