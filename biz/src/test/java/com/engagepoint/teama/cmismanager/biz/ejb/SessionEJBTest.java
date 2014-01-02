package com.engagepoint.teama.cmismanager.biz.ejb;


import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class SessionEJBTest {
    SessionEJB mockSessionEJB;

    @Before
    public void init(){
        mockSessionEJB = mock(SessionEJB.class);
    }

    @Test
    public void testCreateSession() throws Exception {
        mockSessionEJB.createSession("qwe", "qwe", "asd", "asd", "zxc");
        verify(mockSessionEJB).createSession("qwe", "qwe", "asd", "asd", "zxc");
    }

    @Test
    public void testGetSession() throws Exception {
        mockSessionEJB.getSession(new String("1"));
        verify(mockSessionEJB).getSession(new String("1"));
    }
}