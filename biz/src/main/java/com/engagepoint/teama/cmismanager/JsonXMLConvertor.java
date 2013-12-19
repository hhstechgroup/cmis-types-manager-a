package com.engagepoint.teama.cmismanager;

import com.engagepoint.teama.cmismanager.exceptions.BaseException;
import com.engagepoint.teama.cmismanager.model.TypeDTO;
import com.engagepoint.teama.cmismanager.wrappers.TypeDefinitionWrapper;
import org.apache.chemistry.opencmis.client.util.TypeUtils;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParseException;

import javax.xml.stream.XMLStreamException;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

public final class JsonXMLConvertor {

    private JsonXMLConvertor() {

    }

    public static final Logger LOG = Logger.getLogger(JsonXMLConvertor.class);

    public static TypeDefinition createTypeFromXML(InputStream fileStream) throws XMLStreamException {
        TypeDefinition type;

        try {
            type = TypeUtils.readFromXML(fileStream);
        } finally {
            try {
                fileStream.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return type;
    }

    public static TypeDefinition createTypeFromJSON(InputStream fileStream) throws IOException, JSONParseException {
        TypeDefinition type;

        try {
            type = TypeUtils.readFromJSON(fileStream);
        } finally {
            try {
                fileStream.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return type;
    }

    public static InputStream createXMLFromType(TypeDefinition parentType) throws XMLStreamException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream;
        try {
            TypeUtils.writeToXML(parentType, outputStream);
            byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return byteArrayInputStream;
    }

    public static InputStream createJSONFromType(TypeDefinition parentType) throws IOException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream;
        try {
            TypeUtils.writeToJSON(parentType, outputStream);
            byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return byteArrayInputStream;
    }

    public static InputStream createFileFromType(TypeDTO typeDTO, SupportedFileFormat supportedFileFormat) throws BaseException {

        TypeDefinition typeDefinition = new TypeDefinitionWrapper(typeDTO);
        InputStream inputStream;

        switch (supportedFileFormat) {
            case JSON:
                try {
                    inputStream = createJSONFromType(typeDefinition);
                } catch (IOException e) {
                    throw new BaseException(e.getMessage(), e);
                }
                break;
            case XML:
                try {
                    inputStream = createXMLFromType(typeDefinition);
                } catch (XMLStreamException e) {
                    throw new BaseException(e.getMessage(), e);
                }
                break;
            default:
                throw new BaseException("Unknown file format.");
        }

        return inputStream;
    }
}