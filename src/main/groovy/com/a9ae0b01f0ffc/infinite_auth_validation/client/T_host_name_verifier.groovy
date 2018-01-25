package com.a9ae0b01f0ffc.infinite_auth_validation.client

import groovy.transform.ToString

import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSession

@ToString(includeNames = true, includeFields = true, includeSuper = false)
class T_host_name_verifier implements HostnameVerifier {

    @Override
    boolean verify(String l_host_name, SSLSession l_ssl_session) {
        return true
    }
}
