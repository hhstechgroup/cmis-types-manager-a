package com.engagepoint.team_a.cmis_manager;

import com.engagepoint.team_a.cmis_manager.exceptions.TMBaseException;
import com.engagepoint.team_a.cmis_manager.model.TypeDTO;
import com.engagepoint.team_a.cmis_manager.wrappers.TypeDefinitionWrapper;

import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CMISTypeManagerService {
    private static CMISTypeManagerService cmisTypeManagerService;
    private Session session;

    private String url = "localhost";
    private String port = "8080";
    private String name = "";
    private String pass = "";

    private Map<String, Repository> map = new HashMap<String, Repository>();

    private String repo = "/chemistry-opencmis-server-inmemory-0.10.0";     //if not inmemory - change to "/cmis"
    private String repoID = "A1";                                           //may change to

    private CMISTypeManagerService() {
    }

    public static synchronized CMISTypeManagerService getInstance() {
        if (cmisTypeManagerService == null) {
            cmisTypeManagerService = new CMISTypeManagerService();
        }
        return cmisTypeManagerService;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String[] getRepoList(String url, String port) throws Exception {
        this.url = url;
        this.port = port;

        SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put(SessionParameter.USER, name);
        parameter.put(SessionParameter.PASSWORD, pass);

        parameter.put(SessionParameter.ATOMPUB_URL, "http://" + url + ":" + port + repo + "/atom11");
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        //parameter.put(SessionParameter.REPOSITORY_ID, "A1");

        List<Repository> list = factory.getRepositories(parameter);

        if (list.isEmpty()) {
            throw new Exception("no such session");
        }

        map.clear();
        List<String> array = new ArrayList();

        for (Repository repo : list) {
            map.put(repo.getName(), repo);
            array.add(repo.getName());
        }

        return array.toArray(new String[0]);
    }

    public void connect(String repoName) throws Exception {
        session = map.get(repoName).createSession();
        if (session == null) {
            throw new Exception("no such session");
        }
    }

    public void disconnect() {
        session.getBinding().close();
    }

    public Session getSession() {
        return session;
    }

    public List<TypeDTO> getTypes() {
        try {
            List<TypeDTO> typeList = new ArrayList<TypeDTO>();
            List<Tree<ObjectType>> list = session.getTypeDescendants(null, -1, true);
            for (Tree<ObjectType> tree : list) {
                typeList.add(ObjectTypeReader.readTree(tree));
            }
            return typeList;
        } catch (CmisBaseException c) {
            throw new TMBaseException(c.getMessage());
        }
    }

    /**
     * TODO
     * This method should throw exceptions
     *
     * @param newType some com.engagepoint.team_a.cmis_manager.model.TypeDTO instance, that not exist in CMIS repository
     * @return new instance if created, null if not
     * @throws Exception must throw exceptions
     */
    public TypeDTO createType(TypeDTO newType) {
        try {
            TypeDTO returnedTypeDTO = null;
            TypeDefinitionWrapper typeDefinitionWrapper = new TypeDefinitionWrapper(newType);
            ObjectType createdType = session.createType(typeDefinitionWrapper);
            returnedTypeDTO = ObjectTypeReader.readIgnoreChildren(createdType);
            return returnedTypeDTO;
        } catch (CmisBaseException cbe) {
            throw new TMBaseException(cbe.getMessage());
        }
    }

    public ObjectType getTypeById(String id) {
        try {
            ObjectType returnedType = null;
            returnedType = session.getTypeDefinition(id);
            return returnedType;
        } catch (CmisBaseException cbe) {
            throw new TMBaseException(cbe.getMessage());
        }
    }

    public TypeDTO updateType(TypeDTO updatedType) {
        try {
            TypeDTO returnedTypeDTO = null;
            TypeDefinitionWrapper typeDefinitionWrapper = new TypeDefinitionWrapper(updatedType);
            ObjectType newType = session.updateType(typeDefinitionWrapper);
            returnedTypeDTO = ObjectTypeReader.readIgnoreChildren(newType);
            return returnedTypeDTO;
        } catch (CmisBaseException cbe) {
            throw new TMBaseException(cbe.getMessage());
        }
    }

    public void deleteType(TypeDTO deletedType) {
        try {
            session.deleteType(deletedType.getId());
        } catch (CmisBaseException c) {
            throw new TMBaseException(c.getMessage());
        }
    }

    public TypeDTO getSecondaryTypes() {
        try {
            ObjectType baseSecondary = getTypeById("cmis:secondary");
            TypeDTO returnedDTO = ObjectTypeReader.readWithChildren(baseSecondary);
            return returnedDTO;
        } catch (CmisBaseException c) {
            throw new TMBaseException(c.getMessage());
        }
    }

}