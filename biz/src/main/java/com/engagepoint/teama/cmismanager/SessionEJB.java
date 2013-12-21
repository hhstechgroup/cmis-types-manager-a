package com.engagepoint.teama.cmismanager;

import com.engagepoint.teama.cmismanager.exceptions.ConnectionException;

import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Singleton
@LocalBean
public class SessionEJB {

    private Map<String, RepositoryInfo> idToRepoMap = new ConcurrentHashMap<String, RepositoryInfo>();
    private Map<RepositoryInfo, Session> repoToSessionMap = new ConcurrentHashMap<RepositoryInfo, Session>();

    public Session getSession(String sessionID) throws ConnectionException {

        Session returnedSession;

        if (! idToRepoMap.containsKey(sessionID)) {
            throw new ConnectionException("No such session");
        } else {
            returnedSession = repoToSessionMap.get( idToRepoMap.get(sessionID) );
        }

        if (returnedSession == null) {
            //todo
        }

        return returnedSession;
    }

    public synchronized void createSession(String username, String password, String url, String sessionID, String repositoryName)
            throws ConnectionException {

        RepositoryInfo repositoryInfo = new RepositoryInfo(username, password, url, repositoryName);

        if (repoToSessionMap.containsKey(repositoryInfo)) {
            idToRepoMap.put(sessionID, repositoryInfo);
        } else {
            Session session = createNewSession(username, password, url, repositoryName);

            repoToSessionMap.put(repositoryInfo, session);
            idToRepoMap.put(sessionID, repositoryInfo);
        }
    }

    public synchronized void closeSession(String sessionID) throws ConnectionException {

        if (! idToRepoMap.containsKey(sessionID)) {
            throw new ConnectionException("No such session");
        } else {

            RepositoryInfo repositoryInfo = idToRepoMap.remove(sessionID);

            if (! idToRepoMap.containsValue(repositoryInfo)) {
                Session session = repoToSessionMap.remove(repositoryInfo);
                session.getBinding().close();
            }

        }

    }

    private Session createNewSession(String username, String password, String url, String repositoryName) throws ConnectionException{

        Session returnedSession;

        SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put(SessionParameter.USER, username);
        parameter.put(SessionParameter.PASSWORD, password);

        parameter.put(SessionParameter.ATOMPUB_URL, url + "/atom11");
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.REPOSITORY_ID, repositoryName);

        try {
            returnedSession = factory.createSession(parameter);

            if (returnedSession == null) {
                throw new ConnectionException("Can not create session");
            }

        } catch (CmisBaseException e) {
            throw new ConnectionException(e.getMessage(), e);
        }

        return returnedSession;
    }
}
