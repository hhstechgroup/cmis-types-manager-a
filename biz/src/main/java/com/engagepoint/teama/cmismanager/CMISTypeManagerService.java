package com.engagepoint.teama.cmismanager;

import com.engagepoint.teama.cmismanager.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.exceptions.ModificationException;
import com.engagepoint.teama.cmismanager.model.TypeDTO;
import com.engagepoint.teama.cmismanager.wrappers.TypeDefinitionWrapper;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisPermissionDeniedException;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParseException;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
@LocalBean
public class CMISTypeManagerService {
    public static final Logger LOG = Logger.getLogger(CMISTypeManagerService.class);



    @EJB
    private ConnectionCMIS connection;
    public String[] getRepoList(String username, String password, String url) throws ConnectionException {
        return connection.getRepoList(username, password, url);
    }
    public void connect(String s) throws ConnectionException {
        connection.connect(s);
    }
    public Session getSession(){
        return connection.getSession();
    }



    public void disconnect() {
        connection.getSession().getBinding().close();
    }

    public String getSessionID() {
        return connection.getSession().toString();
    }

    /**
     * Get all types in repository.
     *
     * @return list of base types trees
     * @throws ConnectionException
     * @throws BaseException
     */
    public List<TypeDTO> getAllTypes() throws BaseException {
        try {
            List<TypeDTO> typeList = new ArrayList<TypeDTO>();
            List<Tree<ObjectType>> list = connection.getSession().getTypeDescendants(null, -1, true);
            for (Tree<ObjectType> tree : list) {
                typeList.add(ObjectTypeReader.readTree(tree));
            }
            return typeList;
        } catch (CmisPermissionDeniedException cp) {
            throw new ConnectionException(cp.getMessage(), cp);
        } catch (CmisBaseException c) {
            throw new BaseException(c.getMessage(), c);
        }
    }

    /**
     * Create type in repository.
     *
     * @param newType some TypeDTO instance
     * @return new instance if created, null if not
     * @throws ModificationException
     * @throws ConnectionException
     * @throws BaseException
     */
    public TypeDTO createType(TypeDTO newType) throws BaseException {
        TypeDTO returnedTypeDTO;
        try {
            TypeDefinitionWrapper typeDefinitionWrapper = new TypeDefinitionWrapper(newType);
            ObjectType createdType = connection.getSession().createType(typeDefinitionWrapper);
            returnedTypeDTO = ObjectTypeReader.readIgnoreChildren(createdType);
        } catch (CmisObjectNotFoundException ex) {
            throw new ModificationException(ex.getMessage(), ex);
        } catch (CmisInvalidArgumentException ex) {
            throw new ModificationException(ex.getMessage(), ex);
        } catch (CmisPermissionDeniedException ex) {
            throw new ConnectionException(ex.getMessage(), ex);
        } catch (CmisBaseException cbe) {
            throw new BaseException(cbe.getMessage(), cbe);
        }

        return returnedTypeDTO;
    }

    /**
     * Get type from repository.
     *
     * @param id some TypeDTO instance ID
     * @return new instance if created, null if not
     * @throws ModificationException
     * @throws ConnectionException
     * @throws BaseException
     */
    public ObjectType getTypeById(String id) throws BaseException {

        ObjectType returnedType;

        try {
            returnedType = connection.getSession().getTypeDefinition(id);
        } catch (CmisObjectNotFoundException cp) {
            throw new ModificationException(cp.getMessage(), cp);
        } catch (CmisPermissionDeniedException cp) {
            throw new ConnectionException(cp.getMessage(), cp);
        } catch (CmisBaseException cbe) {
            throw new BaseException(cbe.getMessage(), cbe);
        }

        return returnedType;
    }

    /**
     * Update type in repository.
     *
     * @param updatedType some TypeDTO instance
     * @return new instance if created, null if not
     * @throws ModificationException
     * @throws ConnectionException
     * @throws BaseException
     */
    public TypeDTO updateType(TypeDTO updatedType) throws BaseException {
        TypeDTO returnedTypeDTO;

        try {
            TypeDefinitionWrapper typeDefinitionWrapper = new TypeDefinitionWrapper(updatedType);
            ObjectType newType = connection.getSession().updateType(typeDefinitionWrapper);
            returnedTypeDTO = ObjectTypeReader.readIgnoreChildren(newType);
        } catch (CmisPermissionDeniedException cp) {
            throw new ConnectionException(cp.getMessage(), cp);
        } catch (CmisBaseException cbe) {
            throw new BaseException(cbe.getMessage(), cbe);
        }

        return returnedTypeDTO;
    }

