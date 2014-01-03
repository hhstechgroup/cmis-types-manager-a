package com.engagepoint.teama.cmismanager.biz.util;

import com.engagepoint.teama.cmismanager.biz.util.TypesComparator;
import com.engagepoint.teama.cmismanager.biz.wrappers.TypeDefinitionWrapper;
import com.engagepoint.teama.cmismanager.common.model.BaseTypeEnum;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TypesComparatorTest {

    private static TypesComparator comparator;

    @BeforeClass
    public static void init(){
        comparator = new TypesComparator();
    }

    @Test(expected = NullPointerException.class)
    public void compareNullPointerException(){
        Assert.assertEquals(comparator.compare(null, null), 1);
    }

    @Test
    public void compareEqualsTypes(){

        TypeDTO someTestType1 = new TypeDTO();
        someTestType1.setId("ID1");
        someTestType1.setQueryName("Query1");
        someTestType1.setDisplayName("DisplayName1");
        someTestType1.setParentTypeId("ParentID1");
        someTestType1.setLocalName("LocalName1");
        someTestType1.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDTO equalsToSomeTestType1 = new TypeDTO();
        equalsToSomeTestType1.setId("ID1");
        equalsToSomeTestType1.setQueryName("Query1");
        equalsToSomeTestType1.setDisplayName("DisplayName1");
        equalsToSomeTestType1.setParentTypeId("ParentID1");
        equalsToSomeTestType1.setLocalName("LocalName1");
        equalsToSomeTestType1.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDefinitionWrapper typeDefinitionWrapper1 = new TypeDefinitionWrapper(someTestType1);
        TypeDefinitionWrapper typeDefinitionWrapper2 = new TypeDefinitionWrapper(equalsToSomeTestType1);

        Assert.assertEquals(comparator.compare(typeDefinitionWrapper1, typeDefinitionWrapper2), 0);
    }

    @Test
    public void compareTypesWithEqualsID(){
        TypeDTO someTestType1 = new TypeDTO();
        someTestType1.setId("someID1");
        someTestType1.setQueryName("Query1");
        someTestType1.setDisplayName("DisplayName1");
        someTestType1.setParentTypeId("ParentID1");
        someTestType1.setLocalName("LocalName1");
        someTestType1.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDTO equalsToSomeTestType1ByID = new TypeDTO();
        equalsToSomeTestType1ByID.setId("someID1");
        equalsToSomeTestType1ByID.setQueryName("NonEqualsQuery1");
        equalsToSomeTestType1ByID.setDisplayName("NonEqualsDisplayName1");
        equalsToSomeTestType1ByID.setParentTypeId("NonEqualsParentID1");
        equalsToSomeTestType1ByID.setLocalName("NonEqualsLocalName1");
        equalsToSomeTestType1ByID.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDefinitionWrapper typeDefinitionWrapper1 = new TypeDefinitionWrapper(someTestType1);
        TypeDefinitionWrapper typeDefinitionWrapper2 = new TypeDefinitionWrapper(equalsToSomeTestType1ByID);

        Assert.assertEquals(comparator.compare(typeDefinitionWrapper1, typeDefinitionWrapper2), 0);
    }

    @Test
    public void compareTypesWithEqualsQueryName(){
        TypeDTO someTestType1 = new TypeDTO();
        someTestType1.setId("ID1");
        someTestType1.setQueryName("someEqualsQuery1");
        someTestType1.setDisplayName("DisplayName1");
        someTestType1.setParentTypeId("ParentID1");
        someTestType1.setLocalName("LocalName1");
        someTestType1.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDTO equalsToSomeTestType1ByID = new TypeDTO();
        equalsToSomeTestType1ByID.setId("NonEqualsID1");
        equalsToSomeTestType1ByID.setQueryName("someEqualsQuery1");
        equalsToSomeTestType1ByID.setDisplayName("NonEqualsDisplayName1");
        equalsToSomeTestType1ByID.setParentTypeId("NonEqualsParentID1");
        equalsToSomeTestType1ByID.setLocalName("NonEqualsLocalName1");
        equalsToSomeTestType1ByID.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDefinitionWrapper typeDefinitionWrapper1 = new TypeDefinitionWrapper(someTestType1);
        TypeDefinitionWrapper typeDefinitionWrapper2 = new TypeDefinitionWrapper(equalsToSomeTestType1ByID);

        Assert.assertEquals(comparator.compare(typeDefinitionWrapper1, typeDefinitionWrapper2), 0);
    }

    @Test
    public void compareTypesWithEqualsLocalName(){
        TypeDTO someTestType1 = new TypeDTO();
        someTestType1.setId("ID1");
        someTestType1.setQueryName("Query1");
        someTestType1.setDisplayName("DisplayName1");
        someTestType1.setParentTypeId("ParentID1");
        someTestType1.setLocalName("someEqualsLocalName1");
        someTestType1.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDTO equalsToSomeTestType1ByID = new TypeDTO();
        equalsToSomeTestType1ByID.setId("NonEqualsID1");
        equalsToSomeTestType1ByID.setQueryName("NonEqualsQuery1");
        equalsToSomeTestType1ByID.setDisplayName("NonEqualsDisplayName1");
        equalsToSomeTestType1ByID.setParentTypeId("NonEqualsParentID1");
        equalsToSomeTestType1ByID.setLocalName("someEqualsLocalName1");
        equalsToSomeTestType1ByID.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDefinitionWrapper typeDefinitionWrapper1 = new TypeDefinitionWrapper(someTestType1);
        TypeDefinitionWrapper typeDefinitionWrapper2 = new TypeDefinitionWrapper(equalsToSomeTestType1ByID);

        Assert.assertEquals(comparator.compare(typeDefinitionWrapper1, typeDefinitionWrapper2), 0);
    }

    @Test
    public void compareDifferentTypes(){
        TypeDTO someTestType1 = new TypeDTO();
        someTestType1.setId("ID1");
        someTestType1.setQueryName("Query1");
        someTestType1.setDisplayName("DisplayName1");
        someTestType1.setParentTypeId("ParentID1");
        someTestType1.setLocalName("LocalName1");
        someTestType1.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDTO someTestType2 = new TypeDTO();
        someTestType2.setId("ID2");
        someTestType2.setQueryName("Query2");
        someTestType2.setDisplayName("DisplayName2");
        someTestType2.setParentTypeId("ParentID2");
        someTestType2.setLocalName("LocalName2");
        someTestType2.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDefinitionWrapper typeDefinitionWrapper1 = new TypeDefinitionWrapper(someTestType1);
        TypeDefinitionWrapper typeDefinitionWrapper2 = new TypeDefinitionWrapper(someTestType2);

        int compareResult = someTestType1.getId().compareTo(someTestType2.getId());

        Assert.assertEquals(comparator.compare(typeDefinitionWrapper1, typeDefinitionWrapper2), compareResult);
    }

    @Test
    public void compareDifferentTypesWithSameParentID(){
        TypeDTO someTestType1 = new TypeDTO();
        someTestType1.setId("ID1");
        someTestType1.setQueryName("Query1");
        someTestType1.setDisplayName("DisplayName1");
        someTestType1.setParentTypeId("sameParentID");
        someTestType1.setLocalName("LocalName1");
        someTestType1.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDTO someTestType2 = new TypeDTO();
        someTestType2.setId("ID2");
        someTestType2.setQueryName("Query2");
        someTestType2.setDisplayName("DisplayName2");
        someTestType2.setParentTypeId("sameParentID");
        someTestType2.setLocalName("LocalName2");
        someTestType2.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDefinitionWrapper typeDefinitionWrapper1 = new TypeDefinitionWrapper(someTestType1);
        TypeDefinitionWrapper typeDefinitionWrapper2 = new TypeDefinitionWrapper(someTestType2);

        int compareResult = someTestType1.getId().compareTo(someTestType2.getId());

        Assert.assertEquals(comparator.compare(typeDefinitionWrapper1, typeDefinitionWrapper2), compareResult);
    }

    @Test
    public void compareDifferentTypesWithSameParentIDAndDifferentBaseTypes(){
        TypeDTO someTestType1 = new TypeDTO();
        someTestType1.setId("ID1");
        someTestType1.setQueryName("Query1");
        someTestType1.setDisplayName("DisplayName1");
        someTestType1.setParentTypeId("sameParentID");
        someTestType1.setLocalName("LocalName1");
        someTestType1.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDTO someTestType2 = new TypeDTO();
        someTestType2.setId("ID2");
        someTestType2.setQueryName("Query2");
        someTestType2.setDisplayName("DisplayName2");
        someTestType2.setParentTypeId("sameParentID");
        someTestType2.setLocalName("LocalName2");
        someTestType2.setBaseTypeId(BaseTypeEnum.CMIS_ITEM);

        TypeDefinitionWrapper typeDefinitionWrapper1 = new TypeDefinitionWrapper(someTestType1);
        TypeDefinitionWrapper typeDefinitionWrapper2 = new TypeDefinitionWrapper(someTestType2);

        Assert.assertEquals(comparator.compare(typeDefinitionWrapper1, typeDefinitionWrapper2), 0);
    }

}
