package com.engagepoint.teama.cmismanager;

import com.engagepoint.teama.cmismanager.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.model.TypeDTO;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.log4j.Logger;

import javax.ejb.Remote;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ivan.yakubenko
 * Date: 12/20/13
 * Time: 12:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Remote
public interface CMISTypeManagerServiceInterface {
    Logger LOG = Logger.getLogger(CMISTypeManagerService.class);

    String[] getRepoList(String username, String password, String url) throws ConnectionException;

    void connect(UserProperty userProperty) throws ConnectionException;

    Session getSession(UserProperty userProperty);

    void disconnect(UserProperty userProperty);

    String getSessionID(UserProperty userProperty);

    List<TypeDTO> getAllTypes(UserProperty userProperty) throws BaseException;

    TypeDTO createType(TypeDTO newType, UserProperty userProperty) throws BaseException;

    ObjectType getTypeById(String id, UserProperty userProperty) throws BaseException;

    TypeDTO updateType(TypeDTO updatedType, UserProperty userProperty) throws BaseException;

    void deleteType(TypeDTO deletedType, UserProperty userProperty) throws BaseException;

    TypeDTO getSecondaryTypes(UserProperty userProperty) throws BaseException;

    void createMultiply(UserProperty userProperty) throws BaseException;

    List<FileStatusReport> readAndValidate(Map<String, InputStream> streamHashMap);
}
