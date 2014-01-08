package com.engagepoint.teama.cmismanager.view;

import com.engagepoint.teama.cmismanager.common.service.ServiceEJBRemote;
import com.engagepoint.teama.cmismanager.view.beans.LoginBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: v.kondratenko
 * Date: 1/2/14
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoginBeanTest {
    private HttpSession httpSession;
    private ServiceEJBRemote serviceEJBRemote;


    @Before
    public void setUp() {
        httpSession = Mockito.mock(HttpSession.class);
        serviceEJBRemote = Mockito.mock(ServiceEJBRemote.class);


    }

    @Test
    public void testDoLogin(){
        LoginBean loginBean = new LoginBean();
        LoginBean spyBean = Mockito.spy(loginBean);
        Mockito.doReturn(httpSession).when(spyBean).getHttpSessionTrue();
        spyBean.setService(serviceEJBRemote);
        spyBean.doLogin();
    }

    @Test
    public void testDoLogout(){
        LoginBean loginBean = new LoginBean();
        LoginBean spyBean = Mockito.spy(loginBean);
        Mockito.doReturn(httpSession).when(spyBean).getHttpSessionFalse();
        spyBean.setService(serviceEJBRemote);
        spyBean.doLogout();
    }



}
