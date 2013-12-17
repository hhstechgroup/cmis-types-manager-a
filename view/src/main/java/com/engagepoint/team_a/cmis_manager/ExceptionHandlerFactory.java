package com.engagepoint.team_a.cmis_manager;

public class ExceptionHandlerFactory extends javax.faces.context.ExceptionHandlerFactory {
    private final javax.faces.context.ExceptionHandlerFactory parent;

    public ExceptionHandlerFactory(final javax.faces.context.ExceptionHandlerFactory parent) {
        this.parent = parent;
    }

    @Override
    public ExceptionHandler getExceptionHandler() {
        return new ExceptionHandler(this.parent.getExceptionHandler());
    }
}