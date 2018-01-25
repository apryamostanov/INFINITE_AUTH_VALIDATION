package com.a9ae0b01f0ffc.infinite_auth_validation

import com.a9ae0b01f0ffc.infinite_auth_validation.server.T_authorization_filter
import com.a9ae0b01f0ffc.infinite_auth_validation.server.T_main_servlet
import com.a9ae0b01f0ffc.infinite_auth_validation.server.T_servlet_context_listener
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean

import javax.servlet.ServletContextListener

@SpringBootApplication
class T_spring_boot_application {

    @Bean
    ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new T_main_servlet(), "/*")
    }

    @Bean
    FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean l_filter_registration_bean = new FilterRegistrationBean(new T_authorization_filter())
        l_filter_registration_bean.addServletRegistrationBeans(servletRegistrationBean())
        return l_filter_registration_bean
    }

    @Bean
    ServletListenerRegistrationBean<ServletContextListener> listenerRegistrationBean() {
        ServletListenerRegistrationBean<ServletContextListener> bean =
                new ServletListenerRegistrationBean<>()
        bean.setListener(new T_servlet_context_listener())
        return bean

    }

    static void main(String[] args) {
        SpringApplication.run(T_spring_boot_application.class, args)
    }

}