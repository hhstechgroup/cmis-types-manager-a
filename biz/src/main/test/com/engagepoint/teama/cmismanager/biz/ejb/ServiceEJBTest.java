package com.engagepoint.teama.cmismanager.biz.ejb;
//package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.common.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.common.model.BaseTypeEnum;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import org.apache.chemistry.opencmis.client.api.Session;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

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
    ServiceEJB service;
    String sessionID;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        //SessionEJB sessionEJBMock = Mockito.mock(SessionEJB.class);

        UUID uuid = UUID.randomUUID();
        sessionID = uuid.toString();
        service = new ServiceEJB();
        service.sessionEJB = this.sessionEJBMock;
        service.objectTypeReader=this.objectTypeReader;
        try {
            when(sessionEJBMock.getSession(sessionID)).thenReturn(sessionMock);
        } catch (ConnectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //test.when(test.getUniqueId()).thenReturn(43);
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
        try{
            service.connect(USERNAME, PASSWORD, URL, sessionID, null);
        } catch (ConnectionException e) {
            Assert.fail();
        }
    }

    @Test
    public void testCreateType(){
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
          //  returned.getId();
            //Assert.assertEquals(ID,);
        } catch (BaseException e) {
            Assert.fail();
        }

    }

    @Test
    public void testCreateTypes() throws Exception {

    }

    @Test
    public void testDeleteType() throws Exception {

    }

    @Test
    public void testDisconnect() throws Exception {

    }

    @Test
    public void testGetAllTypes() throws Exception {

    }

    @Test
    public void testGetRepoList() throws Exception {

    }

    @Test
    public void testUpdateType() throws Exception {

    }
}
