package com.engagepoint.team_a.cmis_manager;

import java.io.InputStream;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
@ManagedBean(name = "download")
@SessionScoped
public class FileDownloadController {

    private StreamedContent file;

    public FileDownloadController() {
        InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/WEB-INF/o.txt");
        file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.txt");
    }

    public StreamedContent getFile() {
        return file;
    }
}