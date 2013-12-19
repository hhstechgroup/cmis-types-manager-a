package com.engagepoint.teama.cmismanager;

import com.engagepoint.teama.cmismanager.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.model.TypeDTO;
import com.engagepoint.teama.cmismanager.wrappers.TypeDefinitionWrapper;
import org.apache.chemistry.opencmis.client.util.TypeUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    private List<TypeDTO> listOfChildren = null;

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
        InputStream stream = null;

        if (type.equals(XML)) {
            try {
                stream = JsonXMLConvertor.createFileFromType(currentType, SupportedFileFormat.XML);
            } catch (BaseException e) {
                LOG.error(e.getMessage(), e);
                errorBean.setErrorMessage(e.getMessage());
                errorBean.setErrorVisibility(TRUE);
            }
        } else if (type.equals(JSON)) {
            try {
                stream = JsonXMLConvertor.createFileFromType(currentType, SupportedFileFormat.JSON);
            } catch (BaseException e) {
                LOG.error(e.getMessage(), e);
                errorBean.setErrorMessage(e.getMessage());
                errorBean.setErrorVisibility(TRUE);
            }
        } else if (type.equals(XML_ALL)) {
            getTypeWithAllChildrenXML();

        } else if (type.equals(JSON_ALL)) {
            getTypeWithAllChildrenJSON();
        }

        if (type.equals(JSON) || type.equals(XML)) {
            String saveName = currentType.getId() + "." + type;
            file = new DefaultStreamedContent(stream, CONTENT_TYPE, saveName);
        }

        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    private void findAllTypeChildren(List<TreeNode> list) {
        for (int i = 0; i < list.size(); i++) {
            listOfChildren.add((TypeDTO) list.get(i).getData());
            if (list.get(i).getChildren() != null) {
                findAllTypeChildren(list.get(i).getChildren());
            } else {
                return;
            }

        }

    }

    public void getTypeWithAllChildrenXML() {
        listOfChildren = new ArrayList<TypeDTO>();
        TreeNode selected = treeBean.getSelected();
        listOfChildren.add((TypeDTO) selected.getData());
        ArrayList<TypeDefinitionWrapper> listOfDefinitions = new ArrayList<TypeDefinitionWrapper>();
        ZipOutputStream out = null;
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + TYPES_ZIP;

        if (listOfChildren != null) {
            findAllTypeChildren(selected.getChildren());
        }

        for (int i = 0; i < listOfChildren.size(); i++) {
            TypeDefinitionWrapper wrapper = new TypeDefinitionWrapper(listOfChildren.get(i));
            listOfDefinitions.add(wrapper);
        }

        try {
            out = new ZipOutputStream(new FileOutputStream(new File(path)));
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }

        for (int i = 0; i < listOfDefinitions.size(); i++) {
            OutputStream stream = new ByteArrayOutputStream();

            try {
                TypeUtils.writeToXML(listOfDefinitions.get(i), stream);
                if (out != null) {
                    out.putNextEntry(new ZipEntry(listOfDefinitions.get(i).getDisplayName() + ".xml"));
                    out.write(((ByteArrayOutputStream) stream).toByteArray());
                    out.closeEntry();
                }
            } catch (XMLStreamException e) {
                LOG.error(e.getMessage(), e);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
        file = new DefaultStreamedContent(stream, CONTENT_TYPE, "types_in_xml.zip");
    }

    public void getTypeWithAllChildrenJSON() {
        listOfChildren = new ArrayList<TypeDTO>();
        TreeNode selected = treeBean.getSelected();
        listOfChildren.add((TypeDTO) selected.getData());
        if (listOfChildren != null) {
            findAllTypeChildren(selected.getChildren());
        }
        ArrayList<TypeDefinitionWrapper> listOfDefinitions = new ArrayList<TypeDefinitionWrapper>();
        for (int i = 0; i < listOfChildren.size(); i++) {
            TypeDefinitionWrapper wrapper = new TypeDefinitionWrapper(listOfChildren.get(i));
            listOfDefinitions.add(wrapper);
        }

        ZipOutputStream out = null;
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + TYPES_ZIP;

        try {
            out = new ZipOutputStream(new FileOutputStream(new File(path)));
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }

        for (int i = 0; i < listOfDefinitions.size(); i++) {
            OutputStream stream = new ByteArrayOutputStream();
            try {
                TypeUtils.writeToJSON(listOfDefinitions.get(i), stream);
                out.putNextEntry(new ZipEntry(listOfDefinitions.get(i).getDisplayName() + ".json"));
                out.write(((ByteArrayOutputStream) stream).toByteArray());
                out.closeEntry();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
        }
        file = new DefaultStreamedContent(stream, CONTENT_TYPE, "types_in_json.zip");
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


