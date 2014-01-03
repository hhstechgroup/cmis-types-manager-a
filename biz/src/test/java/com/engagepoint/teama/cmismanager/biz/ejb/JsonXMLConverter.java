package com.engagepoint.teama.cmismanager.biz.ejb;

import com.engagepoint.teama.cmismanager.common.exceptions.ConvertationException;
import org.junit.BeforeClass;
import org.junit.Test;

public class JsonXMLConverter {
    static JsonXMLConvertor jsonXMLConvertor;

    @BeforeClass
    public static void init() {
        jsonXMLConvertor = new JsonXMLConvertor();
    }

    @Test(expected = NullPointerException.class)
    public void testCreateTypeFromNullJSON() {
        try
        {
            jsonXMLConvertor.createTypeFromJSON(null);
        } catch (ConvertationException e) {}

    }

    @Test(expected = NullPointerException.class)
      public void testCreateTypeFromNullXML() {
        try
        {
            jsonXMLConvertor.createTypeFromXML(null);
        } catch (ConvertationException e) {}
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetJSONFromTypeInByteArrayNull() {
        try
        {
            jsonXMLConvertor.getJSONFromTypeInByteArray(null);
        } catch (ConvertationException e) {}
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetXMLFromTypeInByteArrayNull() {
        try
        {
            jsonXMLConvertor.getXMLFromTypeInByteArray(null);
        } catch (ConvertationException e) {}
    }
}
