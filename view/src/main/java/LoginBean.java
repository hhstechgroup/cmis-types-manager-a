import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.String;

@ManagedBean(name = "login")
@SessionScoped
public class LoginBean implements Serializable {
    private String username;
    private String password;
    private String sessionID;

    @NotNull(message = "Please enter url")
    private String url;
    @NotNull(message = "Please enter port")
    private String port;

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String doLogin() {
        String page = "";
        CMISTypeManagerService service = CMISTypeManagerService.getInstance();
       // service = CMISTypeManagerService.getInstance();
        service.setName(username);
        service.setPass(password);
        service.setPort(port);
        service.setUrl(url);
        try{
            service.connect();
            sessionID = service.getSession().toString();
            page = "index";
        }catch (Exception e){
            //   e.getMessage();
            page = "error";
        }

        return page;
    }

    public String doLogout() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        session.invalidate();

        username = null;
        password = null;
        url = null;
        port = null;

        CMISTypeManagerService.getInstance().disconnect();
        return "login";
    }
}