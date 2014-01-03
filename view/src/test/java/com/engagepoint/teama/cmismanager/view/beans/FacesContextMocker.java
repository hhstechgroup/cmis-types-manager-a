package com.engagepoint.teama.cmismanager.view.beans;

import org.mockito.Mockito;

import javax.faces.context.FacesContext;

/**
 * Created with IntelliJ IDEA.
 * User: v.kondratenko
 * Date: 1/2/14
 * Time: 5:56 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class FacesContextMocker extends FacesContext {
    private FacesContextMocker() { }
    public static FacesContext mockFacesContext() {
        FacesContext context = Mockito.mock(FacesContext.class);
        setCurrentInstance(context);

        return context;
    }
}