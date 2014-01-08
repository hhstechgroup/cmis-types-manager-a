package com.engagepoint.teama.cmismanager.biz.ejb;

import static org.powermock.api.mockito.PowerMockito.mock;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.Matchers;

import com.engagepoint.teama.cmismanager.common.exceptions.ConvertationException;

import org.apache.chemistry.opencmis.client.util.TypeUtils;
import org.apache.chemistry.opencmis.commons.data.CmisExtensionElement;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.definitions.TypeMutability;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParseException;

import javax.xml.stream.XMLStreamException;

import java.io.*;
import java.util.List;
import java.util.Collections;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TypeUtils.class, JsonXMLConvertor.class})
public class JsonXMLConverterTest {

    private static final String MISSING_FILE = "Test file missing";
    private static JsonXMLConvertor jsonXMLConvertor;
    private static TypeDefinition mockedTypeDefinition;

    @BeforeClass
    public static void init() throws IOException {
        jsonXMLConvertor = new JsonXMLConvertor();

        TypeMutability correctTypeMutability = new TypeMutability() {
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

        mockedTypeDefinition = PowerMockito.mock(TypeDefinition.class);

        PowerMockito.when(mockedTypeDefinition.getId()).thenReturn("my-document");
        PowerMockito.when(mockedTypeDefinition.getQueryName()).thenReturn("mydocument");
        PowerMockito.when(mockedTypeDefinition.getLocalName()).thenReturn("my-document");
        PowerMockito.when(mockedTypeDefinition.getDisplayName()).thenReturn("CMIS Document");
        PowerMockito.when(mockedTypeDefinition.getLocalNamespace()).thenReturn("local");
        PowerMockito.when(mockedTypeDefinition.getDescription()).thenReturn("Description of My Document Type");
        PowerMockito.when(mockedTypeDefinition.getParentTypeId()).thenReturn("cmis:document");

        PowerMockito.when(mockedTypeDefinition.getBaseTypeId()).thenReturn(BaseTypeId.CMIS_DOCUMENT);
        PowerMockito.when(mockedTypeDefinition.getTypeMutability()).thenReturn(correctTypeMutability);

        PowerMockito.when(mockedTypeDefinition.isFileable()).thenReturn(true);
        PowerMockito.when(mockedTypeDefinition.isCreatable()).thenReturn(true);
        PowerMockito.when(mockedTypeDefinition.isQueryable()).thenReturn(true);
        PowerMockito.when(mockedTypeDefinition.isControllableAcl()).thenReturn(true);

        PowerMockito.when(mockedTypeDefinition.isControllablePolicy()).thenReturn(false);
        PowerMockito.when(mockedTypeDefinition.isIncludedInSupertypeQuery()).thenReturn(false);
        PowerMockito.when(mockedTypeDefinition.isFulltextIndexed()).thenReturn(false);
    }

    /**
     @Test(expected = ConvertationException.class)
     public void staticMockTest() throws IOException, JSONParseException, ConvertationException {

     TypeDefinition mockedTypeDefinition = mock(TypeDefinition.class);

     InputStream mockedInputStream =  mock(InputStream.class);

     PowerMockito.mockStatic(TypeUtils.class);
     PowerMockito.when(TypeUtils.readFromJSON(mockedInputStream)).thenThrow(new JSONParseException(3));

     TypeDefinition existed = jsonXMLConvertor.createTypeFromJSON(mockedInputStream);

     Assert.assertEquals(mockedTypeDefinition, existed);
     }
     */

    @Test
    public void getXMLFromTypeInByteArrayIsTestFileExists() {
        Assert.assertNotNull(MISSING_FILE, getClass().getResource("/types/xml/cmis/my-document.xml"));
    }

    @Test
    public void getXMLFromTypeInByteArrayCorrectInput() throws IOException, ConvertationException {

        //create 'expected' byte array
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();

        InputStream fileStream = getClass().getResourceAsStream("/types/xml/cmis/my-document.xml");
        int i;
        while ((i = fileStream.read()) >= 0) {
            byteArrayStream.write((byte) i);
        }
        byte [] expected = byteArrayStream.toByteArray();
        fileStream.close();
        byteArrayStream.close();

        // existed byte array
        byte[] existed = jsonXMLConvertor.getXMLFromTypeInByteArray(mockedTypeDefinition);

        Assert.assertArrayEquals(expected, existed);
    }

    @Test (expected = ConvertationException.class)
    public void getXMLFromTypeInByteArrayWhenThrowsXMLStreamException() throws ConvertationException, XMLStreamException {

        TypeDefinition mockedForTestTypeDefinition = mock(TypeDefinition.class);

        PowerMockito.mockStatic(TypeUtils.class);
        PowerMockito.doThrow(new XMLStreamException("")).when(TypeUtils.class);
        TypeUtils.writeToXML(Matchers.eq(mockedForTestTypeDefinition), Matchers.<OutputStream>anyObject());

        jsonXMLConvertor.getXMLFromTypeInByteArray(mockedForTestTypeDefinition);

        Assert.fail();
    }

    @Test
    public void getJSONFromTypeInByteArrayIsTestFileExists() {
        Assert.assertNotNull(MISSING_FILE, getClass().getResource("/types/json/my-document.json"));
    }

    @Test
    public void getJSONFromTypeInByteArrayCorrectInput() throws IOException, ConvertationException {

        //create 'expected' byte array
        ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();

        InputStream fileStream = getClass().getResourceAsStream("/types/json/my-document.json");
        int i;
        while ((i = fileStream.read()) >= 0) {
            byteArrayStream.write((byte) i);
        }
        byte [] expected = byteArrayStream.toByteArray();
        fileStream.close();
        byteArrayStream.close();

        // actual byte array
        byte[] actual = jsonXMLConvertor.getJSONFromTypeInByteArray(mockedTypeDefinition);

        Assert.assertArrayEquals(expected, actual);
    }

    @Test (expected = ConvertationException.class)
    public void getJSONFromTypeInByteArrayWhenThrowsIOException() throws ConvertationException, IOException {

        TypeDefinition mockedForTestTypeDefinition = mock(TypeDefinition.class);

        PowerMockito.mockStatic(TypeUtils.class);
        PowerMockito.doThrow(new IOException("")).when(TypeUtils.class);
        TypeUtils.writeToJSON(Matchers.eq(mockedForTestTypeDefinition), Matchers.<OutputStream>anyObject());

        jsonXMLConvertor.getJSONFromTypeInByteArray(mockedForTestTypeDefinition);

        Assert.fail();
    }

    @Test
    public void createTypeFromJSONIsTestFileExists() {
        Assert.assertNotNull(MISSING_FILE, getClass().getResource("/types/json/my-document.json"));
    }

    @Test
    public void createTypeFromJSONCorrectInput() throws FileNotFoundException, ConvertationException {

        InputStream fileStream = getClass().getResourceAsStream("/types/json/my-document.json");

        TypeDefinition actual = jsonXMLConvertor.createTypeFromJSON(fileStream);

        Assert.assertTrue(this.isTypesEquals(actual, mockedTypeDefinition));
    }

    @Test (expected = ConvertationException.class)
    public void createTypeFromJSONWhenThrowsJSONParseException() throws IOException, ConvertationException, JSONParseException {

        FileInputStream mockedFileInputStream = mock(FileInputStream.class);

        PowerMockito.mockStatic(TypeUtils.class);
        PowerMockito.doThrow(new JSONParseException(1)).when(TypeUtils.class);
        TypeUtils.readFromJSON(mockedFileInputStream);

        jsonXMLConvertor.createTypeFromJSON(mockedFileInputStream);

        Assert.fail();
    }

    @Test (expected = ConvertationException.class)
    public void createTypeFromJSONWhenThrowsIOException() throws IOException, ConvertationException, JSONParseException {

        FileInputStream mockedFileInputStream = mock(FileInputStream.class);

        PowerMockito.mockStatic(TypeUtils.class);
        PowerMockito.doThrow(new IOException("")).when(TypeUtils.class);
        TypeUtils.readFromJSON(mockedFileInputStream);

        jsonXMLConvertor.createTypeFromJSON(mockedFileInputStream);

        Assert.fail();

    }

    @Test
    public void createTypeFromXMLIsTestFileExists() {
        Assert.assertNotNull(MISSING_FILE, getClass().getResource("/types/xml/oasis/doc1.xml"));
    }

    @Test
    public void createTypeFromXMLCorrectInput() throws FileNotFoundException, ConvertationException {

        InputStream fileStream = getClass().getResourceAsStream("/types/xml/oasis/doc1.xml");

        TypeDefinition actual = jsonXMLConvertor.createTypeFromXML(fileStream);

        Assert.assertTrue(this.isTypesEquals(actual, mockedTypeDefinition));
    }

    @Test (expected = ConvertationException.class)
    public void createTypeFromXMLWhenThrowsXMLStreamException() throws ConvertationException, XMLStreamException {

        FileInputStream mockedFileInputStream = mock(FileInputStream.class);

        PowerMockito.mockStatic(TypeUtils.class);
        PowerMockito.doThrow(new XMLStreamException("")).when(TypeUtils.class);
        TypeUtils.readFromXML(mockedFileInputStream);

        jsonXMLConvertor.createTypeFromXML(mockedFileInputStream);

        Assert.fail();

    }

    /**
     * Check is two TypeDefinition equal.
     * @param o1 some TypeDefinition
     * @param o2 some TypeDefinition
     * @return true is o1 and o2 equals
     */
    private boolean isTypesEquals(TypeDefinition o1, TypeDefinition o2) {

        return ( o1.getId().equals(o2.getId())
                && o1.getQueryName().equals(o2.getQueryName())
                && o1.getLocalName().equals(o2.getLocalName())
                && o1.getDisplayName().equals(o2.getDisplayName())
                && o1.getLocalNamespace().equals(o2.getLocalNamespace())
                && o1.getDescription().equals(o2.getDescription())
                && o1.getParentTypeId().equals(o2.getParentTypeId())
                && o1.getBaseTypeId().value().equals(o2.getBaseTypeId().value())
                && o1.isCreatable().equals(o2.isCreatable())
                && o1.isFileable().equals(o2.isFileable())
                && o1.isQueryable().equals(o2.isQueryable())
                && o1.isControllableAcl().equals(o2.isControllableAcl())
                && o1.isControllablePolicy().equals(o2.isControllablePolicy())
                && o1.isFulltextIndexed().equals(o2.isFulltextIndexed())
                && o1.isIncludedInSupertypeQuery().equals(o2.isIncludedInSupertypeQuery())
        );
    }
}
