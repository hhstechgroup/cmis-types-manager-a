package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.common.exceptions.ValidationException;
import com.engagepoint.teama.cmismanager.common.util.FileStatusReport;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataSorterTest {

    private static DataSorter dataSorter;
    private static TypeMutability correctTypeMutability;

    @BeforeClass
    public static void init(){
        dataSorter = new DataSorter();
        correctTypeMutability = new TypeMutability() {
            @Override
            public Boolean canCreate() {
                return true;
            }

            @Override
            public Boolean canUpdate() {
                return true;
            }

            @Override
            public Boolean canDelete() {
                return true;
            }

            @Override
            public List<CmisExtensionElement> getExtensions() {
                return Collections.emptyList();
            }

            @Override
            public void setExtensions(List<CmisExtensionElement> extensions) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Test
    public void validateTypeDefinitionCorrect() {

        TypeDefinition mockedTypeDefinition = createCorrectMockedTypeDefinition("id", "parentID");

        try{
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
        }
        catch (ValidationException e) {
            Assert.fail("Must not throw exception ValidationException. Problem: " + e.getMessage());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void validateTypeDefinitionNullInput() {
        try{
            dataSorter.validateTypeDefinition(null);
        }
        catch (ValidationException e) {
            Assert.fail("Must not throw exception ValidationException. Problem: " + e.getMessage());
        }
    }

    @Test
    public void validateTypeDefinitionWrongID() {

        TypeDefinition mockedTypeDefinition = mock(TypeDefinition.class);

        when(mockedTypeDefinition.getId()).thenReturn("");

        when(mockedTypeDefinition.getLocalName()).thenReturn("LocalName");
        when(mockedTypeDefinition.getDisplayName()).thenReturn("DisplayName");
        when(mockedTypeDefinition.getLocalNamespace()).thenReturn("LocalNamespace");
        when(mockedTypeDefinition.getDescription()).thenReturn("Description");
        when(mockedTypeDefinition.getParentTypeId()).thenReturn("ParentTypeId");
        when(mockedTypeDefinition.getQueryName()).thenReturn("WrongQueryName");

        when(mockedTypeDefinition.getBaseTypeId()).thenReturn(BaseTypeId.CMIS_DOCUMENT);
        when(mockedTypeDefinition.getTypeMutability()).thenReturn(correctTypeMutability);

        when(mockedTypeDefinition.isFileable()).thenReturn(true);
        when(mockedTypeDefinition.isCreatable()).thenReturn(true);
        when(mockedTypeDefinition.isQueryable()).thenReturn(true);
        when(mockedTypeDefinition.isIncludedInSupertypeQuery()).thenReturn(true);
        when(mockedTypeDefinition.isFulltextIndexed()).thenReturn(true);
        when(mockedTypeDefinition.isControllableAcl()).thenReturn(true);
        when(mockedTypeDefinition.isControllablePolicy()).thenReturn(true);

        try{
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail("Must throw exception ValidationException");
        }
        catch (ValidationException e) {
            Assert.assertEquals("Type id must be set.", e.getMessage());
        }
    }

    @Test
    public void validateTypeDefinitionWrongQueryName() {

        TypeDefinition mockedTypeDefinition = mock(TypeDefinition.class);
        when(mockedTypeDefinition.getId()).thenReturn("ID");
        when(mockedTypeDefinition.getLocalName()).thenReturn("LocalName");
        when(mockedTypeDefinition.getDisplayName()).thenReturn("DisplayName");
        when(mockedTypeDefinition.getLocalNamespace()).thenReturn("LocalNamespace");
        when(mockedTypeDefinition.getDescription()).thenReturn("Description");
        when(mockedTypeDefinition.getParentTypeId()).thenReturn("ParentTypeId");

        when(mockedTypeDefinition.getBaseTypeId()).thenReturn(BaseTypeId.CMIS_DOCUMENT);
        when(mockedTypeDefinition.getTypeMutability()).thenReturn(correctTypeMutability);

        when(mockedTypeDefinition.isFileable()).thenReturn(true);
        when(mockedTypeDefinition.isCreatable()).thenReturn(true);
        when(mockedTypeDefinition.isQueryable()).thenReturn(true);
        when(mockedTypeDefinition.isIncludedInSupertypeQuery()).thenReturn(true);
        when(mockedTypeDefinition.isFulltextIndexed()).thenReturn(true);
        when(mockedTypeDefinition.isControllableAcl()).thenReturn(true);
        when(mockedTypeDefinition.isControllablePolicy()).thenReturn(true);

        //wrong query name
        when(mockedTypeDefinition.getQueryName()).thenReturn("WrongQueryName::!!?,,..");

        try{
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail("Must throw exception ValidationException");
        }
        catch (ValidationException e) {
            Assert.assertEquals("Query name contains invalid characters.", e.getMessage());
        }
    }

    @Test
    public void validateTypeDefinitionWrongAttributes() {

        List<ValidationException> expectedExceptionsList = new ArrayList<ValidationException>();

        TypeDefinition mockedTypeDefinition = mock(TypeDefinition.class);
        when(mockedTypeDefinition.getTypeMutability()).thenReturn(correctTypeMutability);
        when(mockedTypeDefinition.getId()).thenReturn("id");

        when(mockedTypeDefinition.getLocalName()).thenReturn("");
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.getLocalName()).thenReturn("LocalName");
        }

        when(mockedTypeDefinition.getQueryName()).thenReturn("");
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.getQueryName()).thenReturn("QueryName");
        }

        when(mockedTypeDefinition.isCreatable()).thenReturn(null);
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.isCreatable()).thenReturn(true);
        }

        when(mockedTypeDefinition.isFileable()).thenReturn(null);
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.isFileable()).thenReturn(true);
        }

        when(mockedTypeDefinition.isQueryable()).thenReturn(null);
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.isQueryable()).thenReturn(true);
        }

        when(mockedTypeDefinition.isControllablePolicy()).thenReturn(null);
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.isControllablePolicy()).thenReturn(true);
        }

        when(mockedTypeDefinition.isControllableAcl()).thenReturn(null);
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.isControllableAcl()).thenReturn(true);
        }

        when(mockedTypeDefinition.isFulltextIndexed()).thenReturn(null);
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.isFulltextIndexed()).thenReturn(true);
        }

        when(mockedTypeDefinition.isIncludedInSupertypeQuery()).thenReturn(null);
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.isIncludedInSupertypeQuery()).thenReturn(true);
        }

        when(mockedTypeDefinition.getBaseTypeId()).thenReturn(null);
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.getBaseTypeId()).thenReturn(BaseTypeId.CMIS_DOCUMENT);
        }

        when(mockedTypeDefinition.getParentTypeId()).thenReturn("");
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.getParentTypeId()).thenReturn("ParentTypeId");
        }

        Assert.assertEquals(11, expectedExceptionsList.size());
    }

    @Test
    public void validateTypeDefinitionWrongMutability() {
        List<ValidationException> expectedExceptionsList = new ArrayList<ValidationException>();

        TypeDefinition mockedTypeDefinition = createCorrectMockedTypeDefinition("id", "parentID");
        when(mockedTypeDefinition.getTypeMutability()).thenReturn(null);

        TypeMutability mockedTypeMutability = mock(TypeMutability.class);

        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeDefinition.getTypeMutability()).thenReturn(mockedTypeMutability);
        }

        when(mockedTypeMutability.canCreate()).thenReturn(null);
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeMutability.canCreate()).thenReturn(true);
        }

        when(mockedTypeMutability.canUpdate()).thenReturn(null);
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeMutability.canUpdate()).thenReturn(true);
        }

        when(mockedTypeMutability.canDelete()).thenReturn(null);
        try {
            dataSorter.validateTypeDefinition(mockedTypeDefinition);
            Assert.fail();
        } catch (ValidationException e) {
            expectedExceptionsList.add(e);
            when(mockedTypeMutability.canDelete()).thenReturn(true);
        }

        Assert.assertEquals(4, expectedExceptionsList.size());
    }

    @Test
    public void validateAndSortCorrectDataSet() {

        Map<String, TypeDefinition> typeDefinitionMap = new HashMap<String, TypeDefinition>();
        List<FileStatusReport> fileStatusReportList = new ArrayList<FileStatusReport>();

        TypeDefinition typeDefinition1 = createCorrectMockedTypeDefinition("1", "0");
        TypeDefinition typeDefinition11 = createCorrectMockedTypeDefinition("1x1", "1");
        TypeDefinition typeDefinition111 = createCorrectMockedTypeDefinition("1x1x1", "1x1");
        TypeDefinition typeDefinition2 = createCorrectMockedTypeDefinition("2", "0");
        TypeDefinition typeDefinition21 = createCorrectMockedTypeDefinition("2x1", "2");
        TypeDefinition typeDefinition3 = createCorrectMockedTypeDefinition("3", "0");

        typeDefinitionMap.put("type1.xml", typeDefinition1);
        typeDefinitionMap.put("type2.xml", typeDefinition2);
        typeDefinitionMap.put("type3.xml", typeDefinition3);
        typeDefinitionMap.put("type11.xml", typeDefinition11);
        typeDefinitionMap.put("type111.xml", typeDefinition111);
        typeDefinitionMap.put("type21.xml", typeDefinition21);

        TypeDefinition[] expectedTypeDefinitions =
                new TypeDefinition[] {typeDefinition1, typeDefinition2, typeDefinition3, typeDefinition11,
                        typeDefinition111, typeDefinition21};

        List<TypeDefinition> list = dataSorter.validateAndSort(typeDefinitionMap, fileStatusReportList);

        if (fileStatusReportList.size() == 1
                && fileStatusReportList.get(0).getName().equals("Ready to upload")
                && fileStatusReportList.get(0).getStatus().equals(expectedTypeDefinitions.length + " files.") ) {
            Assert.assertArrayEquals(expectedTypeDefinitions, list.toArray(new TypeDefinition[0]));
        } else {
            Assert.fail();
        }

    }

    @Test
    public void validateAndSortTestIDCollisionOnly() {

        Map<String, TypeDefinition> typeDefinitionMap = new HashMap<String, TypeDefinition>();
        List<FileStatusReport> fileStatusReportList = new ArrayList<FileStatusReport>();

        TypeDefinition typeDefinition1 = createCorrectMockedTypeDefinition("1", "0");
        TypeDefinition typeDefinition2 = createCorrectMockedTypeDefinition("2", "1");
        TypeDefinition typeDefinition3 = createCorrectMockedTypeDefinition("0", "2");

        typeDefinitionMap.put("type1.xml", typeDefinition1);
        typeDefinitionMap.put("type2.xml", typeDefinition2);
        typeDefinitionMap.put("type3.xml", typeDefinition3);

        List<TypeDefinition> list = dataSorter.validateAndSort(typeDefinitionMap, fileStatusReportList);

        if ( list.isEmpty() && fileStatusReportList.size() == 1
                && fileStatusReportList.get(0).getName().equals("ID collision")) {
            Assert.assertEquals("0 files.", fileStatusReportList.get(0).getStatus());
        } else {
            Assert.fail();
        }

    }

    @Test
    public void validateAndSortIDCollisionInSomePartOfInputTypes() {

        Map<String, TypeDefinition> typeDefinitionMap = new HashMap<String, TypeDefinition>();
        List<FileStatusReport> fileStatusReportList = new ArrayList<FileStatusReport>();

        TypeDefinition typeDefinition1 = createCorrectMockedTypeDefinition("1", "0");
        TypeDefinition typeDefinition2 = createCorrectMockedTypeDefinition("2", "1");
        TypeDefinition typeDefinition3 = createCorrectMockedTypeDefinition("0", "2");
        TypeDefinition typeDefinition4 = createCorrectMockedTypeDefinition("someID", "someParentID");

        typeDefinitionMap.put("type1.xml", typeDefinition1);
        typeDefinitionMap.put("type2.xml", typeDefinition2);
        typeDefinitionMap.put("type3.xml", typeDefinition3);
        typeDefinitionMap.put("type4.xml", typeDefinition4);

        List<TypeDefinition> list = dataSorter.validateAndSort(typeDefinitionMap, fileStatusReportList);

        if (list.size() == 1 && fileStatusReportList.size() == 1) {
            Assert.assertEquals(typeDefinition4, list.get(0));
        } else {
            Assert.fail();
        }

    }

    @Test
    public void validateAndSortFileStatusListContainsValue() {

        Map<String, TypeDefinition> typeDefinitionMap = new HashMap<String, TypeDefinition>();
        List<FileStatusReport> fileStatusReportList = new ArrayList<FileStatusReport>();

        FileStatusReport testedFileStatusReport = new FileStatusReport("TestName", "TestStatus");
        fileStatusReportList.add(testedFileStatusReport);

        TypeDefinition typeDefinition1 = createCorrectMockedTypeDefinition("1", "0");
        TypeDefinition typeDefinition2 = createCorrectMockedTypeDefinition("2", "1");
        TypeDefinition typeDefinition3 = createCorrectMockedTypeDefinition("0", "2");
        TypeDefinition typeDefinition4 = createCorrectMockedTypeDefinition("someID", "someParentID");

        typeDefinitionMap.put("type1.xml", typeDefinition1);
        typeDefinitionMap.put("type2.xml", typeDefinition2);
        typeDefinitionMap.put("type3.xml", typeDefinition3);
        typeDefinitionMap.put("type4.xml", typeDefinition4);

        List<TypeDefinition> list = dataSorter.validateAndSort(typeDefinitionMap, fileStatusReportList);

        if (list.size() == 1 && fileStatusReportList.size() == 2) {
            Assert.assertEquals(testedFileStatusReport, fileStatusReportList.get(0));
        } else {
            Assert.fail();
        }

    }

    @Test
    public void validateAndSortTypesHaveSameID() {

        Map<String, TypeDefinition> typeDefinitionMap = new HashMap<String, TypeDefinition>();
        List<FileStatusReport> fileStatusReportList = new ArrayList<FileStatusReport>();

        TypeDefinition sameIDTypeDefinition1 = createCorrectMockedTypeDefinition("1", "0");
        TypeDefinition typeDefinition1 = createCorrectMockedTypeDefinition("1", "0");
        TypeDefinition typeDefinition11 = createCorrectMockedTypeDefinition("1x1", "1");
        TypeDefinition typeDefinition111 = createCorrectMockedTypeDefinition("1x1x1", "1x1");
        TypeDefinition typeDefinition2 = createCorrectMockedTypeDefinition("2", "0");
        TypeDefinition typeDefinition21 = createCorrectMockedTypeDefinition("2x1", "2");
        TypeDefinition typeDefinition3 = createCorrectMockedTypeDefinition("3", "0");

        typeDefinitionMap.put("type1.xml", typeDefinition1);
        typeDefinitionMap.put("type1sameID.xml", sameIDTypeDefinition1);
        typeDefinitionMap.put("type2.xml", typeDefinition2);
        typeDefinitionMap.put("type3.xml", typeDefinition3);
        typeDefinitionMap.put("type11.xml", typeDefinition11);
        typeDefinitionMap.put("type111.xml", typeDefinition111);
        typeDefinitionMap.put("type21.xml", typeDefinition21);

        List<TypeDefinition> list = dataSorter.validateAndSort(typeDefinitionMap, fileStatusReportList);

        if (list.size() == 6 && fileStatusReportList.size() == 2) {
            Assert.assertEquals("type1sameID.xml", fileStatusReportList.get(0).getName());
        } else {
            Assert.fail();
        }

    }

    @Test
    public void validateAndSortValidationsFailReport() {

        Map<String, TypeDefinition> typeDefinitionMap = new HashMap<String, TypeDefinition>();
        List<FileStatusReport> fileStatusReportList = new ArrayList<FileStatusReport>();

        TypeDefinition typeDefinition1 = createCorrectMockedTypeDefinition("1", "0");
        when(typeDefinition1.getTypeMutability()).thenReturn(null);

        typeDefinitionMap.put("type1.xml", typeDefinition1);

        List<TypeDefinition> list = dataSorter.validateAndSort(typeDefinitionMap, fileStatusReportList);

        if (list.isEmpty() && fileStatusReportList.get(0).getName().equals("type1.xml")) {
            Assert.assertEquals("TypeMutability must be set.", fileStatusReportList.get(0).getStatus());
        } else {
            Assert.fail();
        }

    }

    private TypeDefinition createCorrectMockedTypeDefinition(String id, String parent) {

        TypeDefinition mockedTypeDefinition = mock(TypeDefinition.class);
        when(mockedTypeDefinition.getId()).thenReturn(id);
        when(mockedTypeDefinition.getLocalName()).thenReturn(id+"LocalName");
        when(mockedTypeDefinition.getDisplayName()).thenReturn(id+"DisplayName");
        when(mockedTypeDefinition.getLocalNamespace()).thenReturn(id+"LocalNamespace");
        when(mockedTypeDefinition.getDescription()).thenReturn(id+"Description");
        when(mockedTypeDefinition.getParentTypeId()).thenReturn(parent);
        when(mockedTypeDefinition.getQueryName()).thenReturn(id+"QueryName");

        when(mockedTypeDefinition.getBaseTypeId()).thenReturn(BaseTypeId.CMIS_DOCUMENT);
        when(mockedTypeDefinition.getTypeMutability()).thenReturn(correctTypeMutability);

        when(mockedTypeDefinition.isFileable()).thenReturn(true);
        when(mockedTypeDefinition.isCreatable()).thenReturn(true);
        when(mockedTypeDefinition.isQueryable()).thenReturn(true);
        when(mockedTypeDefinition.isIncludedInSupertypeQuery()).thenReturn(true);
        when(mockedTypeDefinition.isFulltextIndexed()).thenReturn(true);
        when(mockedTypeDefinition.isControllableAcl()).thenReturn(true);
        when(mockedTypeDefinition.isControllablePolicy()).thenReturn(true);

        return mockedTypeDefinition;
    }
}
