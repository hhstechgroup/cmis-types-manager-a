package com.engagepoint.teama.cmismanager.common.service;

import com.engagepoint.teama.cmismanager.common.util.ResultSet;
import com.engagepoint.teama.cmismanager.common.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;

import javax.ejb.Local;
import java.io.InputStream;
import java.util.Map;

@Local
public interface ConvertorEJBLocal {

    /**
     * This method allows to read dataStreams from map, then validate and sort them.
     * @param streamHashMap streamHashMap, where key is file name
     * @return ResultSet, that contains statusReportList (if convertation or validation fails) and sorted by ParentId TypeDTO instances.
     */
    ResultSet readAndValidate(Map<String, InputStream> streamHashMap);

    /**
     * Convert TypeDTO instance in stream, that contains TypeDefinition in XML.
     * @param typeDTO TypeDTO instance
     * @return stream
     * @throws BaseException
     */
    InputStream createXMLFromType(TypeDTO typeDTO)  throws BaseException;

    /**
     * Convert TypeDTO instance in stream, that contains TypeDefinition in JSON.
     * @param typeDTO TypeDTO instance
     * @return stream
     * @throws BaseException
     */
    InputStream createJSONFromType(TypeDTO typeDTO) throws BaseException;

    /**
     * Convert TypeDTO instance and its children in ZIP stream, that contains TypeDefinition list in XML.
     * @param typeDTO TypeDTO instance
     * @param path path
     * @return stream
     * @throws BaseException
     */
    InputStream createXMLFromTypeIncludeChildren(TypeDTO typeDTO, String path)  throws BaseException;

    /**
     * Convert TypeDTO instance and its children in ZIP stream, that contains TypeDefinition list in JSON.
     * @param typeDTO TypeDTO instance
     * @param path path
     * @return stream
     * @throws BaseException
     */
    InputStream createJSONFromTypeIncludeChildren(TypeDTO typeDTO, String path) throws BaseException;
}
