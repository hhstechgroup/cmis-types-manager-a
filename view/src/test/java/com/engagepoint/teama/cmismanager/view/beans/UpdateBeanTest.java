package com.engagepoint.teama.cmismanager.view.beans;

import com.engagepoint.teama.cmismanager.common.model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: v.kondratenko
 * Date: 1/3/14
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class UpdateBeanTest {
    private PropertyRow propertyRow;
    private TypeDTO typeDTO;
    UpdateBean updateBean;
    @Before
    public void init(){
        updateBean = new UpdateBean();

        propertyRow = new PropertyRow();
        propertyRow.setDisplayName("DisplayName1");
        propertyRow.setId("ID1");
        propertyRow.setDescription("Description1");
        propertyRow.setLocalNamespace("LocalNamespace1");
        propertyRow.setLocalName("LocalName1");
        propertyRow.setQueryName("Query1");

        propertyRow.setCardinality(CardinalityEnum.MULTI);
        propertyRow.setUpdatability(UpdatabilityEnum.ONCREATE);
        propertyRow.setPropertyType(PropertyTypeEnum.BOOLEAN);

        propertyRow.setQueryable(true);
        propertyRow.setOrderable(true);
        propertyRow.setRequired(true);
        propertyRow.setInherited(true);
        propertyRow.setOpenChoice(true);

        typeDTO = new TypeDTO();
        typeDTO.setDescription("Description1");
        typeDTO.setDisplayName("DisplayName1");
        typeDTO.setQueryName("Query1");
        typeDTO.setLocalName("LocalName1");
        typeDTO.setLocalNamespace("LocalNamespace1");

        typeDTO.setCreatable(true);
        typeDTO.setFileable(true);
        typeDTO.setQueryable(true);

        typeDTO.setIncludedInSupertypeQuery(true);
        typeDTO.setControllableAcl(true);
        typeDTO.setControllablePolicy(true);
        typeDTO.setFulltextIndexed(true);

        typeDTO.setMutabilityCanCreate(true);
        typeDTO.setMutabilityCanDelete(true);
        typeDTO.setMutabilityCanUpdate(true);

        typeDTO.getPropertyRows().addAll(new ArrayList<PropertyRow>());

    }
    @Test
    public void testSetNewPropertyRowUpdate(){

        updateBean.setNewPropertyRowUpdate(propertyRow);
        updateBean.submitChanges();
    }

    @Test
    public void testUpdateType(){

        updateBean.setNewTypeDTO(typeDTO);
        updateBean.setCurrentDTO(new TypeDTO());
        updateBean.updateType();
    }


}
