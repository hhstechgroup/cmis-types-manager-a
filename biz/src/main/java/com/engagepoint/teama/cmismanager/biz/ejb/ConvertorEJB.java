package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.common.exceptions.ConvertationException;
import com.engagepoint.teama.cmismanager.common.service.ConvertorEJBRemote;
import com.engagepoint.teama.cmismanager.common.util.FileStatusReport;
import com.engagepoint.teama.cmismanager.common.util.ResultSet;
import com.engagepoint.teama.cmismanager.common.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.common.model.TypeDTO;
import com.engagepoint.teama.cmismanager.common.service.ConvertorEJBLocal;
import com.engagepoint.teama.cmismanager.biz.util.DataSorter;
import com.engagepoint.teama.cmismanager.biz.util.JsonXMLConvertor;
import com.engagepoint.teama.cmismanager.biz.util.ObjectTypeReader;
import com.engagepoint.teama.cmismanager.biz.wrappers.TypeDefinitionWrapper;

import org.apache.chemistry.opencmis.client.util.TypeUtils;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;

import java.io.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import javax.ejb.Stateless;

@Stateless
public class ConvertorEJB implements ConvertorEJBRemote, ConvertorEJBLocal, Serializable {

    public static final Logger LOG = Logger.getLogger(ConvertorEJB.class);

    /**
     * Convert TypeDTO instance in stream, that contains TypeDefinition in JSON.
     *
     *
     * @param typeDTO TypeDTO instance
     * @return stream
     * @throws BaseException
     */
    @Override
    public byte[] createJSONFromType(TypeDTO typeDTO) throws BaseException {

        TypeDefinition typeDefinition = new TypeDefinitionWrapper(typeDTO);
        byte [] returnedByteArray;

        try {
            returnedByteArray = JsonXMLConvertor.getJSONFromTypeInByteArray(typeDefinition);
        } catch (ConvertationException e) {
            LOG.error(e.getMessage(), e);
            throw new BaseException(e.getMessage(), e);
        }

        return returnedByteArray;
    }

    /**
     * Convert TypeDTO instance and its children in ZIP stream, that contains TypeDefinition list in JSON.
     * @param parentTypeDTO TypeDTO instance
     * @return stream
     * @throws BaseException
     */
    @Override
    public Map<String, byte[]> createJSONFromTypeIncludeChildren(TypeDTO parentTypeDTO) throws BaseException {

        List<TypeDTO> typeDTOList = new ArrayList<TypeDTO>();
        typeDTOList.add(parentTypeDTO);

        if (!parentTypeDTO.getChildren().isEmpty()) {
            typeDTOList.addAll( getTypeChildren( parentTypeDTO.getChildren() ) );
        }

        Map<String, byte[]> map = new HashMap<String, byte[]>();

        for (TypeDTO typeDTO : typeDTOList) {
            try {
                byte [] b = JsonXMLConvertor.getXMLFromTypeInByteArray(new TypeDefinitionWrapper(typeDTO));
                map.put(typeDTO.getDisplayName() + ".json", b);
            } catch (ConvertationException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return map;
    }

    /**
     * Convert TypeDTO instance in stream, that contains TypeDefinition in XML.
     * @param typeDTO TypeDTO instance
     * @return stream
     * @throws BaseException
     */
    @Override
    public byte[] createXMLFromType(TypeDTO typeDTO) throws BaseException {

        TypeDefinition typeDefinition = new TypeDefinitionWrapper(typeDTO);
        byte[] inputStream;

        try {
            inputStream = JsonXMLConvertor.getXMLFromTypeInByteArray(typeDefinition);
        } catch (ConvertationException e) {
            LOG.error(e.getMessage(), e);
            throw new BaseException(e.getMessage(), e);
        }
        return inputStream;
    }

    /**
     * Convert TypeDTO instance and its children in ZIP stream, that contains TypeDefinition list in XML.
     * @param parentTypeDTO TypeDTO instance
     * @return stream
     * @throws BaseException
     */
    @Override
    public Map<String, byte[]> createXMLFromTypeIncludeChildren(TypeDTO parentTypeDTO) throws BaseException {

        List<TypeDTO> typeDTOList = new ArrayList<TypeDTO>();
        typeDTOList.add(parentTypeDTO);

        if (!parentTypeDTO.getChildren().isEmpty()) {
            typeDTOList.addAll( getTypeChildren( parentTypeDTO.getChildren() ) );
        }

        Map<String, byte[]> map = new HashMap<String, byte[]>();

        for (TypeDTO typeDTO : typeDTOList) {
            try {
                byte [] b = JsonXMLConvertor.getXMLFromTypeInByteArray(new TypeDefinitionWrapper(typeDTO));
                map.put(typeDTO.getDisplayName() + ".xml", b);
            } catch (ConvertationException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return map;
    }

    /**
     * This method allows to read dataStreams from map, then validate and sort them.
     * @param streamHashMap streamHashMap, where key is file name
     * @return ResultSet, that contains statusReportList (if convertation or validation fails) and sorted by ParentId TypeDTO instances.
     */
    @Override
    public ResultSet readAndValidate(Map<String, InputStream> streamHashMap) {

        ArrayList<FileStatusReport> fileStatusList = new ArrayList<FileStatusReport>();

        HashMap<String, TypeDefinition> okTypeMap = new HashMap<String, TypeDefinition>();

        for (String fileName : streamHashMap.keySet()) {
            InputStream stream = streamHashMap.get(fileName);
            if (fileName.endsWith(".xml")) {
                try {
                    TypeDefinition type = JsonXMLConvertor.createTypeFromXML(stream);
                    okTypeMap.put(fileName, type);
                } catch (ConvertationException e) {
                    LOG.error(e.getMessage(), e);
                    fileStatusList.add(new FileStatusReport(fileName, e.getMessage()));
                }
            } else if (fileName.endsWith(".json")) {
                try {
                    TypeDefinition type = JsonXMLConvertor.createTypeFromJSON(stream);
                    okTypeMap.put(fileName, type);
                } catch (ConvertationException e) {
                    LOG.error(e.getMessage(), e);
                    fileStatusList.add(new FileStatusReport(fileName, e.getMessage()));
                }
            } else {
                fileStatusList.add(new FileStatusReport(fileName, "Wrong file format."));
            }
        }

        List<TypeDefinition> query = DataSorter.validateAndSort(okTypeMap, fileStatusList);
        List<TypeDTO> sortedTypeList = new ArrayList<TypeDTO>();

        for (TypeDefinition typeDefinition : query) {
            sortedTypeList.add(ObjectTypeReader.readTypeDefinition(typeDefinition));
        }

        return new ResultSet(fileStatusList, sortedTypeList);
    }

    /**
     * This method creates one list, that contains all types and their children.
     * @param typeDTOList typeDTOList
     * @return list
     */
    private List<TypeDTO> getTypeChildren(List<TypeDTO> typeDTOList) {

        List<TypeDTO> listOfChildren = new ArrayList<TypeDTO>();
        listOfChildren.addAll(typeDTOList);

        for(TypeDTO typeDTO : typeDTOList) {

            if (typeDTO.getChildren() != null && !typeDTO.getChildren().isEmpty()) {
                listOfChildren.addAll(getTypeChildren(typeDTO.getChildren()));
            }

        }

        return listOfChildren;
    }
}
