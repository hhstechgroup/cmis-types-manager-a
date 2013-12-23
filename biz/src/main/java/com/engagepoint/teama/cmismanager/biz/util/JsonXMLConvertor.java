package com.engagepoint.teama.cmismanager.biz.util;

import com.engagepoint.teama.cmismanager.common.exceptions.ConvertationException;
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

    public static final Logger LOG = Logger.getLogger(JsonXMLConvertor.class);

    private JsonXMLConvertor () {

    }

    public static InputStream createJSONFromType(TypeDefinition parentType) throws ConvertationException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream;
        try {
            TypeUtils.writeToJSON(parentType, outputStream);
            byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new ConvertationException(e.getMessage(), e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return byteArrayInputStream;
    }

    public static TypeDefinition createTypeFromJSON(InputStream fileStream) throws ConvertationException {
        TypeDefinition type;

        try {
            type = TypeUtils.readFromJSON(fileStream);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            throw new ConvertationException(e.getMessage(), e);
        } catch (JSONParseException e) {
            LOG.error(e.getMessage(), e);
            throw new ConvertationException(e.getMessage(), e);
        } finally {
            try {
                fileStream.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return type;
    }

    public static TypeDefinition createTypeFromXML(InputStream fileStream) throws ConvertationException {
        TypeDefinition type;

        try {
            type = TypeUtils.readFromXML(fileStream);
        } catch (XMLStreamException e) {
            LOG.error(e.getMessage(), e);
            throw new ConvertationException(e.getMessage(), e);
        } finally {
            try {
                fileStream.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        return type;
    }

    public static InputStream createXMLFromType(TypeDefinition parentType) throws ConvertationException {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream byteArrayInputStream;
        try {
            TypeUtils.writeToXML(parentType, outputStream);
            byteArrayInputStream = new ByteArrayInputStream(outputStream.toByteArray());
        } catch (XMLStreamException e) {
            LOG.error(e.getMessage(), e);
            throw new ConvertationException(e.getMessage(), e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }

        return byteArrayInputStream;
    }

}