import com.engagepoint.teama.cmismanager.biz.ejb.JsonXMLConvertor;
import com.engagepoint.teama.cmismanager.common.exceptions.ConvertationException;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: aleksandr.davidiuk
 * Date: 12/30/13
 * Time: 3:19 PM
 * To change this template use File | Settings | File Templates.
 */
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
