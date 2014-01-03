package com.engagepoint.teama.cmismanager.biz.ejb;
//package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.biz.ejb.ObjectTypeReader;
import com.engagepoint.teama.cmismanager.biz.ejb.ServiceEJB;
import com.engagepoint.teama.cmismanager.biz.ejb.SessionEJB;
import com.engagepoint.teama.cmismanager.biz.wrappers.TypeDefinitionWrapper;
import com.engagepoint.teama.cmismanager.common.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.common.model.BaseTypeEnum;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import com.engagepoint.teama.cmismanager.common.util.FileStatusReport;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;

import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: katerina.gluschenko
 * Date: 1/2/14
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceEJBTest {
    public static final String URL = "http://LAB4:8080/chemistry-opencmis-server-inmemory-0.10.0";
    public static final String REPOSITORY_NAME = "A1";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";

    @Mock
    SessionEJB sessionEJBMock;
    @Mock
    Session sessionMock;
    @Mock
    ObjectTypeReader objectTypeReader;
    @Mock
    ServiceEJB service;

    String sessionID;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
//        TypeDTO type = new TypeDTO();
//
//        type.setId("Test");
//        type.setLocalName("Test");
//        type.setLocalNamespace("Test");
//        type.setDisplayName("Test");
//        type.setQueryName("Test");
//        type.setDescription("Test");
//
//        type.setParentTypeId("Test");
//        type.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);
//
//        type.setCreatable(true);
//        type.setFileable(true);
//        type.setQueryable(true);
//
//        type.setIncludedInSupertypeQuery(true);
//        type.setControllableAcl(true);
//        type.setControllablePolicy(true);
//        type.setFulltextIndexed(true);
//
//        type.setMutabilityCanCreate(true);
//        type.setMutabilityCanDelete(true);
//        type.setMutabilityCanUpdate(true);
        UUID uuid = UUID.randomUUID();
        sessionID = uuid.toString();
        service = new ServiceEJB();
        service.sessionEJB = this.sessionEJBMock;
        service.objectTypeReader = this.objectTypeReader;
        try {

            when(sessionEJBMock.getSession(sessionID)).thenReturn(sessionMock);
        } catch (ConnectionException e) {
            e.printStackTrace();

        }
        //when(service.createType(new TypeDefinitionWrapper(type))).thenReturn(type);
    }

    @Test
    public void testConnect() {
        try {
            service.connect(USERNAME, PASSWORD, URL, sessionID, REPOSITORY_NAME);
            Mockito.verify(sessionEJBMock).createSession(USERNAME, PASSWORD, URL, sessionID, REPOSITORY_NAME);
        } catch (ConnectionException e) {
            Assert.fail();
        }

    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectNullURL() {
        try {

            service.connect(USERNAME, PASSWORD, null, sessionID, REPOSITORY_NAME);
        } catch (ConnectionException e) {
            Assert.fail();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConnectNullRepoName() {
        try {
            service.connect(USERNAME, PASSWORD, URL, sessionID, null);
        } catch (ConnectionException e) {
            Assert.fail();
        }
    }

    @Test
    public void testCreateType() {
        final String ID = "Test";
        TypeDTO newTypeDTO = new TypeDTO();

        newTypeDTO.setId(ID);
        newTypeDTO.setLocalName("Test");
        newTypeDTO.setLocalNamespace("Test");
        newTypeDTO.setDisplayName("Test");
        newTypeDTO.setQueryName("Test");
        newTypeDTO.setDescription("Test");

        newTypeDTO.setParentTypeId("Test");
        newTypeDTO.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        newTypeDTO.setCreatable(true);
        newTypeDTO.setFileable(true);
        newTypeDTO.setQueryable(true);

        newTypeDTO.setIncludedInSupertypeQuery(true);
        newTypeDTO.setControllableAcl(true);
        newTypeDTO.setControllablePolicy(true);
        newTypeDTO.setFulltextIndexed(true);

        newTypeDTO.setMutabilityCanCreate(true);
        newTypeDTO.setMutabilityCanDelete(true);
        newTypeDTO.setMutabilityCanUpdate(true);

        try {
            TypeDTO returned = service.createType(newTypeDTO, sessionID);
            Session session = sessionEJBMock.getSession(sessionID);
            TypeDefinitionWrapper typeDefinitionWrapper =
                    new TypeDefinitionWrapper(newTypeDTO);
            Mockito.verify(session).createType(typeDefinitionWrapper);
            //Assert.assertEquals(ID,returned.getId());
        } catch (BaseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testCreateTypes() {
        final int SIZE = 3;
        List<TypeDTO> typeDTOList = new ArrayList<TypeDTO>();
        for (int i = 0; i < SIZE; i++) {
            TypeDTO newTypeDTO = new TypeDTO();

            newTypeDTO.setId("Test" + i);
            newTypeDTO.setLocalName("Test" + i);
            newTypeDTO.setLocalNamespace("Test" + i);
            newTypeDTO.setDisplayName("Test" + i);
            newTypeDTO.setQueryName("Test" + i);
            newTypeDTO.setDescription("Test" + i);

            newTypeDTO.setParentTypeId("Test" + i);
            newTypeDTO.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

            newTypeDTO.setCreatable(true);
            newTypeDTO.setFileable(true);
            newTypeDTO.setQueryable(true);

            newTypeDTO.setIncludedInSupertypeQuery(true);
            newTypeDTO.setControllableAcl(true);
            newTypeDTO.setControllablePolicy(true);
            newTypeDTO.setFulltextIndexed(true);

            newTypeDTO.setMutabilityCanCreate(true);
            newTypeDTO.setMutabilityCanDelete(true);
            newTypeDTO.setMutabilityCanUpdate(true);
            typeDTOList.add(newTypeDTO);
        }
        List<FileStatusReport> report = null;
        try {
            report = service.createTypes(typeDTOList, sessionID);
        } catch (BaseException e) {
            Assert.fail();
        }
        Assert.assertEquals(SIZE, report.size());
    }

    @Test(expected = BaseException.class)
    public void testCreateTypesBaseException() throws BaseException {
        service.createTypes(null, sessionID);
    }

//    @Test
//    public void testCreateTypesBaseExceptionWithMock() {
//        List<TypeDTO> typeDTOList = new ArrayList<TypeDTO>();
//
//        TypeDTO newTypeDTO = new TypeDTO();
//
//        newTypeDTO.setId("Test");
//        newTypeDTO.setLocalName("Test");
//        newTypeDTO.setLocalNamespace("Test");
//        newTypeDTO.setDisplayName("Test");
//        newTypeDTO.setQueryName("Test");
//        newTypeDTO.setDescription("Test");
//
//        newTypeDTO.setParentTypeId("Test");
//        newTypeDTO.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);
//
//        newTypeDTO.setCreatable(true);
//        newTypeDTO.setFileable(true);
//        newTypeDTO.setQueryable(true);
//
//        newTypeDTO.setIncludedInSupertypeQuery(true);
//        newTypeDTO.setControllableAcl(true);
//        newTypeDTO.setControllablePolicy(true);
//        newTypeDTO.setFulltextIndexed(true);
//
//        newTypeDTO.setMutabilityCanCreate(true);
//        newTypeDTO.setMutabilityCanDelete(true);
//        newTypeDTO.setMutabilityCanUpdate(true);
//        typeDTOList.add(newTypeDTO);
//
//        List<FileStatusReport> report = null;
//        try{
//            Session session = sessionEJBMock.getSession(sessionID);
//            //session.createType();
//            when(session.createType(new TypeDefinitionWrapper(newTypeDTO))).thenReturn( new ObjectType());
//            service.createTypes(typeDTOList, sessionID);
//        }catch (BaseException e){
//            Assert.fail();
//        }
//    }

    @Test
    public void testDeleteType(){
        TypeDTO newTypeDTO = new TypeDTO();

        newTypeDTO.setId("Test");
        newTypeDTO.setLocalName("Test");
        newTypeDTO.setLocalNamespace("Test");
        newTypeDTO.setDisplayName("Test");
        newTypeDTO.setQueryName("Test");
        newTypeDTO.setDescription("Test");

        newTypeDTO.setParentTypeId("Test");
        newTypeDTO.setBaseTypeId(BaseTypeEnum.CMIS_DOCUMENT);

        newTypeDTO.setCreatable(true);
        newTypeDTO.setFileable(true);
        newTypeDTO.setQueryable(true);

        newTypeDTO.setIncludedInSupertypeQuery(true);
        newTypeDTO.setControllableAcl(true);
        newTypeDTO.setControllablePolicy(true);
        newTypeDTO.setFulltextIndexed(true);

        newTypeDTO.setMutabilityCanCreate(true);
        newTypeDTO.setMutabilityCanDelete(true);
        newTypeDTO.setMutabilityCanUpdate(true);

        Session session = null;
        try {
            session = sessionEJBMock.getSession(sessionID);
            service.deleteType(newTypeDTO, sessionID);
        } catch (ConnectionException e) {
            Assert.fail();
        } catch (BaseException e) {
            Assert.fail();
        }
        Mockito.verify(session).deleteType(newTypeDTO.getId());
    }

    @Test
    public void testDisconnect() {
        try {
            service.disconnect(sessionID);
            Mockito.verify(sessionEJBMock).closeSession(sessionID);
        } catch (ConnectionException e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetAllTypes()  {
        try {
            service.getAllTypes(sessionID);
            Session session = sessionEJBMock.getSession(sessionID);
            //session.getTypeDescendants(null, -1, true);
           Mockito.verify(session).getTypeDescendants(null, -1, true);
        } catch (BaseException e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetRepoList() throws Exception {

    }

    @Test
    public void testUpdateType() throws Exception {

    }
}
