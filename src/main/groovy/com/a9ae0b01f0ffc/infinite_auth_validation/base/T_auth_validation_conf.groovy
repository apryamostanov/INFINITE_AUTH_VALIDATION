package com.a9ae0b01f0ffc.infinite_auth_validation.base

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

import javax.annotation.PostConstruct

@Component
@PropertySource(value = "application.properties")
class T_auth_validation_conf {

    @Value('${authenticationModulesPath}')
    public String authenticationModulesPath


    @Value('${byPassAuthorizationServiceList}')
    public String byPassAuthorizationServiceList

    @Value('${jwtTestMode}')
    public String jwtTestMode

    @PostConstruct
    void init() {
        System.out.println("================== " + authenticationModulesPath + "================== ");
    }

}
