package com.a9ae0b01f0ffc.infinite_auth_validation.server

import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener

class T_servlet_context_listener implements ServletContextListener {
    @Override
    void contextInitialized(ServletContextEvent e) {
        System.out.println("MyServletContextListener Context Initialized")
    }

    @Override
    void contextDestroyed(ServletContextEvent e) {
        System.out.println("MyServletContextListener Context Destroyed")
    }

}