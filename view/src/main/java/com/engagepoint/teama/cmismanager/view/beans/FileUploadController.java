package com.engagepoint.teama.cmismanager.view.beans;

import com.engagepoint.teama.cmismanager.common.service.ConvertorEJBRemote;
import com.engagepoint.teama.cmismanager.common.service.ServiceEJBRemote;
import com.engagepoint.teama.cmismanager.common.util.FileStatusReport;
import com.engagepoint.teama.cmismanager.common.util.ResultSet;
import com.engagepoint.teama.cmismanager.common.exceptions.BaseException;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@ViewScoped
public class FileUploadController {

    public static final Logger LOG = Logger.getLogger(FileDownloadController.class);
    private String msgLbl = "";
    private String show = "false";
    @ManagedProperty(value = "#{treeBean}")
    private TreeBean treeBean;
    private Map<String, byte[]> fileMap = new HashMap<String, byte[]>();
    private List<FileStatusReport> fileStatus = new ArrayList<FileStatusReport>();
    @ManagedProperty(value = "#{error}")
    private ErrorBean errorBean;

    @EJB(lookup = "java:global/ear-1.0-SNAPSHOT/biz-1.0-SNAPSHOT/ServiceEJB!com.engagepoint.teama.cmismanager.common.service.ServiceEJBRemote")
    private ServiceEJBRemote service;

    @EJB(lookup = "java:global/ear-1.0-SNAPSHOT/biz-1.0-SNAPSHOT/ConvertorEJB!com.engagepoint.teama.cmismanager.common.service.ConvertorEJBRemote")
    private ConvertorEJBRemote convertor;

    private ResultSet validationResultSet;

    public List<FileStatusReport> getFileStatus() {
        return fileStatus;
    }

    public void setTreeBean(TreeBean treeBean) {
        this.treeBean = treeBean;
    }

    public ErrorBean getErrorBean() {
        return errorBean;
    }

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

        validationResultSet = convertor.readAndValidate(fileMap);

        try {
            service.createTypes(validationResultSet.getSortedTypeList(), treeBean.getSessionID());
        } catch (BaseException e) {
            LOG.error(e.getMessage(), e);
        }

        msgLbl = "Types created";
        treeBean.updateTree();
        fileMap.clear();
        fileStatus.clear();
    }

    public void handleFileUpload(FileUploadEvent event) {

        UploadedFile file = event.getFile();
        show = "true";
        String fileName = file.getFileName();
        msgLbl = file.getFileName() + " ";
        fileMap.put(fileName, file.getContents());
    }

    public void hideMsg() {
        show = "false";
    }
}

