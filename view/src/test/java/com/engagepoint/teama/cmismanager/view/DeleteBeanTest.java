package com.engagepoint.teama.cmismanager.view;

import com.engagepoint.teama.cmismanager.common.model.BaseTypeEnum;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import com.engagepoint.teama.cmismanager.view.beans.DeleteBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class DeleteBeanTest {
    TypeDTO typeDTO;
    DeleteBean deleteBean;
    @Before
    public void setUp(){
        deleteBean = new DeleteBean();
        typeDTO = new TypeDTO();
        typeDTO.setId("Test");
        typeDTO.setQueryName("Test");
        typeDTO.setDisplayName("Test");
        typeDTO.setLocalName("Test");
        typeDTO.setParentTypeId("document");
        typeDTO.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

    }
    @Test
    public void testDeleteConditionsDeleteFalse() throws Exception {
        deleteBean.setCurrentDTO(typeDTO);
        deleteBean.deleteConditions();
        String message = "Can't delete " + typeDTO.getDisplayName() + " type!!";
        Assert.assertEquals(message, deleteBean.getErrorDialogMsg());
        Assert.assertFalse(deleteBean.isDisableDeleteBtn());
    }
    @Test
    public void testDeleteConditionsTypeDTONULL() throws Exception {
        deleteBean.setCurrentDTO(null);
        deleteBean.deleteConditions();
        Assert.assertEquals("You haven't chosen type", deleteBean.getErrorDialogMsg());
        Assert.assertFalse(deleteBean.isDisableDeleteBtn());
    }
    @Test
    public void testDeleteConditionsChildrenEmpty() throws Exception {
        typeDTO.setMutabilityCanDelete(true);
        deleteBean.setCurrentDTO(typeDTO);
        deleteBean.deleteConditions();
        Assert.assertEquals("Are you sure?", deleteBean.getErrorDialogMsg());
        Assert.assertTrue(deleteBean.isDisableDeleteBtn());
    }
    @Test
    public void testDeleteConditionsChildrenNotEmpty() throws Exception {
        TypeDTO child = new TypeDTO();
        child.setId("Child");
        child.setQueryName("Child");
        child.setDisplayName("Child");
        child.setLocalName("Child");
        child.setParentTypeId("Test");
        child.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);
        List list = new LinkedList();
        list.add(child);
        typeDTO.setChildren(list);
        typeDTO.setMutabilityCanDelete(true);
        deleteBean.setCurrentDTO(typeDTO);
        deleteBean.deleteConditions();
        Assert.assertEquals("Type has children, are you sure ?", deleteBean.getErrorDialogMsg());
        Assert.assertTrue(deleteBean.isDisableDeleteBtn());
    }
}
