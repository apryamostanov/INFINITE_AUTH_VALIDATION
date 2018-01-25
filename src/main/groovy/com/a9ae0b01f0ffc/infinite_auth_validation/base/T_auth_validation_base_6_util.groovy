package com.a9ae0b01f0ffc.infinite_auth_validation.base

import com.a9ae0b01f0ffc.infinite_auth_validation.client.T_client_response
import groovy.json.JsonSlurper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class T_auth_validation_base_6_util extends T_auth_validation_base_5_context {

    static T_client_response okhttp_request(String i_url, Integer... i_ignore_error_codes = GC_SKIPPED_ARGS as Integer[]) {
        Request l_request = new Request.Builder().url(i_url).build()
        OkHttpClient l_ok_http_client = get_app_context().p_ok_http_client
        Response l_response
        try {
            l_response = l_ok_http_client.newCall(l_request).execute()
            String l_response_body = l_response.body().string()
            if (!l_response.isSuccessful()) {
                if (method_arguments_present(i_ignore_error_codes)) {
                    if (i_ignore_error_codes.contains(l_response.code())) {
                        Object l_slurped_conf_api_response_json = GC_NULL_OBJ_REF
                        JSONObject l_json_object = GC_NULL_OBJ_REF as JSONObject
                        return new T_client_response(p_slurped_response_json: l_slurped_conf_api_response_json, p_okhttp_response: l_response, p_response_string: l_response_body, p_json_object: l_json_object)
                    }
                }
                //throw new E_api_exception(l_response.code(), "Configuration API error")
            } else {
                JsonSlurper l_configuration_api_response_json_slurper = new JsonSlurper()
                Object l_slurped_conf_api_response_json = l_configuration_api_response_json_slurper.parseText(l_response_body)
                return new T_client_response(p_slurped_response_json: l_slurped_conf_api_response_json, p_okhttp_response: l_response, p_response_string: l_response_body)
            }
        }
        finally {
            l_response?.close()
        }
    }

    static String get_service_name(String i_uri) {
        String LC_RELATIVE_PATH = "middleware/Rest/"
        Integer l_start_index = i_uri.lastIndexOf(LC_RELATIVE_PATH) + LC_RELATIVE_PATH.length()
        Integer l_end_index
        if (i_uri.indexOf("/", l_start_index + 1) > 0) {
            l_end_index = i_uri.indexOf("/", l_start_index + 1)
        } else {
            if (i_uri.indexOf("?", l_start_index + 1) > 0) {
                l_end_index = i_uri.indexOf("?", l_start_index + 1)
            } else {
                l_end_index = i_uri.length()
            }
        }
        return i_uri.substring(l_start_index, l_end_index)
    }

    static String get_uri(HttpServletRequest i_http_servlet_request) {
        return i_http_servlet_request.getScheme() + "://" +
                i_http_servlet_request.getServerName() +
                ("http".equals(i_http_servlet_request.getScheme()) && i_http_servlet_request.getServerPort() == 80 || "https".equals(i_http_servlet_request.getScheme()) && i_http_servlet_request.getServerPort() == 443 ? "" : ":" + i_http_servlet_request.getServerPort()) +
                i_http_servlet_request.getRequestURI() +
                (i_http_servlet_request.getQueryString() != null ? "?" + i_http_servlet_request.getQueryString() : "")
    }

    static String remove_jwt_bearer(String i_jwt) {
        if (i_jwt.indexOf("Bearer") != GC_CHAR_INDEX_NOT_EXISTING) {
            return i_jwt.substring(i_jwt.indexOf(GC_SPACE) + GC_ONE_CHAR)
        } else {
            return i_jwt
        }
    }

    static Integer validate_jwt(String i_jwt) {
        try {
            Jwts.parser().setSigningKey(get_jwt_manager().get_jwt_key()).parseClaimsJws(remove_jwt_bearer(i_jwt))
            return GC_JWT_VALIDITY_OK
        } catch (ExpiredJwtException e_expired) {
            return GC_JWT_VALIDITY_EXPIRED
        } catch (Throwable e_others) {
            return GC_JWT_VALIDITY_INVALID
        }
    }

    static boolean authorize(HttpServletRequest i_servlet_request, HttpServletResponse io_http_servlet_response) {
        Enumeration<String> l_headers = i_servlet_request.getHeaderNames()
        Boolean l_is_authenticated = GC_FALSE
        Boolean l_authorization_header_present = GC_FALSE
        String l_jwt
        String l_service_name = get_service_name(get_uri(i_servlet_request))
        while (l_headers.hasMoreElements()) {
            String l_header_name = l_headers.nextElement()
            if (l_header_name.toLowerCase() == GC_JWT_AUTHORIZATION_HEADER_NAME) {
                l_authorization_header_present = GC_TRUE
                if (not(get_app_context().app_conf().byPassAuthorizationServiceList.contains(GC_COMMA + l_service_name + GC_COMMA))) {
                    l_jwt = i_servlet_request.getHeader(l_header_name)
                    Integer l_jwt_status = validate_jwt(l_jwt)
                    if (l_jwt_status == GC_JWT_VALIDITY_INVALID) {
                        io_http_servlet_response.setStatus(GC_HTTP_RESP_CODE_FORBIDDEN_403)
                    } else if (l_jwt_status == GC_JWT_VALIDITY_EXPIRED) {
                        io_http_servlet_response.setStatus(GC_HTTP_RESP_CODE_UNAUTHORIZED_401)
                    } else if (l_jwt_status == GC_JWT_VALIDITY_OK) {
                        l_is_authenticated = GC_TRUE
                    } else {
                        io_http_servlet_response.setStatus(GC_HTTP_RESP_CODE_FORBIDDEN_403)
                    }
                }
            }
        }
        if (get_app_context().app_conf().jwtTestMode != GC_TRUE_STRING || l_authorization_header_present) {
            if (not(l_is_authenticated) && not(get_app_context().app_conf().byPassAuthorizationServiceList.contains(GC_COMMA + l_service_name + GC_COMMA))) {
                io_http_servlet_response.setStatus(GC_HTTP_RESP_CODE_FORBIDDEN_403)
            } else {
                l_is_authenticated = GC_TRUE
            }
        } else {
            l_is_authenticated = GC_TRUE
        }
        return l_is_authenticated
    }


}