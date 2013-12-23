package com.engagepoint.teama.cmismanager.view.beans;

import com.engagepoint.teama.cmismanager.common.service.ConvertorEJBRemove;
import com.engagepoint.teama.cmismanager.common.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import java.io.InputStream;
import java.io.IOException;

import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class FileDownloadController {

    public static final Logger LOG = Logger.getLogger(FileDownloadController.class);
    public static final String XML = "xml";
    public static final String JSON = "json";
    public static final String XML_ALL = "Tree XML";
    public static final String JSON_ALL = "Tree JSON";
    public static final String CONTENT_TYPE = "image/jpg";
    public static final String TYPES_ZIP = "\\types.zip";
    public static final String TRUE = "true";

    private StreamedContent file;
    private String type;
    private String[] types = {XML, JSON, XML_ALL, JSON_ALL};
    @ManagedProperty(value = "#{treeBean}")
    private TreeBean treeBean;
    @ManagedProperty(value = "#{error}")
    private ErrorBean errorBean;

    @EJB(beanInterface = ConvertorEJBRemove.class, name="java:global/MultiMVNEAR/biz/com.engagepoint.teama.cmismanager.biz.ejb.ConvertorEJB")
    private ConvertorEJBRemove convertor;

    private InputStream downloadedFileInputStream;

    public FileDownloadController() {
    }

    public void setErrorBean(ErrorBean errorBean) {
        this.errorBean = errorBean;
    }

    public void setTreeBean(TreeBean treeBean) {
        this.treeBean = treeBean;
    }

    public String[] getTypes() {
        return types;
    }

    public void setTypes(String[] types) {
        String[] tmp = types;
        this.types = tmp;
    }

    public void setDownloadedFile() {

        TypeDTO currentType = treeBean.getCurrentDTO();

        try {
            if (downloadedFileInputStream != null) {
                downloadedFileInputStream.close();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        try {

            if (type.equals(XML)) {
               downloadedFileInputStream = convertor.createXMLFromType(currentType);
            } else if (type.equals(JSON)) {
               downloadedFileInputStream = convertor.createJSONFromType(currentType);
            } else {
                String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + TYPES_ZIP;

                if (type.equals(XML_ALL)) {
                    downloadedFileInputStream = convertor.createXMLFromTypeIncludeChildren(currentType, path);
                } else if (type.equals(JSON_ALL)) {
                    downloadedFileInputStream = convertor.createJSONFromTypeIncludeChildren(currentType, path);
                }
            }

            StringBuilder savedName = new StringBuilder(currentType.getId());

            if (type.equals(JSON) || type.equals(XML)) {
                savedName.append(".");
                savedName.append(type);
            } else {

                if( type.equals(JSON_ALL) ) {
                    savedName.append("_json");
                } else if ( type.equals(XML_ALL) ) {
                    savedName.append("_xml");
                }
                savedName.append(".");
                savedName.append("zip");
            }

            file = new DefaultStreamedContent(downloadedFileInputStream, CONTENT_TYPE, savedName.toString());

        } catch (BaseException e) {
            LOG.error(e.getMessage(), e);
            errorBean.setErrorMessage(e.getMessage());
            errorBean.setErrorVisibility(TRUE);
        }
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

    @PreDestroy
    public void closeDownloadedFileInputStream() {
        try {
            if (downloadedFileInputStream != null) {
                downloadedFileInputStream.close();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}


