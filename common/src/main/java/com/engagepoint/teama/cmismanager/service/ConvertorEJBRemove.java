package com.engagepoint.teama.cmismanager.service;

import com.engagepoint.teama.cmismanager.ResultSet;
import com.engagepoint.teama.cmismanager.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.model.TypeDTO;

import java.io.InputStream;
import java.util.Map;

public interface ConvertorEJBRemove {

    ResultSet readAndValidate(Map<String, InputStream> streamHashMap);

    InputStream createXMLFromType(TypeDTO typeDTO)  throws BaseException;

    InputStream createJSONFromType(TypeDTO typeDTO) throws BaseException;

    InputStream createXMLFromTypeIncludeChildren(TypeDTO typeDTO, String path)  throws BaseException;

    InputStream createJSONFromTypeIncludeChildren(TypeDTO typeDTO, String path) throws BaseException;

}
