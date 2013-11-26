import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;

import java.util.*;

public class ObjectTypeReader {

    /**
     * Use this method to create TypeDTO instace from ObjectType instace
     * @param objType
     * @return TypeDTO
     */

    public static TypeDTO readIgnoreChildren(ObjectType objType) {
        TypeDTO dto = new TypeDTO();

        //private BaseTypeId baseTypeId;
        dto.setBaseTypeId(objType.getBaseTypeId().value()); //TODO change TypeDTO.class. This is shitcode.
        dto.setParentTypeId(objType.getParentTypeId()); //TODO change TypeDTO.class. This is shitcode.

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
        Map<String, PropertyDefinition<?>> propertyDefinitionMap = objType.getPropertyDefinitions();

        /*
        * TODO tests this
        */
        for(PropertyDefinition propertyDefinition : propertyDefinitionMap.values()) {

            try{
                PropertyRow propertyRow = ObjectTypeReader.readPropertyRow(propertyDefinition);
                propertyList.add(propertyRow);
            } catch (NullPointerException ex) {
                //TODO
            }
        }

        return propertyList;
    }

    private static PropertyRow readPropertyRow(PropertyDefinition definition) {

        PropertyRow propertyRow = new PropertyRow();

        try {
            propertyRow.setDisplayName(definition.getDisplayName());
            propertyRow.setId(definition.getId());
            propertyRow.setDescription(definition.getDescription());
            propertyRow.setLocalNamespace(definition.getLocalNamespace());
            propertyRow.setLocalName(definition.getLocalName());
            propertyRow.setQueryName(definition.getDisplayName());

            propertyRow.setCardinality(definition.getCardinality().value());
            propertyRow.setUpdatability(definition.getUpdatability().value());
            propertyRow.setQueryable(definition.isQueryable());
            propertyRow.setOrderable(definition.isOrderable());
            propertyRow.setRequired(definition.isRequired());
            propertyRow.setInherited(definition.isInherited());
            propertyRow.setOpenChoice(definition.isOpenChoice());
            //TODO PropertyRow
        } catch (NullPointerException ex) {
            //propertyRow.setDisplayName("(PropertyDefinition)pair.getValue() is null");
        }

        return propertyRow;
    }

    public static TypeDTO readTree(Tree<ObjectType> objectTypeTree) {
        ObjectType objectType = objectTypeTree.getItem();

        return ObjectTypeReader.readTree(objectType);
    }

    public static TypeDTO readTree(ObjectType objectType) {
        TypeDTO root = ObjectTypeReader.readIgnoreChildren(objectType);

        Iterator i = objectType.getChildren().iterator();
        List<TypeDTO> children = new ArrayList<TypeDTO>();
        while(i.hasNext())
        {
            ObjectType child = (ObjectType)i.next();
            children.add(readTree(child));
        }
        root.setChildren(children);

        return root;
    }
}