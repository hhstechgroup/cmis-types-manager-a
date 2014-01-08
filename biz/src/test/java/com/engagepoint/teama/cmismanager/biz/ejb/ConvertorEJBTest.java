package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.biz.wrappers.TypeDefinitionWrapper;
import com.engagepoint.teama.cmismanager.common.exceptions.ConvertationException;
import com.engagepoint.teama.cmismanager.common.model.BaseTypeEnum;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import com.engagepoint.teama.cmismanager.common.util.FileStatusReport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

public class ConvertorEJBTest {
    private TypeDTO typeDTO;
    private JsonXMLConvertor jsonXMLConvertor;
    private ConvertorEJB convertorEJB;
    private TypeDTO child;
    Map<String, byte[]> mapJSON;
    Map<String, byte[]> mapXML;
    byte [] typeParentJSON;
    byte [] typeChildJSON;
    byte [] typeParentXML;
    byte [] typeChildXML;
    List<FileStatusReport> listFSR;
    List<TypeDTO> typeDTOs;

    @Before
    public void setUp() throws ConvertationException {
        typeDTO = new TypeDTO();
        typeDTO.setId("Test");
        typeDTO.setQueryName("Test");
        typeDTO.setDisplayName("Test");
        typeDTO.setLocalName("Test");
        typeDTO.setParentTypeId("document");
        typeDTO.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);


        child = new TypeDTO();
        child.setId("Child");
        child.setQueryName("Child");
        child.setDisplayName("Child");
        child.setLocalName("Child");
        child.setParentTypeId("Test");
        child.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);
        List list = new LinkedList();
        list.add(child);
        typeDTO.setChildren(list);

        TypeDefinitionWrapper typeDefinition = new TypeDefinitionWrapper(typeDTO);
        jsonXMLConvertor = new JsonXMLConvertor();
        convertorEJB = new ConvertorEJB();
        convertorEJB.objectTypeReader = new ObjectTypeReader();
        convertorEJB.jsonXMLConvertor = new JsonXMLConvertor();
        convertorEJB.dataSorter = new DataSorter();

        typeParentJSON = jsonXMLConvertor.getJSONFromTypeInByteArray(typeDefinition);
        typeChildJSON = jsonXMLConvertor.getJSONFromTypeInByteArray(new TypeDefinitionWrapper(child));
        mapJSON = new HashMap<String, byte[]>();
        mapJSON.put(typeDTO.getDisplayName() + ".json", typeParentJSON);
        mapJSON.put(child.getDisplayName() + ".json", typeChildJSON);

        typeParentXML = jsonXMLConvertor.getXMLFromTypeInByteArray(typeDefinition);
        typeChildXML = jsonXMLConvertor.getXMLFromTypeInByteArray(new TypeDefinitionWrapper(child));
        mapXML = new HashMap<String, byte[]>();
        mapXML.put(typeDTO.getDisplayName() + ".xml", typeParentXML);
        mapXML.put(child.getDisplayName() + ".xml", typeChildXML);

        FileStatusReport fsr = new FileStatusReport("Ready to upload", "2 files.");
        listFSR = new LinkedList<FileStatusReport>();
        listFSR.add(fsr);
        typeDTOs = new ArrayList<TypeDTO>();
        typeDTOs.add(typeDTO);
        typeDTOs.add(child);
    }

    @Test
    public void testCreateJSONFromType() throws Exception {
        Assert.assertTrue(Arrays.equals(typeParentJSON, convertorEJB.createJSONFromType(typeDTO)));
    }

    @Test
    public void testCreateJSONFromTypeIncludeChildren() throws Exception {
        Map<String, byte[]> map = convertorEJB.createJSONFromTypeIncludeChildren(typeDTO);
        Assert.assertTrue(equalMaps(mapJSON, map));
    }

    @Test
    public void testCreateXMLFromType() throws Exception {
        Assert.assertTrue(Arrays.equals(typeParentXML, convertorEJB.createXMLFromType(typeDTO)));
    }

    @Test
    public void testCreateXMLFromTypeIncludeChildren() throws Exception {
        Map<String, byte[]> map = convertorEJB.createXMLFromTypeIncludeChildren(typeDTO);
        Assert.assertTrue(equalMaps(mapXML, map));
    }

    @Test
    public void testReadAndValidateJSON() throws Exception {
        Assert.assertEquals(listFSR.get(0).getName(), convertorEJB.readAndValidate(mapJSON).getStatusReportList().get(0).getName());
        Assert.assertEquals(listFSR.get(0).getStatus(), convertorEJB.readAndValidate(mapJSON).getStatusReportList().get(0).getStatus());
        Assert.assertTrue(typeDTOs.equals(convertorEJB.readAndValidate(mapJSON).getSortedTypeList()));

    }

    boolean equalMaps(Map<String, byte []> m1, Map<String, byte []> m2) {
        boolean b = true;
        if (m1.size() != m2.size()){
            b = false;
        }
        for (String key: m1.keySet()){
            if ( !Arrays.equals(m1.get(key), m2.get(key) ) ){
                b = false;
            }
        }
        return b;
    }
}
