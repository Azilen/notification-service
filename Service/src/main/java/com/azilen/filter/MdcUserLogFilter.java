package com.azilen.filter;

import com.azilen.security.AuthoritiesConstants;
import com.azilen.security.SecurityUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.Optional;

@Component
public class MdcUserLogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        this.insertIntoMDC(servletRequest);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            this.clearMDC();
        }
    }

    void insertIntoMDC(ServletRequest request) {
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        MDC.put("logged_in_user", currentUserLogin.orElse(AuthoritiesConstants.ANONYMOUS));
    }

    void clearMDC() {
        MDC.remove("logged_in_user");
    }

    @Override
    public void destroy() {
    }
}
