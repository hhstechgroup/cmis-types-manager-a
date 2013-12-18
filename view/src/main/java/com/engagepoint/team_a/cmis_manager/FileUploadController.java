package com.engagepoint.team_a.cmis_manager;

import java.io.IOException;

import com.engagepoint.team_a.cmis_manager.exceptions.BaseException;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParseException;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.*;
import java.util.*;

@ManagedBean
@ViewScoped
public class FileUploadController {

    private String msgLbl = "";
    private String show="false";
    private String fileExtension;
    @ManagedProperty(value = "#{treeBean}")
    private TreeBean treeBean;
    private HashMap<String, InputStream> fileMap = new HashMap<String, InputStream>();
    private List<FileStatusReport> fileStatus = new ArrayList<FileStatusReport>();
    public static final Logger LOG = Logger.getLogger(FileDownloadController.class);

    public List<FileStatusReport> getFileStatus() {
        return fileStatus;
    }

    public void setTreeBean(TreeBean treeBean) {
        this.treeBean = treeBean;
    }


    @ManagedProperty(value = "#{error}")
    private ErrorBean errorBean;

    public void setErrorBean(ErrorBean errorBean) {
        this.errorBean = errorBean;
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

    public void processFiles() {
        fileStatus = CMISTypeManagerService.getInstance().readAndValidate(fileMap);

        try {
            CMISTypeManagerService.getInstance().createMultiply();
        } catch (BaseException e) {
            LOG.error(e.getMessage(), e);
        }

        msgLbl="Types created";
        treeBean.updateTree();
        fileMap.clear();
        fileStatus.clear();
    }


    public void handleFileUpload(FileUploadEvent event) {

            UploadedFile file = event.getFile();
             show="true";
            String fileName = file.getFileName();
            msgLbl=file.getFileName()+" ";
            InputStream generatedFileInputStream = null;
            try {
                generatedFileInputStream = file.getInputstream();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            fileMap.put(fileName, generatedFileInputStream);
    }

    public void hideMsg() {
        show = "false";
    }
}

