package com.a9ae0b01f0ffc.infinite_auth_validation.server

import com.a9ae0b01f0ffc.infinite_auth_validation.base.T_auth_validation_base_6_util

import java.security.Key
import java.security.KeyStore

class T_jwt_manager extends T_auth_validation_base_6_util {

    protected Key p_jwt_key = GC_NULL_OBJ_REF as Key

    Key get_jwt_key() {
        return p_jwt_key
    }

    void init_jwt_key() {
        FileInputStream l_keystore_file_input_stream = new FileInputStream(app_conf().GC_JWT_KEYSTORE_PATH)
        KeyStore l_key_store = KeyStore.getInstance(app_conf().GC_JWT_KEYSTORE_TYPE)
        l_key_store.load(l_keystore_file_input_stream, app_conf().GC_JWT_KEYSTORE_PASSWORD.toCharArray())
        p_jwt_key = l_key_store.getKey(app_conf().GC_JWT_KEYSTORE_ALIAS, app_conf().GC_JWT_KEYSTORE_PASSWORD.toCharArray())
    }

}
