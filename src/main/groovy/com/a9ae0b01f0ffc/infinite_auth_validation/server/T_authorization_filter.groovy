package com.a9ae0b01f0ffc.infinite_auth_validation.server

import com.a9ae0b01f0ffc.infinite_auth_validation.base.T_auth_validation_base_6_util

import javax.servlet.*

class T_authorization_filter implements Filter {

    @Override
    void doFilter(ServletRequest i_servlet_request, ServletResponse i_servlet_response, FilterChain i_filter_chain) throws IOException, ServletException {
        i_filter_chain.doFilter(i_servlet_request, i_servlet_response)
    }

    @Override
    void init(FilterConfig config) throws ServletException {

    }

    @Override
    void destroy() {

    }

}