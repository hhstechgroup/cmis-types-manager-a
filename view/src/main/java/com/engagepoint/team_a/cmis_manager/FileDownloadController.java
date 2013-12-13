package com.engagepoint.team_a.cmis_manager;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.InputStream;

@ManagedBean
@SessionScoped
public class FileDownloadController {

    private StreamedContent file;
    private String type;
    private String[] types = {"xml", "json"};

    public FileDownloadController() {
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        this.types = types;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }

    public void setURL() {
        file = null;
        String url = "/WEB-INF/web." + type;
        String saveName = "downloaded_optimus." + type;
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream(url);
        file = new DefaultStreamedContent(stream, "image/jpg", saveName);
    }
}


