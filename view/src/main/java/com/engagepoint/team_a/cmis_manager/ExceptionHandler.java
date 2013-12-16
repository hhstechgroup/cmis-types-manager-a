package com.engagepoint.team_a.cmis_manager;

import javax.faces.FacesException;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import java.util.Iterator;

public class ExceptionHandler extends ExceptionHandlerWrapper {

    private final javax.faces.context.ExceptionHandler wrapped;

    public ExceptionHandler(final javax.faces.context.ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public javax.faces.context.ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator iterator = getUnhandledExceptionQueuedEvents().iterator();

        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext)event.getSource();
            Throwable throwable = context.getException();
            FacesContext fc = FacesContext.getCurrentInstance();

            try {
                Flash flash = fc.getExternalContext().getFlash();
                flash.put("errorDetails", throwable.getMessage());

                String tmp = "";
                for(int i = 0; i < throwable.getStackTrace().length; ++i)  tmp = tmp + throwable.getStackTrace()[i] + "\n";
                flash.put("errorLog", tmp);

                System.out.println("ErrorCatched" + throwable.getMessage());

                NavigationHandler navigationHandler = fc.getApplication().getNavigationHandler();
                navigationHandler.handleNavigation(fc, null, "errorUnhandled?faces-redirect=true");

                fc.renderResponse();
            }
            finally {
                iterator.remove();
            }
        }

        getWrapped().handle();
    }
}