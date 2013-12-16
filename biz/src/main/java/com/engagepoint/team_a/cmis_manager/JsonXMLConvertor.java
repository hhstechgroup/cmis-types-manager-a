package com.engagepoint.team_a.cmis_manager;

import com.engagepoint.team_a.cmis_manager.exceptions.BaseException;
import com.engagepoint.team_a.cmis_manager.exceptions.ConnectionException;
import com.engagepoint.team_a.cmis_manager.exceptions.ModificationException;
import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.util.TypeUtils;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
import org.apache.chemistry.opencmis.commons.exceptions.CmisBaseException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisInvalidArgumentException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisPermissionDeniedException;
import org.apache.chemistry.opencmis.commons.impl.json.parser.JSONParseException;

import javax.xml.stream.XMLStreamException;
import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: v.kondratenko
 * Date: 12/12/13
 * Time: 11:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonXMLConvertor {
    private Session session = null;

    public JsonXMLConvertor() {
        session = CMISTypeManagerService.getInstance().getSession();
    }

    public void createTypeFromXML(InputStream fileStream) throws BaseException {
        TypeDefinition type = null;
        InputStream stream = fileStream;

        try {
            type = TypeUtils.readFromXML(stream);
            session.createType(type);
        } catch (XMLStreamException e) {
            throw new BaseException(e.getMessage(), e);
        } catch (CmisObjectNotFoundException ex) {
            throw new ModificationException(ex.getMessage(), ex);
        } catch (CmisInvalidArgumentException ex) {
            throw new ModificationException(ex.getMessage(), ex);
        } catch (CmisPermissionDeniedException ex) {
            throw new ConnectionException(ex.getMessage(), ex);
        } catch (CmisBaseException cbe) {
            throw new BaseException(cbe.getMessage(), cbe);
        }

    }

    public void createTypeFromJSON(InputStream fileStream) throws JSONParseException, BaseException {
        TypeDefinition type = null;
        InputStream stream = fileStream;

        try {
            type = TypeUtils.readFromJSON(stream);
            session.createType(type);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (CmisObjectNotFoundException ex) {
            throw new ModificationException(ex.getMessage(), ex);
        } catch (CmisInvalidArgumentException ex) {
            throw new ModificationException(ex.getMessage(), ex);
        } catch (CmisPermissionDeniedException ex) {
            throw new ConnectionException(ex.getMessage(), ex);
        } catch (CmisBaseException cbe) {
            throw new BaseException(cbe.getMessage(), cbe);
        }


    }

    public InputStream createXMLFromType(String typeId) throws XMLStreamException {
        TypeDefinition parentType = (TypeDefinition) session.getTypeDefinition(typeId);
        OutputStream outputStream = new ByteArrayOutputStream();
        InputStream fileInputStream = null;
        TypeUtils.writeToXML(parentType, outputStream);
        fileInputStream = new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileInputStream;
    }

    public InputStream createJSONFromType(String typeId) throws BaseException {
        TypeDefinition parentType = (TypeDefinition) session.getTypeDefinition(typeId);
        OutputStream outputStream = new ByteArrayOutputStream();
        InputStream fileInputStream = null;

        try {
            TypeUtils.writeToJSON(parentType, outputStream);
            fileInputStream = new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(e.getMessage(), e);
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileInputStream;
    }


}
