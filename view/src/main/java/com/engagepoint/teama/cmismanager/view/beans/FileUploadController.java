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
import java.io.IOException;
import java.io.InputStream;
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
    private Map<String, InputStream> fileMap = new HashMap<String, InputStream>();
    private List<FileStatusReport> fileStatus = new ArrayList<FileStatusReport>();
    @ManagedProperty(value = "#{error}")
    private ErrorBean errorBean;

    @EJB(beanInterface = ServiceEJBRemote.class, name="java:global/MultiMVNEAR/biz/com.engagepoint.teama.cmismanager.biz.ejb.ServiceEJB")
    private ServiceEJBRemote service;

    @EJB(beanInterface = ConvertorEJBRemote.class, name="java:global/MultiMVNEAR/biz/com.engagepoint.teama.cmismanager.biz.ejb.ConvertorEJB")
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

