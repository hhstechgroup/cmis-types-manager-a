import org.apache.chemistry.opencmis.client.api.ObjectType;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.definitions.MutablePropertyDefinition;
import org.apache.chemistry.opencmis.commons.definitions.MutableTypeDefinition;
import org.apache.chemistry.opencmis.commons.enums.BindingType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CMISTypeManagerService {
    private static CMISTypeManagerService cmisTypeManagerService;
    private Session session;

    private String url = "localhost";
    private String port = "8080";
    private String name = "";
    private String pass = "";

    private String repo = "/chemistry-opencmis-server-inmemory-0.10.0";     //if not inmemory - change to "/cmis"
    private String repoID = "A1";                                           //may change to

    private CMISTypeManagerService() {
    }

    public static synchronized CMISTypeManagerService getInstance() {
        if (cmisTypeManagerService == null) {
            cmisTypeManagerService = new CMISTypeManagerService();
        }
        return cmisTypeManagerService;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void connect() throws Exception{
        SessionFactory factory = SessionFactoryImpl.newInstance();
        Map<String, String> parameter = new HashMap<String, String>();

        parameter.put(SessionParameter.USER, name);
        parameter.put(SessionParameter.PASSWORD, pass);

        parameter.put(SessionParameter.BINDING_TYPE, BindingType.WEBSERVICES.value());
        parameter.put(SessionParameter.WEBSERVICES_ACL_SERVICE, "http://"+ url + ":" + port + repo + "/services/ACLService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_DISCOVERY_SERVICE, "http://"+ url + ":" + port + repo + "/services/DiscoveryService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_MULTIFILING_SERVICE, "http://"+ url + ":" + port + repo + "/services/MultiFilingService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_NAVIGATION_SERVICE, "http://"+ url + ":" + port + repo + "/services/NavigationService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_OBJECT_SERVICE, "http://"+ url + ":" + port + repo+  "/services/ObjectService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_POLICY_SERVICE, "http://"+ url + ":" + port + repo + "services/PolicyService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_RELATIONSHIP_SERVICE, "http://"+ url + ":" + port + repo + "/services/RelationshipService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_REPOSITORY_SERVICE,"http://"+ url + ":" + port + repo + "/services/RepositoryService?wsdl");
        parameter.put(SessionParameter.WEBSERVICES_VERSIONING_SERVICE, "http://"+ url + ":" + port + repo + "/services/VersioningService?wsdl");
        parameter.put(SessionParameter.REPOSITORY_ID, repoID);

        session = factory.createSession(parameter);

        if(session==null){
            throw new Exception("no such session");
        }

    }

    public void disconnect(){
        session.getBinding().close();

        //???
        //  session = null;
    }

    public Session getSession() {
        return session;
    }

    public List<TypeDTO> getTypes() {
        List<TypeDTO> typeList= new ArrayList<TypeDTO>();

        List<Tree<ObjectType>> list = session.getTypeDescendants(null, -1, true);

        for(Tree<ObjectType> tree : list) {
            typeList.add(ObjectTypeReader.readTree(tree));
        }
        return typeList;
    }

    /**
     * TODO
     * This method should throw exceptions
     * @param newType some TypeDTO instance, that not exist in CMIS repository
     * @return new instance if created, null if not
     * @throws Exception must throw exceptions
     */
    public TypeDTO createType(TypeDTO newType) {
        TypeDTO returnedTypeDTO = null;
        ObjectType createdType = null;
        try {
            ObjectType parentType = getTypeById(newType.getParentTypeId());
            MutableTypeDefinition newTypeDefinition = getTypeDefinition(parentType, newType);

            createdType = session.createType(newTypeDefinition);
        } catch (Exception ex) {
            //TODO throw CannotCreateTypeException or IllegalArgumentException
        }
        if(createdType == null) {
            //TODO throw CannotCreateTypeException
        } else {
            returnedTypeDTO = ObjectTypeReader.readIgnoreChildren(createdType);
        }

        return returnedTypeDTO;
    }

    private MutableTypeDefinition getTypeDefinition(ObjectType parent, TypeDTO newType) {

        MutableTypeDefinition typeDefinition = (MutableTypeDefinition) parent;
        //TestTypeDefinition typeDefinition = (TestTypeDefinition) parent; // ??????

        MutablePropertyDefinition propertyDefinition = (MutablePropertyDefinition)typeDefinition.getPropertyDefinitions().get("cmis:name");

        typeDefinition.removeAllPropertyDefinitions();
        //Do not change !
        //typeDefinition.setBaseTypeId();
        typeDefinition.setParentTypeId(typeDefinition.getId());
        typeDefinition.setId(newType.getId());
        typeDefinition.setDescription(newType.getDescription());
        typeDefinition.setDisplayName(newType.getDisplayName());
        typeDefinition.setLocalName(newType.getLocalName());
        typeDefinition.setLocalNamespace(newType.getLocalNamespace());
        typeDefinition.setQueryName(newType.getQueryName());

        //typeDefinition.setBaseTypeId(BaseTypeId.fromValue(newType.getBaseTypeId()));

        typeDefinition.setIsQueryable(newType.isQueryable());
        typeDefinition.setIsFileable(newType.isFileable());
        typeDefinition.setIsCreatable(newType.isCreatable());

        typeDefinition.setIsControllableAcl(newType.isControllableAcl());
        typeDefinition.setIsControllablePolicy(newType.isControllablePolicy());
        typeDefinition.setIsFulltextIndexed(newType.isFulltextIndexed());
        typeDefinition.setIsIncludedInSupertypeQuery(newType.isIncludedInSupertypeQuery());

        TestTypeMutability testTypeMutability = new TestTypeMutability(newType);
        typeDefinition.setTypeMutability(testTypeMutability);

        //add a property row. Have some problems with enumerations.
        //propertyDefinition.setId("New property");
        //propertyDefinition.setDisplayName("New");
        //propertyDefinition.setLocalName("NewLocName");
        //propertyDefinition.setQueryName("NewQuery");
        //typeDefinition.addPropertyDefinition(propertyDefinition);

        List<PropertyRow> rowsList = newType.getPropertyRows();
        if (rowsList.size() > 0) {
            typeDefinition.addPropertyDefinition(getPropertyDefinition(propertyDefinition, rowsList.get(0)));
        }

        return typeDefinition;
    }

    public ObjectType getTypeById(String id) {

        ObjectType returnedType;

        try {
            returnedType = session.getTypeDefinition(id);
        } catch (Exception ex) {
            returnedType = null;
            //TODO must throw SomeUnknownToMeException
        }
        return returnedType;
    }

    private MutablePropertyDefinition getPropertyDefinition(MutablePropertyDefinition property, PropertyRow row) {

        property.setId(row.getId());
        property.setDisplayName(row.getDisplayName());
        property.setDescription(row.getDescription());
        property.setLocalName(row.getLocalName());
        property.setQueryName(row.getQueryName());
        //TODO

        return property;
    }

    public TypeDTO getSecondaryTypes() {

        ObjectType baseSecondary = getTypeById("cmis:secondary");
        TypeDTO returnedDTO = ObjectTypeReader.readTree(baseSecondary);

        return returnedDTO;
    }
}