package com.engagepoint.teama.cmismanager;

import com.engagepoint.teama.cmismanager.exceptions.ConnectionException;
import org.apache.chemistry.opencmis.client.api.Repository;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Singleton
@LocalBean
public class ConnectionCMIS {
    Map<String, Repository> map = new HashMap<String, Repository>();

    public Session getSession() {
        return session;
    }

    Session session;

    public String[] getRepoList(String username, String password, String url) throws ConnectionException {

        map = new HashMap<String, Repository>();
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

        map.clear();
        List<String> array = new ArrayList<String>();

        for (Repository repo : list) {
            map.put(repo.getName(), repo);
            array.add(repo.getName());
        }

        return array.toArray(new String[0]);
    }

    public void connect(String repoName) throws ConnectionException {

        if(map.isEmpty()) {
            throw new ConnectionException("There are no available repository. Use 'getRepoList' at first");
        }

        session = map.get(repoName).createSession();

        if (session == null) {
            throw new ConnectionException("no session");
        }
    }
}
