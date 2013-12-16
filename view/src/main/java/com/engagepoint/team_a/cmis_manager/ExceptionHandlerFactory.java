package com.engagepoint.team_a.cmis_manager;

/**
 * Created with IntelliJ IDEA.
 * User: bait
 * Date: 22.11.13
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
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