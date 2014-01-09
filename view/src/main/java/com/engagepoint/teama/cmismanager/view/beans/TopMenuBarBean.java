package com.engagepoint.teama.cmismanager.view.beans;

import org.primefaces.component.menuitem.MenuItem;
import org.primefaces.model.DefaultMenuModel;
import org.primefaces.model.MenuModel;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;


/**
 * Created with IntelliJ IDEA.
 * User: v.kondratenko
 * Date: 12/25/13
 * Time: 2:37 PM
 * To change this template use File | Settings | File Templates.
 */
@ManagedBean
@RequestScoped
public class TopMenuBarBean {
    private MenuModel model;
    @ManagedProperty("#{login}")
    private LoginBean loginBean;

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    @PostConstruct
       public void initModel() {
        model = new DefaultMenuModel();
        UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
        String viewId = viewRoot.getViewId();
        addMenuItem(viewId, "home", "/show/", "index.xhtml", "Home ");
        addMenuItem(viewId, "settings","/dif/", "settings.xhtml", "Settings");
        addMenuItem(viewId, "logout", "/about/", "about.xhtml", "About");
    }

    private void addMenuItem(String viewId, String mID, String rootView, String address, String label) {
        MenuItem menuItem = new MenuItem();
        menuItem.setId(mID);
        if (viewId.startsWith(rootView)) {
            menuItem.setStyleClass("active");
        }
        menuItem.setValue(label);
        menuItem.setUrl(rootView + address);

        model.addMenuItem(menuItem);
    }

    public MenuModel getModel() {
        return model;
    }

    public void setModel(MenuModel model) {
        this.model = model;
    }
}