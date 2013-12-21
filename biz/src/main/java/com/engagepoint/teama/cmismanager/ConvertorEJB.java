package com.engagepoint.teama.cmismanager;

import com.engagepoint.teama.cmismanager.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.model.TypeDTO;
import com.engagepoint.teama.cmismanager.service.ConvertorEJBLocal;
import com.engagepoint.teama.cmismanager.service.ConvertorEJBRemove;
import com.engagepoint.teama.cmismanager.util.DataSorter;
import com.engagepoint.teama.cmismanager.util.JsonXMLConvertor;
import com.engagepoint.teama.cmismanager.util.ObjectTypeReader;
import com.engagepoint.teama.cmismanager.wrappers.TypeDefinitionWrapper;

import org.apache.chemistry.opencmis.client.util.TypeUtils;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParseException;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import javax.ejb.Stateless;
import javax.xml.stream.XMLStreamException;

@Stateless
public class ConvertorEJB implements ConvertorEJBRemove, ConvertorEJBLocal {

    public static final Logger LOG = Logger.getLogger(ConvertorEJB.class);

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
                } catch (XMLStreamException e) {
                    fileStatusList.add(new FileStatusReport(fileName, e.getMessage()));
                }
            } else if (fileName.endsWith(".json")) {
                try {
                    TypeDefinition type = JsonXMLConvertor.createTypeFromJSON(stream);
                    okTypeMap.put(fileName, type);
                } catch (IOException e) {
                    fileStatusList.add(new FileStatusReport(fileName, e.getMessage()));
                } catch (JSONParseException e) {
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

    @Override
    public InputStream createXMLFromType(TypeDTO typeDTO) throws BaseException {

        TypeDefinition typeDefinition = new TypeDefinitionWrapper(typeDTO);
        InputStream inputStream;

        try {
            inputStream = JsonXMLConvertor.createXMLFromType(typeDefinition);
        } catch (XMLStreamException e) {
            throw new BaseException(e.getMessage(), e);
        }
        return inputStream;
    }

    @Override
    public InputStream createJSONFromType(TypeDTO typeDTO) throws BaseException {

        TypeDefinition typeDefinition = new TypeDefinitionWrapper(typeDTO);
        InputStream inputStream;

        try {
            inputStream = JsonXMLConvertor.createJSONFromType(typeDefinition);
        } catch (IOException e) {
            throw new BaseException(e.getMessage(), e);
        }
        return inputStream;
    }

    @Override
    public InputStream createXMLFromTypeIncludeChildren(TypeDTO parentTypeDTO, String path) throws BaseException {

        List<TypeDTO> typeDTOList = new ArrayList<TypeDTO>();
        typeDTOList.add(parentTypeDTO);

        if (!parentTypeDTO.getChildren().isEmpty()) {
            typeDTOList.addAll( getTypeChildren( parentTypeDTO.getChildren() ) );
        }

        InputStream stream;

        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(path)));

            for (TypeDTO typeDTO : typeDTOList) {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                try {

                    TypeUtils.writeToXML(new TypeDefinitionWrapper(typeDTO), outputStream);
                    out.putNextEntry(new ZipEntry(typeDTO.getDisplayName() + ".xml"));
                    out.write( outputStream.toByteArray() );
                    out.closeEntry();

                } catch (XMLStreamException e) {
                    LOG.error(e.getMessage(), e);
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }

            try {
                out.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }

            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
            throw new BaseException(e.getMessage(), e);
        }

        return stream;
    }

    @Override
    public InputStream createJSONFromTypeIncludeChildren(TypeDTO parentTypeDTO, String path) throws BaseException {

        List<TypeDTO> typeDTOList = new ArrayList<TypeDTO>();
        typeDTOList.add(parentTypeDTO);

        if (!parentTypeDTO.getChildren().isEmpty()) {
            typeDTOList.addAll( getTypeChildren( parentTypeDTO.getChildren() ) );
        }

        InputStream stream;

        try {
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(new File(path)));

            for (TypeDTO typeDTO : typeDTOList) {
                OutputStream outputStream = new ByteArrayOutputStream();

                try {

                    TypeUtils.writeToJSON(new TypeDefinitionWrapper(typeDTO), outputStream);
                    out.putNextEntry(new ZipEntry(typeDTO.getDisplayName() + ".json"));
                    out.write(((ByteArrayOutputStream) outputStream).toByteArray());
                    out.closeEntry();

                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }

            try {
                out.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }

            stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            LOG.error(e.getMessage(), e);
            throw new BaseException(e.getMessage(), e);
        }

        return stream;

    }

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
