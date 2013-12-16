package com.engagepoint.team_a.cmis_manager;

import java.io.IOException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.StringTokenizer;

@ManagedBean
@RequestScoped
public class FileUploadController {
    private String msgLbl = "----";
    private String show="false";
    private String fileExtension;
    @ManagedProperty(value = "#{treeBean}")
    private TreeBean treeBean;
    public void setTreeBean(TreeBean treeBean) {
        this.treeBean = treeBean;
    }


    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getMsgLbl() {
        return msgLbl;
    }

    public void setMsgLbl(String msgLbl) {
        this.msgLbl = msgLbl;
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();
             show="true";
            String fileName = file.getFileName();
            String [] array = fileName.split("\\.");
            fileExtension = array[array.length - 1];
            InputStream generatedFileInputStream = file.getInputstream();
            JsonXMLConvertor convertor = new JsonXMLConvertor();
            if(fileExtension.equals("json")){
                convertor.createTypeFromJSON(generatedFileInputStream);
                msgLbl = "File successfully added!";
            } else if(fileExtension.equals("xml")) {
                convertor.createTypeFromXML(generatedFileInputStream);
                msgLbl = "File successfully added!";
            }else {
                msgLbl = "Wrong format, try again!";
            }

        } catch (IOException e) {
            msgLbl=e.getMessage();
        }
        treeBean.updateTree();
    }


}

