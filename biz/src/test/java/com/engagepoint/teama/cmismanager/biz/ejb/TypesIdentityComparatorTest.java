package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.biz.util.TypesIdentityComparator;
import com.engagepoint.teama.cmismanager.biz.wrappers.TypeDefinitionWrapper;
import com.engagepoint.teama.cmismanager.common.model.BaseTypeEnum;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: v.kondratenko
 * Date: 12/31/13
 * Time: 11:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class TypesIdentityComparatorTest {
    private TypesIdentityComparator comparator;
    @Before
    public void init(){
        comparator = new TypesIdentityComparator();
    }

    @Test(expected = NullPointerException.class)
    public void testCompareNullPointerExc(){
        Assert.assertEquals(comparator.compare(null, null), 1);
    }

    @Test ()
    public void testCompareEqualsTrue(){
        TypeDTO typeDTO1 = new TypeDTO();
        typeDTO1.setId("Test");
        typeDTO1.setQueryName("Test");
        typeDTO1.setDisplayName("Test");
        typeDTO1.setParentTypeId("Test");
        typeDTO1.setLocalName("Test");
        typeDTO1.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDTO typeDTO2 = new TypeDTO();
        typeDTO2.setId("Test");
        typeDTO2.setQueryName("Test");
        typeDTO2.setDisplayName("Test");
        typeDTO2.setParentTypeId("Test");
        typeDTO2.setLocalName("Test");
        typeDTO2.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDefinitionWrapper wrapper1 = new TypeDefinitionWrapper(typeDTO1);
        TypeDefinitionWrapper wrapper2 = new TypeDefinitionWrapper(typeDTO2);
        Assert.assertEquals(comparator.compare(wrapper1, wrapper2), 0);
    }

    @Test ()
    public void testCompareEqualsFalse(){
        TypeDTO typeDTO1 = new TypeDTO();
        typeDTO1.setId("Test");
        typeDTO1.setQueryName("Test");
        typeDTO1.setDisplayName("Test");
        typeDTO1.setParentTypeId("Test");
        typeDTO1.setLocalName("Test");
        typeDTO1.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDTO typeDTO2 = new TypeDTO();
        typeDTO2.setId("Test2");
        typeDTO2.setQueryName("Test2");
        typeDTO2.setDisplayName("Test2");
        typeDTO2.setParentTypeId("Test2");
        typeDTO2.setLocalName("Test2");
        typeDTO2.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        TypeDefinitionWrapper wrapper1 = new TypeDefinitionWrapper(typeDTO1);
        TypeDefinitionWrapper wrapper2 = new TypeDefinitionWrapper(typeDTO2);

        Assert.assertNotEquals(comparator.compare(wrapper1, wrapper2), 0);
    }


}
