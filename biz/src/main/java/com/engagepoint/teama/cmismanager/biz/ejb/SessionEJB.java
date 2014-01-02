package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.biz.util.RepositoryInfo;
import com.engagepoint.teama.cmismanager.common.exceptions.ConnectionException;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.log4j.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Singleton
@LocalBean
public class SessionEJB implements Serializable {

    /**
     * This map binds unique userID with repository. Different application users can use the same repository
     * (same username, password, url and repositoryName), and they will have one session for all.
     */

    private Map<String, RepositoryInfo> idToRepoMap = new ConcurrentHashMap<String, RepositoryInfo>();

    private SessionFactory sessionFactory;
    /**
     * This map contains session pool.
     */
    private Map<RepositoryInfo, Session> repoToSessionMap = new ConcurrentHashMap<RepositoryInfo, Session>();

    public static final Logger LOG = Logger.getLogger(SessionEJB.class);

    public SessionEJB(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionEJB() {
        this.sessionFactory = SessionFactoryImpl.newInstance();
    }

    /**
     * Remove sessionID from idToRepoMap. If no one use this session at this moment, this method will close it.
     * @param sessionID every user must have unique session ID
     * @throws ConnectionException
     */
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

    /**
     * Bind users sessionID with some session. If session does not exist, create new session. If session exist,
     * bind sessionID with this session.
     * @param username username
     * @param password password
     * @param url url
     * @param sessionID every user must have unique session ID
     * @param repositoryName repository ID
     * @throws ConnectionException
     */
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

    /**
     * Get session from pool by users sessionID
     * @param sessionID every user must have unique session ID
     * @return session
     * @throws ConnectionException
     */
    public Session getSession(String sessionID) throws ConnectionException {

        Session returnedSession;

        if (! idToRepoMap.containsKey(sessionID)) {
            throw new ConnectionException("No such session");
        } else {
            returnedSession = repoToSessionMap.get( idToRepoMap.get(sessionID) );
        }

        if (returnedSession == null) {
            throw new ConnectionException("Session was close");
        }

        return returnedSession;
    }

    /**
     * Create new session if this necessary.
     * @param username username
     * @param password password
     * @param url url
     * @param repositoryName repository ID
     * @throws ConnectionException
     */
    private Session createNewSession(String username, String password, String url, String repositoryName) throws ConnectionException{

        Session returnedSession;

        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put(SessionParameter.USER, username);
        parameter.put(SessionParameter.PASSWORD, password);
        parameter.put(SessionParameter.ATOMPUB_URL, url + "/atom11");
        parameter.put(SessionParameter.BINDING_TYPE, BindingType.ATOMPUB.value());
        parameter.put(SessionParameter.REPOSITORY_ID, repositoryName);

        try {
            returnedSession = sessionFactory.createSession(parameter);

            if (returnedSession == null) {
                throw new ConnectionException("Can not create session");
            }

        } catch (CmisBaseException e) {
            LOG.error(e.getMessage(), e);
            throw new ConnectionException(e.getMessage(), e);
        }

        return returnedSession;
    }


}
