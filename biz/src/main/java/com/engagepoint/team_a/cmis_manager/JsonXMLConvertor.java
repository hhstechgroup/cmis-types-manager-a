package com.engagepoint.team_a.cmis_manager;

import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.util.TypeUtils;
import org.apache.chemistry.opencmis.commons.definitions.TypeDefinition;
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
    public JsonXMLConvertor(){
        session = CMISTypeManagerService.getInstance().getSession();
    }

    public void createTypeFromXML(InputStream fileStream){
        TypeDefinition type= null;
        InputStream stream= fileStream;

        try {
            type = TypeUtils.readFromXML(stream);
            ObjectType createdType= session.createType(type);
        } catch (XMLStreamException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void createTypeFromJSON(InputStream fileStream){
        TypeDefinition type= null;
        InputStream stream= fileStream;

        try {
            type = TypeUtils.readFromJSON(stream);
            ObjectType createdType= session.createType(type);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (JSONParseException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public InputStream createXMLFromType(String typeId){
        TypeDefinition parentType= (TypeDefinition) session.getTypeDefinition(typeId);
        OutputStream outputStream = new ByteArrayOutputStream();
        InputStream fileInputStream = null;
        try {
            TypeUtils.writeToXML(parentType, outputStream);
            fileInputStream = new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());
        } catch (XMLStreamException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return fileInputStream;
    }

    public InputStream createJSONFromType(String typeId){
        TypeDefinition parentType= (TypeDefinition) session.getTypeDefinition(typeId);
        OutputStream outputStream = new ByteArrayOutputStream();
        InputStream fileInputStream = null;

        try {
            TypeUtils.writeToJSON(parentType, outputStream);
            fileInputStream = new ByteArrayInputStream(((ByteArrayOutputStream) outputStream).toByteArray());
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return fileInputStream;
    }



}
