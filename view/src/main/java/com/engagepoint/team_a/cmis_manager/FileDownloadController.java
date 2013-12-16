package com.engagepoint.team_a.cmis_manager;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;

@ManagedBean
@SessionScoped
public class FileDownloadController {

    private StreamedContent file;
    private String type;
    private String[] types = {"xml", "json"};

    public void setTreeBean(TreeBean treeBean) {
        this.treeBean = treeBean;
    }

    @ManagedProperty(value = "#{treeBean}")

    private TreeBean treeBean;

    public FileDownloadController() {
    }

    public String[] getTypes() {
        return types;
    }
    public void setDownloadedFile(){
        JsonXMLConvertor convertor = new JsonXMLConvertor();
        String currentTypeName = treeBean.getCurrentDTO().getId();
        InputStream stream = null;
        if(type.equals("xml")){
            stream = convertor.createXMLFromType(currentTypeName);
        } else if(type.equals("json")){
            stream = convertor.createJSONFromType(currentTypeName);
        }
        String saveName = currentTypeName + "." + type;
        file = new DefaultStreamedContent(stream, "image/jpg", saveName);
        try {
            if(stream != null){
                stream.close();
            }
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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


