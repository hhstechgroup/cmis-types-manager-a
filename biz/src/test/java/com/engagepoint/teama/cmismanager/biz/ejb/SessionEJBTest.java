package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.biz.util.RepositoryInfo;
import com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.exceptions.CmisConnectionException;
import org.apache.chemistry.opencmis.commons.spi.CmisBinding;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.util.Map;
import java.util.HashMap;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConcurrentHashMap.class, SessionFactoryImpl.class, SessionEJB.class})
public class SessionEJBTest {

    private SessionEJB sessionEJB;
    private static Map<String, String> connectionParametersMap;

    @BeforeClass
    public static void initMap() {
        connectionParametersMap = new HashMap<String, String>();
        connectionParametersMap.put(SessionParameter.USER, "user");
        connectionParametersMap.put(SessionParameter.PASSWORD, "password");
        connectionParametersMap.put(SessionParameter.ATOMPUB_URL, "url" + "/atom11");
        connectionParametersMap.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        connectionParametersMap.put(SessionParameter.REPOSITORY_ID, "repoID");
    }

    @Before
    public void init() {
        sessionEJB = new SessionEJB();
    }

    @Test
    public void createSessionCorrect() throws ConnectionException {

        Session mockedSession = Mockito.mock(Session.class);

        SessionFactoryImpl mockedSessionFactory = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(mockedSessionFactory.createSession(Matchers.anyMapOf(String.class, String.class))).thenReturn(mockedSession);

        PowerMockito.mockStatic(SessionFactoryImpl.class);
        PowerMockito.when(SessionFactoryImpl.newInstance()).thenReturn(mockedSessionFactory);

        sessionEJB.createSession("user", "password", "url", "sessionId", "repoID");

        Mockito.verify(mockedSessionFactory, Mockito.times(1)).createSession(Matchers.anyMapOf(String.class, String.class));
    }

    @Test
    public void createSessionSameConnectionInfo() throws ConnectionException {

        Session mockedSession = Mockito.mock(Session.class);

        SessionFactoryImpl mockedSessionFactory = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(mockedSessionFactory.createSession(Matchers.anyMapOf(String.class, String.class))).thenReturn(mockedSession);

        PowerMockito.mockStatic(SessionFactoryImpl.class);
        PowerMockito.when(SessionFactoryImpl.newInstance()).thenReturn(mockedSessionFactory);

        sessionEJB.createSession("user", "password", "url", "someUniqueSessionID1", "repoID");
        sessionEJB.createSession("user", "password", "url", "someUniqueSessionID2", "repoID");

        Mockito.verify(mockedSessionFactory, Mockito.times(1)).createSession(Matchers.anyMapOf(String.class, String.class));
    }

    @Test (expected = ConnectionException.class)
    public void createSessionSessionIsNull() throws ConnectionException {

        SessionFactoryImpl mockedSessionFactory = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(mockedSessionFactory.createSession(connectionParametersMap)).thenReturn(null);

        PowerMockito.mockStatic(SessionFactoryImpl.class);
        PowerMockito.when(SessionFactoryImpl.newInstance()).thenReturn(mockedSessionFactory);

        sessionEJB.createSession("user", "password", "url", "sessionId", "repoID");
    }

    @Test (expected = ConnectionException.class)
    public void createSessionThrowsCmisBaseException() throws ConnectionException {

        SessionFactoryImpl mockedSessionFactory = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(mockedSessionFactory.createSession(connectionParametersMap)).thenThrow(new CmisConnectionException(""));

        PowerMockito.mockStatic(SessionFactoryImpl.class);
        PowerMockito.when(SessionFactoryImpl.newInstance()).thenReturn(mockedSessionFactory);

        sessionEJB.createSession("user", "password", "url", "sessionId", "repoID");
    }