    /**
     * Delete type in repository. If type have children, this method delete all children.
     *
     * @param deletedType
     * @throws ModificationException
     * @throws ConnectionException
     * @throws BaseException
     */
    public void deleteType(TypeDTO deletedType) throws BaseException {

        try {

            List<Tree<ObjectType>> list = connection.getSession().getTypeDescendants(deletedType.getId(), -1, false);

            if (list != null && !list.isEmpty()) {
                deleteTree(list);
            }

            connection.getSession().deleteType(deletedType.getId());

        } catch (CmisInvalidArgumentException cp) {
            throw new ModificationException(cp.getMessage(), cp);
        } catch (CmisPermissionDeniedException cp) {
            throw new ConnectionException(cp.getMessage(), cp);
        } catch (CmisBaseException c) {
            throw new BaseException(c.getMessage(), c);
        }


    }

    private void deleteTree(List<Tree<ObjectType>> list) {

        for (Tree<ObjectType> objectTypeTree : list) {

            if (objectTypeTree.getChildren() != null && !objectTypeTree.getChildren().isEmpty()) {
                deleteTree(objectTypeTree.getChildren());
            }
            connection.getSession().deleteType(objectTypeTree.getItem().getId());
        }

    }

    public TypeDTO getSecondaryTypes() throws BaseException {
        ObjectType baseSecondary = getTypeById("cmis:secondary");
        return ObjectTypeReader.readWithChildren(baseSecondary);
    }

    /**
     * Create
     */
    public void createMultiply() throws BaseException {
        if (query == null || query.isEmpty()) {
            throw new BaseException("No data");
        }
        HashMap<String, String> resultMap = new HashMap<String, String>();
        TypeDefinition returnedTypeDefinition;
        for (TypeDefinition type : query) {

            try {
                returnedTypeDefinition = connection.getSession().createType(type);
                if (returnedTypeDefinition != null) {
                    resultMap.put(returnedTypeDefinition.getDisplayName(), "in repo");
                } else {
                    resultMap.put(type.getDisplayName(), "can not create");
                }
            } catch (CmisBaseException ex) {
                LOG.error(ex.getMessage(), ex);
                resultMap.put(type.getDisplayName(), "can not create");
            }
        }
        query = null;
    }

    private List<TypeDefinition> query;

    public List<FileStatusReport> readAndValidate(Map<String, InputStream> streamHashMap) {
        ArrayList<FileStatusReport> fileStatusList = new ArrayList<FileStatusReport>();
        HashMap<String, TypeDefinition> okTypeMap = new HashMap<String, TypeDefinition>();

        for (String fileName : streamHashMap.keySet()) {
            InputStream stream = streamHashMap.get(fileName);
            if (fileName.endsWith(".xml")) {
                try {
                    TypeDefinition type = JsonXMLConvertor.createTypeFromXML(stream);
                    okTypeMap.put(fileName, type);
                } catch (XMLStreamException e) {
                    LOG.error(e.getMessage(), e);
                    fileStatusList.add(new FileStatusReport(fileName, e.getMessage()));
                }
            } else if (fileName.endsWith(".json")) {
                try {
                    TypeDefinition type = JsonXMLConvertor.createTypeFromJSON(stream);
                    okTypeMap.put(fileName, type);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                    fileStatusList.add(new FileStatusReport(fileName, e.getMessage()));
                } catch (JSONParseException e) {
                    LOG.error(e.getMessage(), e);
                    fileStatusList.add(new FileStatusReport(fileName, e.getMessage()));
                }
            } else {
                fileStatusList.add(new FileStatusReport(fileName, "Wrong file format."));
            }
        }
        query = DataSorter.validateAndSort(okTypeMap, fileStatusList);
        return fileStatusList;
    }

}