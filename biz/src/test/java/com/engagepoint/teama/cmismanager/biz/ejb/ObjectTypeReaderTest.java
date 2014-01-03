package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.biz.wrappers.PropertyDefinitionWrapper;
import com.engagepoint.teama.cmismanager.common.model.BaseTypeEnum;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.PropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.Cardinality;
import org.apache.chemistry.opencmis.commons.enums.PropertyType;
import org.apache.chemistry.opencmis.commons.enums.Updatability;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ObjectTypeReaderTest {

    private static ObjectTypeReader objectTypeReader;
    private static TypeMutability correctTypeMutability;
    private static Updatability updatability;
    private static PropertyType propertyType;
    private static Cardinality cardinality;

    @BeforeClass
    public static void init() {
        objectTypeReader = new ObjectTypeReader();
        correctTypeMutability = new TypeMutability() {
            @Override
            public Boolean canCreate() {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Boolean canUpdate() {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Boolean canDelete() {
                return true;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<CmisExtensionElement> getExtensions() {
                return Collections.EMPTY_LIST;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setExtensions(List<CmisExtensionElement> cmisExtensionElements) {
                throw new UnsupportedOperationException();
            }
        };
    }




    @Test
    public void testReadIgnoreChildren()
    {
        PropertyDefinition mockedPropertyDefinition = mock(PropertyDefinition.class);
        when(mockedPropertyDefinition.getId()).thenReturn("propertyID1");
        when(mockedPropertyDefinition.getLocalName()).thenReturn("propertyLocalName");
        when(mockedPropertyDefinition.getPropertyType()).thenReturn(propertyType.INTEGER);
        when(mockedPropertyDefinition.getQueryName()).thenReturn("propertyQueryName");
        when(mockedPropertyDefinition.getCardinality()).thenReturn(cardinality.MULTI);
        when(mockedPropertyDefinition.getChoices()).thenReturn(Collections.EMPTY_LIST);
        when(mockedPropertyDefinition.getDefaultValue()).thenReturn(Collections.EMPTY_LIST);
        when(mockedPropertyDefinition.getLocalNamespace()).thenReturn("propertyLocalNamespace");
        when(mockedPropertyDefinition.getDescription()).thenReturn("propertyDescription");
        when(mockedPropertyDefinition.getDisplayName()).thenReturn("propertyDisplayName");
        when(mockedPropertyDefinition.getUpdatability()).thenReturn(updatability.READWRITE);
        when(mockedPropertyDefinition.isInherited()).thenReturn(true);
        when(mockedPropertyDefinition.isOpenChoice()).thenReturn(true);
        when(mockedPropertyDefinition.isOrderable()).thenReturn(true);
        when(mockedPropertyDefinition.isQueryable()).thenReturn(true);
        when(mockedPropertyDefinition.isRequired()).thenReturn(true);

        ObjectType mockedObjectType = mock(ObjectType.class);
        when(mockedObjectType.getId()).thenReturn("id");
        when(mockedObjectType.getLocalName()).thenReturn("LocalName");
        when(mockedObjectType.getDisplayName()).thenReturn("DisplayName");
        when(mockedObjectType.getLocalNamespace()).thenReturn("LocalNamespace");
        when(mockedObjectType.getDescription()).thenReturn("Description");
        when(mockedObjectType.getParentTypeId()).thenReturn("ParentTypeId");
        when(mockedObjectType.getQueryName()).thenReturn("WrongQueryName");

        when(mockedObjectType.getBaseTypeId()).thenReturn(BaseTypeId.CMIS_DOCUMENT);
        when(mockedObjectType.getTypeMutability()).thenReturn(correctTypeMutability);

        when(mockedObjectType.isFileable()).thenReturn(true);
        when(mockedObjectType.isCreatable()).thenReturn(true);
        when(mockedObjectType.isQueryable()).thenReturn(true);
        when(mockedObjectType.isIncludedInSupertypeQuery()).thenReturn(true);
        when(mockedObjectType.isFulltextIndexed()).thenReturn(true);
        when(mockedObjectType.isControllableAcl()).thenReturn(true);
        when(mockedObjectType.isControllablePolicy()).thenReturn(true);

        Map<String, PropertyDefinition<?>> returnedMap = new HashMap<String, PropertyDefinition<?>>();
        returnedMap.put("propertyID1", mockedPropertyDefinition);

        when(mockedObjectType.getPropertyDefinitions()).thenReturn(returnedMap);


        TypeDTO typeDTO1 = objectTypeReader.readIgnoreChildren(mockedObjectType);



        if (typeDTO1.getId().equals(mockedObjectType.getId())
                && typeDTO1.getQueryName().equals(mockedObjectType.getQueryName())
                && typeDTO1.getLocalName().equals(mockedObjectType.getLocalName())
                && typeDTO1.getDisplayName().equals(mockedObjectType.getDisplayName())
                && typeDTO1.getLocalNamespace().equals(mockedObjectType.getLocalNamespace())
                && typeDTO1.getDescription().equals(mockedObjectType.getDescription())
                && typeDTO1.getParentTypeId().equals(mockedObjectType.getParentTypeId())
                && typeDTO1.getBaseTypeId().toString().equals(mockedObjectType.getBaseTypeId().value())

                ) {

            Assert.assertTrue(true);

        } else {
            Assert.fail();
        }


    }


    private ObjectType createCorrectMockedObjectType(String id, String parent) {
    return null;
    }
}
