package com.engagepoint.teama.cmismanager.service;

import com.engagepoint.teama.cmismanager.FileStatusReport;
import com.engagepoint.teama.cmismanager.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.model.TypeDTO;

import javax.ejb.Local;
import java.util.List;

@Local
public interface ServiceEJBLocal {

    /**
     * This method allows us to check does user can connect without creating session. Returns array of repositories ID,
     * if user with current username, password and url can create session with some repository.
     * @param username username
     * @param password password
     * @param url url
     * @return array of repositories
     * @throws com.engagepoint.teama.cmismanager.exceptions.ConnectionException
     */
    String[] getRepoList(String username, String password, String url) throws ConnectionException;

    /**
     * This method allows us to bind current user with session. If two or more users use same repository in same tame
     * and have same username and password, SessionEJB create only one session and share it.
     * Use this method only once at start!
     * @param username username
     * @param password password
     * @param url url
     * @param sessionID every user must have unique session ID
     * @throws ConnectionException
     */
    void connect(String username, String password, String url, String sessionID, String repositoryName) throws ConnectionException;

    /**
     * This method allows us to close session, if no one use it.
     * Use this method only one at finish!
     * @param sessionID every user must have unique session ID
     * @throws com.engagepoint.teama.cmismanager.exceptions.ConnectionException
     */
    void disconnect(String sessionID) throws ConnectionException;

    /**
     * Get all types from repository.
     * @param sessionID every user must have unique session ID
     * @return list of base types trees
     * @throws ConnectionException
     * @throws BaseException
     */
    List<TypeDTO> getAllTypes(String sessionID) throws BaseException;

    /**
     * Create new type in repository.
     * @param sessionID every user must have unique session ID
     * @param newType some TypeDTO instance
     * @return new instance if created, null if not
     * @throws com.engagepoint.teama.cmismanager.exceptions.ModificationException
     * @throws ConnectionException
     * @throws BaseException
     */
    TypeDTO createType(TypeDTO newType, String sessionID) throws BaseException;

    /**
     * Update type in repository.
     * @param sessionID every user must have unique session ID
     * @param updatedType some TypeDTO instance
     * @return new instance if created, null if not
     * @throws com.engagepoint.teama.cmismanager.exceptions.ModificationException
     * @throws ConnectionException
     * @throws BaseException
     */
    @Deprecated
    TypeDTO updateType(TypeDTO updatedType, String sessionID) throws BaseException;

    /**
     * Delete type in repository. If type have children, this method must delete them too..
     * @param sessionID every user must have unique session ID
     * @param deletedType some TypeDTO instance
     * @throws com.engagepoint.teama.cmismanager.exceptions.ModificationException
     * @throws ConnectionException
     * @throws BaseException
     */
    void deleteType(TypeDTO deletedType, String sessionID) throws BaseException;

    /**
     * Try to create many types at once.
     * @param typeDTOList list of sorted TypeDTO instances
     * @param sessionID every user must have unique session ID
     * @throws BaseException
     * @return list of instances FileStatusReport
     */
    List<FileStatusReport> createTypes(List<TypeDTO> typeDTOList, String sessionID) throws BaseException;

}
