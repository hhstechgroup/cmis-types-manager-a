import org.apache.chemistry.opencmis.client.api.*;
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl;
import org.apache.chemistry.opencmis.commons.SessionParameter;
import org.apache.chemistry.opencmis.commons.enums.BindingType;
import java.util.*;

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
        for(int i = 0; i < list.size(); ++i) {
            ObjectType objType = list.get(i).getItem();
            typeList.add(getInf(objType));
        }
        return typeList;
    }

    private TypeDTO getInf(ObjectType objType) {
        TypeDTO dto = new TypeDTO();

        dto.setDisplayName(objType.getDisplayName());
        dto.setDescription(objType.getDescription());
        dto.setFileable(objType.isFileable());
        dto.setQueryable(objType.isQueryable());
        dto.setCreateble(objType.isCreatable());
        dto.setFileable(objType.isFileable());
        dto.setFulltextIndexed(objType.isFulltextIndexed());
        dto.setMutabilityCanCreate(objType.getTypeMutability().canCreate());
        dto.setMutabilityCanDelete(objType.getTypeMutability().canDelete());
        dto.setMutabilityCanUpdate(objType.getTypeMutability().canUpdate());

        Iterator i = objType.getChildren().iterator();
        List<TypeDTO> childs = new ArrayList<TypeDTO>();
        while(i.hasNext())
        {
            TypeDTO dtoChild = new TypeDTO();
            ObjectType child = (ObjectType)i.next();
/*
            dtoChild.setDisplayName(child.getDisplayName());
            dtoChild.setDescription(child.getDescription());
            dtoChild.setFileable(child.isFileable());
            dtoChild.setQueryable(child.isQueryable());
            dtoChild.setCreateble(child.isCreatable());
            dtoChild.setFileable(child.isFileable());
            dtoChild.setFulltextIndexed(child.isFulltextIndexed());*/

            childs.add(getInf(child));

            //childs.add(dtoChild);
        }
        dto.setChilds(childs);

        return dto;
    }

}