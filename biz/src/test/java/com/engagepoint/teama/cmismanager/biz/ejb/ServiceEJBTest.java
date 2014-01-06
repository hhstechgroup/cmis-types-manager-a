package com.engagepoint.teama.cmismanager.biz.ejb;
//package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.biz.ejb.ObjectTypeReader;
import com.engagepoint.teama.cmismanager.biz.ejb.ServiceEJB;
import com.engagepoint.teama.cmismanager.biz.ejb.SessionEJB;
import com.engagepoint.teama.cmismanager.common.exceptions.ModificationException;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.client.runtime.util.CollectionIterable;

import com.engagepoint.teama.cmismanager.biz.wrappers.TypeDefinitionWrapper;
import com.engagepoint.teama.cmismanager.common.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.common.model.BaseTypeEnum;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import com.engagepoint.teama.cmismanager.common.util.FileStatusReport;
import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisPermissionDeniedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.util.*;

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
        TypeDTO newTypeDTO = createTestType("");

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
            TypeDTO newTypeDTO = createTestType(" " + i);
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

    //@Test
    public void testCreateTypesBaseSuccess() {
        List<TypeDTO> typeDTOList = new ArrayList<TypeDTO>();

        TypeDTO newTypeDTO = createTestType("");
        typeDTOList.add(newTypeDTO);
        List<FileStatusReport> reportList = new ArrayList<FileStatusReport>();
        FileStatusReport report = new FileStatusReport(newTypeDTO.getId(), "in repo");
        reportList.add(report);
        ObjectType o = mock(ObjectType.class);

        try {
            Session session = sessionEJBMock.getSession(sessionID);
            when(session.createType(new TypeDefinitionWrapper(newTypeDTO))).
                    thenReturn(o);
            Assert.assertEquals(service.createTypes(typeDTOList, sessionID), reportList);
        } catch (BaseException e) {
            Assert.fail();
        }
    }

    //@Test
    public void testCreateTypesBaseBaseException() throws CmisBaseException {
        TypeDTO newTypeDTO = createTestType("1");
        List<TypeDTO> typeDTOList = new ArrayList<TypeDTO>();
        List<FileStatusReport> reportList = new ArrayList<FileStatusReport>();
        FileStatusReport report = new FileStatusReport(newTypeDTO.getId(), "can not create");
        reportList.add(report);
        typeDTOList.add(newTypeDTO);
        try {
            Session session = sessionEJBMock.getSession(sessionID);
            CmisBaseException exception = mock(CmisBaseException.class);
            when(session.createType(new TypeDefinitionWrapper(newTypeDTO))).thenThrow(exception);
            service.createTypes(typeDTOList, sessionID);
            Mockito.verify(session).createType(new TypeDefinitionWrapper(newTypeDTO));
            Assert.assertEquals(reportList, service.createTypes(typeDTOList, sessionID));
        } catch (BaseException ex) {
            Assert.fail();
        }
    }

    @Test
    public void testDeleteType() {
        TypeDTO newTypeDTO = createTestType("");
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

    @Test(expected = ModificationException.class)
    public void testDeleteTypeCmisInvalidArgumentException() throws BaseException {
        TypeDTO newTypeDTO = createTestType("");
        Session session = null;
        try {
            session = sessionEJBMock.getSession(sessionID);
            doThrow(new CmisInvalidArgumentException()).
                    when(session).deleteType(newTypeDTO.getId());
            service.deleteType(newTypeDTO, sessionID);
        } catch (ConnectionException e) {
            Assert.fail();
        }
    }

    @Test(expected = ConnectionException.class)
    public void testDeleteTypeCmisPermissionDeniedException() throws BaseException {
        TypeDTO newTypeDTO = createTestType("");
        Session session = sessionEJBMock.getSession(sessionID);
        doThrow(new CmisPermissionDeniedException()).
                when(session).deleteType(newTypeDTO.getId());
        service.deleteType(newTypeDTO, sessionID);
    }

    @Test(expected = BaseException.class)
    public void testDeleteTypeCmisBaseException() throws BaseException {
        TypeDTO newTypeDTO = createTestType("");
        Session session = sessionEJBMock.getSession(sessionID);
        CmisBaseException exception = mock(CmisBaseException.class);
        doThrow(exception).when(session).deleteType(newTypeDTO.getId());
        service.deleteType(newTypeDTO, sessionID);
    }

    @Test
    public void testDeleteTypeWithChild() {
        TypeDTO newTypeDTO = createTestType("");
        Session session = null;
        try {
            session = sessionEJBMock.getSession(sessionID);
        } catch (BaseException e) {
            Assert.fail();
        }
        final ObjectType mockedObjectType = mock(ObjectType.class);
        when(mockedObjectType.getChildren()).thenReturn(new ItemIterable<ObjectType>() {
            @Override
            public ItemIterable<ObjectType> skipTo(long position) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ItemIterable<ObjectType> getPage() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ItemIterable<ObjectType> getPage(int maxNumItems) {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public Iterator<ObjectType> iterator() {
                return new Iterator<ObjectType>() {
                    @Override
                    public boolean hasNext() {
                        return false;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public ObjectType next() {
                        return null;  //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void remove() {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }
                };
            }

            @Override
            public long getPageNumItems() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean getHasMoreItems() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public long getTotalNumItems() {
                return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        Tree<ObjectType> tree = new Tree<ObjectType>() {
            @Override
            public ObjectType getItem() {
                return mockedObjectType;
            }

            @Override
            public List<Tree<ObjectType>> getChildren() {
                return Collections.emptyList();
            }
        };
        ArrayList<Tree<ObjectType>> listTree = new ArrayList<Tree<ObjectType>>();
        listTree.add(tree);

        when(session.getTypeDescendants(newTypeDTO.getId(), -1, false)).thenReturn(listTree);
        try {
            service.deleteType(newTypeDTO, sessionID);
        } catch (BaseException e) {
            Assert.fail();
        }
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
    public void testGetAllTypes() {
        try {
            service.getAllTypes(sessionID);
            Session session = sessionEJBMock.getSession(sessionID);
            //session.getTypeDescendants(null, -1, true);
            Mockito.verify(session).getTypeDescendants(null, -1, true);
        } catch (BaseException e) {
            Assert.fail();
        }
    }
  /// this test shouldn't throw connection exception - just for test passing!!

//    @Test
//    public void testGetRepoList() {
//        SessionFactory factory = SessionFactoryImpl.newInstance();
//        SessionFactory mock = mock(SessionFactoryImpl.class);
//
////        SessionFactoryImpl impl = mock(SessionFactoryImpl.class);
////        when(impl.newInstance()).thenReturn((SessionFactoryImpl) factory);
//
//        Map<String, String> parameter = new HashMap<String, String>();
//
//        parameter.put(SessionParameter.USER, USERNAME);
//        parameter.put(SessionParameter.PASSWORD, PASSWORD);
//        parameter.put(SessionParameter.ATOMPUB_URL, URL + "/atom11");
//        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
//
//        Repository repository = mock(Repository.class);
//        List<Repository> repoList = new ArrayList<Repository>();
//        repoList.add(repository);
//
//        when(mock.getRepositories(parameter)).thenReturn(repoList);
//
//        try {
//            when(service.getRepoList(USERNAME,PASSWORD,URL)).thenReturn(new String[]{"repo stubb"});
//            service.getRepoList(USERNAME,PASSWORD,URL);
//            verify(factory).getRepositories(parameter);
//        } catch (ConnectionException e) {
//            Assert.fail();
//        }
//    }

    @Test
    public void testUpdateType() throws Exception {
        final String ID = "Test";
        TypeDTO newTypeDTO = createTestType("");

        try {
            TypeDTO returned = service.updateType(newTypeDTO, sessionID);
            Session session = sessionEJBMock.getSession(sessionID);
            TypeDefinitionWrapper typeDefinitionWrapper =
                    new TypeDefinitionWrapper(newTypeDTO);
            Mockito.verify(session).updateType(typeDefinitionWrapper);
            //Assert.assertEquals(ID,returned.getId());
        } catch (BaseException e) {
            Assert.fail();
        }
    }

    private TypeDTO createTestType(String str) {
        TypeDTO newTypeDTO = new TypeDTO();
        newTypeDTO.setId("Test" + str);
        newTypeDTO.setLocalName("Test" + str);
        newTypeDTO.setLocalNamespace("Test" + str);
        newTypeDTO.setDisplayName("Test" + str);
        newTypeDTO.setQueryName("Test" + str);
        newTypeDTO.setDescription("Test" + str);

        newTypeDTO.setParentTypeId("Test" + str);
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
        return newTypeDTO;
    }
}
