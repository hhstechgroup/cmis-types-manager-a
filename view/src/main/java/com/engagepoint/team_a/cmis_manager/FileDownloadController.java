package com.engagepoint.team_a.cmis_manager;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.InputStream;

@ManagedBean
public class FileDownloadController {
    private StreamedContent file;

    public FileDownloadController() {
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/WEB-INF/web.xml");
        file = new DefaultStreamedContent(stream, "image/jpg", "downloaded_optimus.xml");
    }

    public StreamedContent getFile() {
        return file;
    }
}

