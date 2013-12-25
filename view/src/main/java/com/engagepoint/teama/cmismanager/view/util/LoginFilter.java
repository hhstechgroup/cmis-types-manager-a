package com.engagepoint.teama.cmismanager.view.util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.FilterConfig;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "Filter", urlPatterns = {"*.xhtml"})
public class LoginFilter implements Filter {

    public LoginFilter() {
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
        //todo complete method, this code is just for sonar
        return;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //todo complete method, this code is just for sonar
        return;
    }
}