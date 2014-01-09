package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.common.service.ServiceEJBLocal;
import com.engagepoint.teama.cmismanager.common.service.ServiceEJBRemote;
import com.engagepoint.teama.cmismanager.common.util.FileStatusReport;
import com.engagepoint.teama.cmismanager.common.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.common.exceptions.ModificationException;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import com.engagepoint.teama.cmismanager.biz.wrappers.TypeDefinitionWrapper;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisPermissionDeniedException;
import org.apache.log4j.Logger;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class ServiceEJB implements ServiceEJBRemote, ServiceEJBLocal, Serializable {

    public static final Logger LOG = Logger.getLogger(ServiceEJB.class);
    @EJB
    ObjectTypeReader objectTypeReader;

    /**
     * This EJB handle clients sessions
     */

    @EJB
    SessionEJB sessionEJB;

    /**
     * This method allows us to bind current user with session. If two or more users use same repository in same tame
     * and have same username and password, SessionEJB create only one session and share it.
     * Use this method only once at start!
     *
     * @param username  username
     * @param password  password
     * @param url       url
     * @param sessionID every user must have unique session ID
     * @throws ConnectionException
     */
    @Override
    public void connect(String username, String password, String url, String sessionID, String repositoryName)
            throws ConnectionException {
        if (url == null || url.isEmpty()) {
            throw new IllegalArgumentException("url must be set");
        } else if (repositoryName == null || repositoryName.isEmpty()) {
            throw new IllegalArgumentException("repository ID must be set");
        }
        sessionEJB.createSession(username, password, url, sessionID, repositoryName);
    }

    /**
     * Create new type in repository.
     *
     * @param sessionID every user must have unique session ID
     * @param newType   some TypeDTO instance
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
            returnedTypeDTO = objectTypeReader.readIgnoreChildren(createdType);
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
     * Try to create many types at once.
     *
     * @param typeDTOList list of sorted TypeDTO instances
     * @param sessionID   every user must have unique session ID
     * @return list of instances FileStatusReport
     * @throws BaseException
     * @see ConvertorEJB
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
                LOG.error(ex.getMessage(), ex);
                fileStatusReportList.add(new FileStatusReport(type.getId(), "can not create"));
            }
        }
        return fileStatusReportList;
    }

    /**
     * Delete type in repository. If type have children, this method must delete them too..
     *
     * @param sessionID   every user must have unique session ID
     * @param deletedType some TypeDTO instance
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

    /**
     * This method allows us to close session, if no one use it.
     * Use this method only one at finish!
     *
     * @param sessionID every user must have unique session ID
     * @throws com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException
     *
     */
    @Override
    public void disconnect(String sessionID) throws ConnectionException {
        sessionEJB.closeSession(sessionID);
    }

    /**
     * Get all types from repository.
     *
     * @param sessionID every user must have unique session ID
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
                typeList.add(objectTypeReader.readTree(tree));
            }
            return typeList;
        } catch (CmisPermissionDeniedException cp) {
            throw new ConnectionException(cp.getMessage(), cp);
        } catch (CmisBaseException c) {
            throw new BaseException(c.getMessage(), c);
        }
    }

    /**
     * This method allows us to check does user can connect without creating session. Returns array of repositories ID,
     * if user with current username, password and url can create session with some repository.
     *
     * @param username username
     * @param password password
     * @param url      url
     * @return array of repositories
     * @throws com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException
     *
     */
    @Override
    public String[] getRepoList(String username, String password, String url)
            throws ConnectionException {
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

    /**
     * Update type in repository.
     *
     * @param sessionID   every user must have unique session ID
     * @param updatedType some TypeDTO instance
     * @return new instance if created, null if not
     * @throws ModificationException
     * @throws ConnectionException
     * @throws BaseException
     * @deprecated throws exception with 'not supported'
     */
    @Override
    @Deprecated
    public TypeDTO updateType(TypeDTO updatedType, String sessionID) throws BaseException {
        TypeDTO returnedTypeDTO;
        Session session = sessionEJB.getSession(sessionID);

        try {
            TypeDefinitionWrapper typeDefinitionWrapper = new TypeDefinitionWrapper(updatedType);
            ObjectType newType = session.updateType(typeDefinitionWrapper);
            returnedTypeDTO = objectTypeReader.readIgnoreChildren(newType);
        } catch (CmisPermissionDeniedException cp) {
            throw new ConnectionException(cp.getMessage(), cp);
        } catch (CmisBaseException cbe) {
            throw new BaseException(cbe.getMessage(), cbe);
        }

        return returnedTypeDTO;
    }

    /**
     * Util method, that allows delete types and their children.
     *
     * @param list    list of Tree<ObjectType>
     * @param session users session
     */
    private void deleteTree(List<Tree<ObjectType>> list, Session session) {
        for (Tree<ObjectType> objectTypeTree : list) {
            if (objectTypeTree.getChildren() != null && !objectTypeTree.getChildren().isEmpty()) {
                deleteTree(objectTypeTree.getChildren(), session);
            }
            session.deleteType(objectTypeTree.getItem().getId());
        }
    }

}