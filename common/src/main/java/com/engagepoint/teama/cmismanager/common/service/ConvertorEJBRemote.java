package com.engagepoint.teama.cmismanager.common.service;

import com.engagepoint.teama.cmismanager.common.util.ResultSet;
import com.engagepoint.teama.cmismanager.common.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;

import java.io.Serializable;
import java.util.Map;
import javax.ejb.Remote;

@Remote
public interface ConvertorEJBRemote extends Serializable {

    /**
     * This method allows to read dataStreams from map, then validate and sort them.
     * @param streamHashMap streamHashMap, where key is file name
     * @return ResultSet, that contains statusReportList (if convertation or validation fails) and sorted by ParentId TypeDTO instances.
     */
    ResultSet readAndValidate(Map<String, byte[]> streamHashMap);

    /**
     * Convert TypeDTO instance in stream, that contains TypeDefinition in XML.
     * @param typeDTO TypeDTO instance
     * @return stream
     * @throws BaseException
     */
    byte[] createXMLFromType(TypeDTO typeDTO)  throws BaseException;

    /**
     * Convert TypeDTO instance in stream, that contains TypeDefinition in JSON.
     *
     * @param typeDTO TypeDTO instance
     * @return stream
     * @throws BaseException
     */
    byte[] createJSONFromType(TypeDTO typeDTO) throws BaseException;

    /**
     * Convert TypeDTO instance and its children in ZIP stream, that contains TypeDefinition list in XML.
     *
     *
     *
     * @param typeDTO TypeDTO instance
     * @return stream
     * @throws BaseException
     */
    Map<String, byte[]> createXMLFromTypeIncludeChildren(TypeDTO typeDTO)  throws BaseException;

    /**
     * Convert TypeDTO instance and its children in ZIP stream, that contains TypeDefinition list in JSON.
     *
     * @param typeDTO TypeDTO instance
     * @return stream
     * @throws BaseException
     */
    Map<String, byte[]> createJSONFromTypeIncludeChildren(TypeDTO typeDTO) throws BaseException;
}
