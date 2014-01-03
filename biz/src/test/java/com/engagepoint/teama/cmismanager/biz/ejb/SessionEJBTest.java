package com.engagepoint.teama.cmismanager.biz.ejb;


import com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.spi.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class SessionEJBTest {

    @Test
    public void testCreateSession() throws Exception {
        SessionFactory sessionFactory = new SessionFactory() {
            @Override
            public Session createSession(Map<String, String> parameters) {
                return null;
            }

            @Override
            public List<Repository> getRepositories(Map<String, String> parameters) {
                return null;
            }
        };
        SessionFactory spy = spy(sessionFactory);
        SessionEJB sessionEJB = new SessionEJB();
        sessionEJB.sessionFactory = spy;
        Map<String, String> map = new HashMap<String, String>();
        map.put(SessionParameter.USER, "user");
        map.put(SessionParameter.PASSWORD, "password");
        map.put(SessionParameter.ATOMPUB_URL, "url" + "/atom11");
        map.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        map.put(SessionParameter.REPOSITORY_ID, "repoID");
        Session mockSession = mock(Session.class);
        when(spy.createSession(map)).thenReturn(mockSession);
        sessionEJB.createSession("user", "password", "url", "sessionId", "repoID");
        verify(spy).createSession(map);
        Assert.assertEquals(mockSession, sessionEJB.getSession("sessionId"));
    }

    @Test(expected = ConnectionException.class)
    public void testCloseSessionException() throws Exception {
        SessionEJB sessionEJB = new SessionEJB();
        sessionEJB.closeSession("sessionId");
        sessionEJB.getSession("sessionId");
    }

    @Test(expected = ConnectionException.class)
    public void testCloseSession() throws ConnectionException {
        SessionFactory sessionFactory = new SessionFactory() {
            @Override
            public Session createSession(Map<String, String> parameters) {
                return null;
            }

            @Override
            public List<Repository> getRepositories(Map<String, String> parameters) {
                return null;
            }
        };
        SessionFactory spy = spy(sessionFactory);
        SessionEJB sessionEJB = new SessionEJB();
        sessionEJB.sessionFactory = spy;
        Map<String, String> map = new HashMap<String, String>();
        map.put(SessionParameter.USER, "user");
        map.put(SessionParameter.PASSWORD, "password");
        map.put(SessionParameter.ATOMPUB_URL, "url" + "/atom11");
        map.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        map.put(SessionParameter.REPOSITORY_ID, "repoID");
        Session session = mock(Session.class);
        when(spy.createSession(map)).thenReturn(session);
        sessionEJB.createSession("user", "password", "url", "sessionId", "repoID");
        CmisBinding cmisBinding = mock(CmisBinding.class);
        when(session.getBinding()).thenReturn(cmisBinding);
        sessionEJB.closeSession("sessionId");
        sessionEJB.closeSession("sessionId");
    }
}