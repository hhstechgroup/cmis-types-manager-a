package com.engagepoint.team_a.cmis_manager;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
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
            req.getRequestDispatcher("/login.xhtml").forward(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}