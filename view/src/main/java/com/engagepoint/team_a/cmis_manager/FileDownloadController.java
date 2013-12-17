package com.engagepoint.team_a.cmis_manager;

import com.engagepoint.team_a.cmis_manager.exceptions.BaseException;
import com.engagepoint.team_a.cmis_manager.model.TypeDTO;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;

@ManagedBean
@SessionScoped
public class FileDownloadController {

    private StreamedContent file;
    private String type;
    private String[] types = {"xml", "json"};
    @ManagedProperty(value = "#{treeBean}")
    private TreeBean treeBean;
    @ManagedProperty(value = "#{error}")
    private ErrorBean errorBean;

    public void setErrorBean(ErrorBean errorBean) {
        this.errorBean = errorBean;
    }

    public void setTreeBean(TreeBean treeBean) {
        this.treeBean = treeBean;
    }

    public FileDownloadController() {
    }

    public String[] getTypes() {
        return types;
    }
    public void setDownloadedFile(){

        TypeDTO currentType = treeBean.getCurrentDTO();
        InputStream stream = null;

       if(type.equals("xml")){
            try {
                stream = JsonXMLConvertor.createFileFromType(currentType, SupportedFileFormat.XML);
            } catch (BaseException e) {
                errorBean.setErrorMessage(e.getMessage());
                errorBean.setErrorVisibility("true");
            }
        } else if(type.equals("json")){
            try {
                stream = JsonXMLConvertor.createFileFromType(currentType, SupportedFileFormat.JSON);
            } catch (BaseException e) {
                errorBean.setErrorMessage(e.getMessage());
                errorBean.setErrorVisibility("true");
            }
        }

        String saveName = currentType.getId() + "." + type;
        file = new DefaultStreamedContent(stream, "image/jpg", saveName);

        try {
            if(stream != null){
                stream.close();
            }
            } catch (IOException e) {
                e.printStackTrace(); //TODO
            }

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

}


