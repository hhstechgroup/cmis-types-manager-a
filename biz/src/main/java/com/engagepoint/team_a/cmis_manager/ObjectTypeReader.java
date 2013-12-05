package com.engagepoint.team_a.cmis_manager;

import com.engagepoint.team_a.cmis_manager.model.BaseTypeEnum;
import com.engagepoint.team_a.cmis_manager.model.PropertyRow;
import com.engagepoint.team_a.cmis_manager.model.TypeDTO;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;

import java.util.*;

public class ObjectTypeReader {

    /**
     * Use this method to create com.engagepoint.team_a.cmis_manager.model.TypeDTO instace from ObjectType instace
     *
     * @param objType
     * @return com.engagepoint.team_a.cmis_manager.model.TypeDTO
     */

    public static TypeDTO readIgnoreChildren(ObjectType objType) {
        TypeDTO dto = new TypeDTO();

        dto.setBaseTypeId(BaseTypeEnum.fromValue(objType.getBaseTypeId().value()));
        dto.setParentTypeId(objType.getParentTypeId());

        dto.setMutabilityCanCreate(objType.getTypeMutability().canCreate());
        dto.setMutabilityCanDelete(objType.getTypeMutability().canDelete());
        dto.setMutabilityCanUpdate(objType.getTypeMutability().canUpdate());

        dto.setId(objType.getId());
        dto.setLocalName(objType.getLocalName());
        dto.setLocalNamespace(objType.getLocalNamespace());
        dto.setQueryName(objType.getQueryName());
        dto.setDisplayName(objType.getDisplayName());
        dto.setDescription(objType.getDescription());

        dto.setCreatable(objType.isCreatable());
        dto.setFileable(objType.isFileable());
        dto.setQueryable(objType.isQueryable());

        dto.setIncludedInSupertypeQuery(objType.isIncludedInSupertypeQuery());
        dto.setFulltextIndexed(objType.isFulltextIndexed());
        dto.setControllableAcl(objType.isControllableAcl());
        dto.setControllablePolicy(objType.isControllablePolicy());
        dto.setPropertyRows(ObjectTypeReader.readProperties(objType));

        return dto;
    }

    private static ArrayList<PropertyRow> readProperties(ObjectType objType) {

        ArrayList<PropertyRow> propertyList = new ArrayList<PropertyRow>();
        Map<String, PropertyDefinition<?>> propertyDefinitionMap = new HashMap<String, PropertyDefinition<?>>();
        propertyDefinitionMap.putAll(objType.getPropertyDefinitions());

        for (PropertyDefinition propertyDefinition : propertyDefinitionMap.values()) {

            PropertyRow propertyRow = ObjectTypeReader.readPropertyRow(propertyDefinition);
            propertyList.add(propertyRow);
        }

        return propertyList;
    }

    private static PropertyRow readPropertyRow(PropertyDefinition definition) {

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

        try {
            // definition.isOpenChoice() for BaseTypes return null
            propertyRow.setOpenChoice(definition.isOpenChoice());
        } catch (NullPointerException e) {
            propertyRow.setOpenChoice(false);
        }

        return propertyRow;
    }

    public static TypeDTO readTree(Tree<ObjectType> objectTypeTree) {
        ObjectType objectType = objectTypeTree.getItem();

        return ObjectTypeReader.readWithChildren(objectType);
    }

    public static TypeDTO readWithChildren(ObjectType objectType) {
        TypeDTO root = ObjectTypeReader.readIgnoreChildren(objectType);

        Iterator i = objectType.getChildren().iterator();
        List<TypeDTO> children = new ArrayList<TypeDTO>();
        while (i.hasNext()) {
            ObjectType child = (ObjectType) i.next();
            children.add(readWithChildren(child));
        }
        root.setChildren(children);

        return root;
    }
}