    @Test
    public void getSessionCorrect() throws ConnectionException {

        Session expected = Mockito.mock(Session.class);

        SessionFactoryImpl mockedSessionFactory = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(mockedSessionFactory.createSession(Matchers.anyMapOf(String.class, String.class))).thenReturn(expected);

        PowerMockito.mockStatic(SessionFactoryImpl.class);
        PowerMockito.when(SessionFactoryImpl.newInstance()).thenReturn(mockedSessionFactory);

        sessionEJB.createSession("user", "password", "url", "sessionId", "repoID");

        Mockito.verify(mockedSessionFactory, Mockito.times(1)).createSession(Matchers.anyMapOf(String.class, String.class));

        Session actual = sessionEJB.getSession("sessionId");

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = ConnectionException.class)
    public void getSessionNoSuchID() throws ConnectionException {

        Session mockedSession = Mockito.mock(Session.class);

        SessionFactoryImpl mockedSessionFactory = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(mockedSessionFactory.createSession(Matchers.anyMapOf(String.class, String.class))).thenReturn(mockedSession);

        PowerMockito.mockStatic(SessionFactoryImpl.class);
        PowerMockito.when(SessionFactoryImpl.newInstance()).thenReturn(mockedSessionFactory);

        sessionEJB.createSession("user", "password", "url", "sessionId", "repoID");

        Mockito.verify(mockedSessionFactory, Mockito.times(1)).createSession(Matchers.anyMapOf(String.class, String.class));

        sessionEJB.getSession("someUniqueID");

        Assert.fail();
    }

    @Test(expected = ConnectionException.class)
    public void getSessionSessionIsNull() throws Exception {

        Session mockedSession = Mockito.mock(Session.class);

        Session session = null;

        ConcurrentHashMap mockedSessionMap = PowerMockito.mock(ConcurrentHashMap.class);

        RepositoryInfo repositoryInfo = new RepositoryInfo("user", "password", "url", "repoID");

        PowerMockito.when(mockedSessionMap.put(Matchers.eq("sessionId"), Matchers.any(RepositoryInfo.class))).thenReturn(null);
        PowerMockito.when(mockedSessionMap.put(Matchers.eq(repositoryInfo), Matchers.any(Session.class))).thenReturn(null);

        PowerMockito.when(mockedSessionMap.containsKey("sessionId")).thenReturn(true);
        PowerMockito.when(mockedSessionMap.containsKey(repositoryInfo)).thenReturn(false);

        PowerMockito.when(mockedSessionMap.get(Matchers.eq("sessionId"))).thenReturn(repositoryInfo);
        PowerMockito.when(mockedSessionMap.get(Matchers.eq(repositoryInfo))).thenReturn(session);

        PowerMockito.whenNew(ConcurrentHashMap.class).withNoArguments().thenReturn(mockedSessionMap);

        SessionEJB localSessionEJB = new SessionEJB();

        SessionFactoryImpl mockedSessionFactory = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(mockedSessionFactory.createSession(Matchers.anyMapOf(String.class, String.class))).thenReturn(mockedSession);

        PowerMockito.mockStatic(SessionFactoryImpl.class);
        PowerMockito.when(SessionFactoryImpl.newInstance()).thenReturn(mockedSessionFactory);

        localSessionEJB.createSession("user", "password", "url", "sessionId", "repoID");

        Mockito.verify(mockedSessionFactory, Mockito.times(1)).createSession(Matchers.anyMapOf(String.class, String.class));

        localSessionEJB.getSession("sessionId");

        Assert.fail();
    }

    @Test
    public void closeSessionCorrect() throws ConnectionException {

        CmisBinding mockedCmisBinding = Mockito.mock(CmisBinding.class);

        Session mockedSession = Mockito.mock(Session.class);
        Mockito.when(mockedSession.getBinding()).thenReturn(mockedCmisBinding);

        SessionFactoryImpl mockedSessionFactory = Mockito.mock(SessionFactoryImpl.class);
        Mockito.when(mockedSessionFactory.createSession(Matchers.anyMapOf(String.class, String.class))).thenReturn(mockedSession);

        PowerMockito.mockStatic(SessionFactoryImpl.class);
        PowerMockito.when(SessionFactoryImpl.newInstance()).thenReturn(mockedSessionFactory);

        sessionEJB.createSession("user", "password", "url", "sessionId", "repoID");

        Mockito.verify(mockedSessionFactory, Mockito.times(1)).createSession(Matchers.anyMapOf(String.class, String.class));

        sessionEJB.closeSession("sessionId");

        Mockito.verify(mockedSession, Mockito.times(1)).getBinding();
        Mockito.verify(mockedCmisBinding, Mockito.times(1)).close();
    }

    @Test (expected = ConnectionException.class)
    public void closeSessionWrongID() throws ConnectionException {
        sessionEJB.closeSession("sessionId");
    }
}