package com.a9ae0b01f0ffc.infinite_auth_validation.server

import com.a9ae0b01f0ffc.infinite_auth_validation.base.T_auth_validation_base_6_util

import java.security.Key
import java.security.KeyStore

class T_jwt_manager extends T_auth_validation_base_6_util {

    protected Key p_jwt_key = GC_NULL_OBJ_REF as Key

    Key get_jwt_key() {
        return p_jwt_key
    }

    T_jwt_manager() {
        FileInputStream l_keystore_file_input_stream = new FileInputStream(app_conf().jwtKeyStorePath)
        KeyStore l_key_store = KeyStore.getInstance(app_conf().jwtKeyStoreType)
        l_key_store.load(l_keystore_file_input_stream, app_conf().jwtKeyStorePassword.toCharArray())
        p_jwt_key = l_key_store.getKey(app_conf().jwtKeyStoreAlias, app_conf().jwtKeyStorePassword.toCharArray())
    }

}
