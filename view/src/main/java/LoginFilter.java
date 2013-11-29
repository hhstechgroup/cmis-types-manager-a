/**
 * Created with IntelliJ IDEA.
 * User: ivan.yakubenko
 * Date: 11/20/13
 * Time: 3:17 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "Filter", urlPatterns = {"*.xhtml"})
public class LoginFilter implements Filter {

    public LoginFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest req = (HttpServletRequest) request;

        HttpSession session = ((HttpServletRequest) request).getSession(false);
        String s = session == null ? null : (String) session.getAttribute("sessionID");
        if (s != null && !s.isEmpty()) {
            chain.doFilter(request, response);
        } else {
            req.getRequestDispatcher("/pages/login.xhtml").forward(request, response);
        }

    }

    @Override
    public void destroy() {

    }
}