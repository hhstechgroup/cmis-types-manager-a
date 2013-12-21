package com.engagepoint.teama.cmismanager.service;

import com.engagepoint.teama.cmismanager.FileStatusReport;
import com.engagepoint.teama.cmismanager.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.exceptions.ConnectionException;
import com.engagepoint.teama.cmismanager.model.TypeDTO;

import javax.ejb.Remote;
import java.util.List;

@Remote
public interface ServiceEJBRemove {

    String[] getRepoList(String username, String password, String url) throws ConnectionException;

    void connect(String username, String password, String url, String sessionID, String repositoryName) throws ConnectionException;

    void disconnect(String sessionID) throws ConnectionException;

    List<TypeDTO> getAllTypes(String sessionID) throws BaseException;

    TypeDTO createType(TypeDTO newType, String sessionID) throws BaseException;

    @Deprecated
    TypeDTO updateType(TypeDTO updatedType, String sessionID) throws BaseException;

    void deleteType(TypeDTO deletedType, String sessionID) throws BaseException;

    List<FileStatusReport> createTypes(List<TypeDTO> typeDTOList, String sessionID) throws BaseException;
}
