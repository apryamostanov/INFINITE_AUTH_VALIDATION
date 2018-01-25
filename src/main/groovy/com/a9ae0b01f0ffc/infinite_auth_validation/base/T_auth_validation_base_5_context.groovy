package com.a9ae0b01f0ffc.infinite_auth_validation.base

import com.a9ae0b01f0ffc.infinite_auth_validation.client.T_host_name_verifier
import com.a9ae0b01f0ffc.infinite_auth_validation.server.T_jwt_manager
import com.fasterxml.jackson.annotation.JsonIgnore
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import other.T_thread_local

import javax.net.ssl.HostnameVerifier

@Component
class T_auth_validation_base_5_context extends T_auth_validation_base_4_const {

    static T_thread_local<T_auth_validation_base_5_context> p_auth_base_5_context_thread_local = new T_thread_local<T_auth_validation_base_5_context>()
    OkHttpClient p_ok_http_client = new OkHttpClient.Builder().hostnameVerifier(get_unsecure_host_name_verifier()).build()
    private T_jwt_manager p_jwt_manager = GC_NULL_OBJ_REF as T_jwt_manager
    @Autowired
    @JsonIgnore
    T_auth_validation_conf p_app_conf

    static HostnameVerifier get_unsecure_host_name_verifier() {
        return new T_host_name_verifier()
    }

    T_auth_validation_base_5_context() {
        p_auth_base_5_context_thread_local.set(this)
    }

    static T_auth_validation_base_5_context get_app_context() {
        if (is_null(p_auth_base_5_context_thread_local.get(T_auth_validation_base_5_context.class))) {
            p_auth_base_5_context_thread_local.set(new T_auth_validation_base_5_context())

        }
        return p_auth_base_5_context_thread_local.get(T_auth_validation_base_5_context.class) as T_auth_validation_base_5_context
    }


    T_auth_validation_conf app_conf() {
        return p_app_conf
    }

    static T_jwt_manager get_jwt_manager() {
        return get_app_context().p_jwt_manager
    }

}
