package com.engagepoint.team_a.cmis_manager;

import com.engagepoint.team_a.cmis_manager.exceptions.BaseException;
import com.engagepoint.team_a.cmis_manager.model.TypeDTO;
import com.engagepoint.team_a.cmis_manager.wrappers.TypeDefinitionWrapper;
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
    private StreamedContent file;
    private String type;
    private String[] types = {"xml", "json", "Tree XML", "Tree JSON"};
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

        if (type.equals("xml")) {
            try {
                stream = JsonXMLConvertor.createFileFromType(currentType, SupportedFileFormat.XML);
            } catch (BaseException e) {
                LOG.error(e.getMessage(), e);
                errorBean.setErrorMessage(e.getMessage());
                errorBean.setErrorVisibility("true");
            }
        } else if (type.equals("json")) {
            try {
                stream = JsonXMLConvertor.createFileFromType(currentType, SupportedFileFormat.JSON);
            } catch (BaseException e) {
                LOG.error(e.getMessage(), e);
                errorBean.setErrorMessage(e.getMessage());
                errorBean.setErrorVisibility("true");
            }
        } else if (type.equals("Tree XML")) {
            getTypeWithAllChildrenXML();

        } else if (type.equals("Tree JSON")) {
            getTypeWithAllChildrenJSON();
        }

        if (type.equals("json") || type.equals("xml")) {
            String saveName = currentType.getId() + "." + type;
            file = new DefaultStreamedContent(stream, "image/jpg", saveName);
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
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "\\types.zip";

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
        file = new DefaultStreamedContent(stream, "image/jpg", "types_in_xml.zip");
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
        String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("") + "\\types.zip";

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
        file = new DefaultStreamedContent(stream, "image/jpg", "types_in_json.zip");


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


