package com.engagepoint.teama.cmismanager;

import com.engagepoint.teama.cmismanager.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.exceptions.ModificationException;
import com.engagepoint.teama.cmismanager.model.TypeDTO;
import com.engagepoint.teama.cmismanager.service.ServiceEJBLocal;
import com.engagepoint.teama.cmismanager.service.ServiceEJBRemove;
import com.engagepoint.teama.cmismanager.util.ObjectTypeReader;
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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ServiceEJB implements ServiceEJBRemove, ServiceEJBLocal {

    @EJB
    private SessionEJB sessionEJB;

    /**
     *
     * @param username
     * @param password
     * @param url
     * @return array of repositories
     * @throws com.engagepoint.teama.cmismanager.exceptions.ConnectionException
     */
    @Override
    public String[] getRepoList(String username, String password, String url) throws ConnectionException {
        SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put(SessionParameter.USER, username);
        parameter.put(SessionParameter.PASSWORD, password);

        parameter.put(SessionParameter.ATOMPUB_URL, url + "/atom11");
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        List<Repository> list;

        try {
            list = factory.getRepositories(parameter);
        } catch (CmisBaseException e) {
            throw new ConnectionException(e.getMessage(), e);
        }

        if (list.isEmpty()) {
            throw new ConnectionException("no connection");
        }

        List<String> array = new ArrayList<String>();

        for (Repository repo : list) {
            array.add(repo.getId());
        }

        return array.toArray(new String[1]);
    }


    @Override
    public void connect(String username, String password, String url, String sessionID, String repositoryName) throws ConnectionException {

        if (url == null || url.isEmpty() || repositoryName == null || repositoryName.isEmpty()) {
            throw new NullPointerException();
        } else {
            sessionEJB.createSession(username, password, url, sessionID, repositoryName);
        }
    }

    @Override
    public void disconnect(String sessionID) throws ConnectionException {
        sessionEJB.closeSession(sessionID);
    }

    /**
     * Get all types in repository.
     *
     * @return list of base types trees
     * @throws ConnectionException
     * @throws BaseException
     */
    @Override
    public List<TypeDTO> getAllTypes(String sessionID) throws BaseException {

        Session session = sessionEJB.getSession(sessionID);

        try {
            List<TypeDTO> typeList = new ArrayList<TypeDTO>();
            List<Tree<ObjectType>> list = session.getTypeDescendants(null, -1, true);
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
    @Override
    public TypeDTO createType(TypeDTO newType, String sessionID) throws BaseException {
        TypeDTO returnedTypeDTO;
        Session session = sessionEJB.getSession(sessionID);

        try {
            TypeDefinitionWrapper typeDefinitionWrapper = new TypeDefinitionWrapper(newType);
            ObjectType createdType = session.createType(typeDefinitionWrapper);
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
    private ObjectType getTypeById(String id, String sessionID) throws BaseException {

        ObjectType returnedType;
        Session session = sessionEJB.getSession(sessionID);

        try {
            returnedType = session.getTypeDefinition(id);
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
    @Override
    public TypeDTO updateType(TypeDTO updatedType, String sessionID) throws BaseException {
        TypeDTO returnedTypeDTO;
        Session session = sessionEJB.getSession(sessionID);

        try {
            TypeDefinitionWrapper typeDefinitionWrapper = new TypeDefinitionWrapper(updatedType);
            ObjectType newType = session.updateType(typeDefinitionWrapper);
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
    @Override
    public void deleteType(TypeDTO deletedType, String sessionID) throws BaseException {

        Session session = sessionEJB.getSession(sessionID);

        try {

            List<Tree<ObjectType>> list = session.getTypeDescendants(deletedType.getId(), -1, false);

            if (list != null && !list.isEmpty()) {
                deleteTree(list, session);
            }

            session.deleteType(deletedType.getId());

        } catch (CmisInvalidArgumentException cp) {
            throw new ModificationException(cp.getMessage(), cp);
        } catch (CmisPermissionDeniedException cp) {
            throw new ConnectionException(cp.getMessage(), cp);
        } catch (CmisBaseException c) {
            throw new BaseException(c.getMessage(), c);
        }


    }

    private void deleteTree(List<Tree<ObjectType>> list, Session session) {

        for (Tree<ObjectType> objectTypeTree : list) {

            if (objectTypeTree.getChildren() != null && !objectTypeTree.getChildren().isEmpty()) {
                deleteTree(objectTypeTree.getChildren(), session);
            }
            session.deleteType(objectTypeTree.getItem().getId());
        }

    }

    /**
     * Create
     */
    @Override
    public List<FileStatusReport> createTypes(List<TypeDTO> typeDTOList, String sessionID) throws BaseException {

        if (typeDTOList == null) {
            throw new BaseException("No data");
        }

        Session session = sessionEJB.getSession(sessionID);
        List<FileStatusReport> fileStatusReportList = new ArrayList<FileStatusReport>();

        TypeDefinition returnedTypeDefinition;

        for (TypeDTO type : typeDTOList) {

            try {
                returnedTypeDefinition = session.createType(new TypeDefinitionWrapper(type));
                if (returnedTypeDefinition != null) {
                    fileStatusReportList.add(new FileStatusReport(type.getId(), "in repo"));
                } else {
                    fileStatusReportList.add(new FileStatusReport(type.getId(), "can not create"));
                }
            } catch (CmisBaseException ex) {
                fileStatusReportList.add(new FileStatusReport(type.getId(), "can not create"));
            }
        }

        return fileStatusReportList;
    }

}