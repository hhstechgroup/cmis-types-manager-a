package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.biz.ejb.DataSorter;
import com.engagepoint.teama.cmismanager.biz.wrappers.TypeDefinitionWrapper;
import com.engagepoint.teama.cmismanager.common.exceptions.ValidationException;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import com.engagepoint.teama.cmismanager.common.util.FileStatusReport;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;

import java.util.*;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Created with IntelliJ IDEA.
 * User: aleksandr.davidiuk
 * Date: 12/27/13
 * Time: 6:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataSorterTest {
    static DataSorter dataSorter;

    @BeforeClass
    public static void init() {
        dataSorter = new DataSorter();
    }

    @Test
    public void testEmptyValidateAndSort() {
        Map<String, TypeDefinition> testMap = new HashMap<String, TypeDefinition>();
        List<FileStatusReport> testList = new ArrayList<FileStatusReport>();

        List<TypeDefinition> result = dataSorter.validateAndSort(testMap, testList);
        List<TypeDefinition> basic = new ArrayList<TypeDefinition>();

        Assert.assertTrue("OK", result.equals(basic));
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionValidateAndSort() {
        Map<String, TypeDefinition> testMap = null;
        List<FileStatusReport> testList = null;

        List<TypeDefinition> result = dataSorter.validateAndSort(testMap, testList);
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionValidateTypeDefinition() {
        TypeDTO typeDTO = new TypeDTO();
        TypeDefinitionWrapper typeDefinitionWrapper = new TypeDefinitionWrapper(typeDTO);

        try{
            dataSorter.validateTypeDefinition(typeDefinitionWrapper);
        }
        catch (ValidationException e) {
        }
    }
}
