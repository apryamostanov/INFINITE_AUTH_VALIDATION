package com.a9ae0b01f0ffc.infinite_auth_validation.server

import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static com.a9ae0b01f0ffc.infinite_auth_validation.base.T_auth_validation_base_6_util.*

class T_main_servlet extends HttpServlet {

    private void init_app_context() {
        mutual_context_registration(getServletContext())
        init_app_context(getInitParameter(GC_CONTEXT_VAR_NAME_CONFIG_FILE_NAME) as String)
        T_logging_base_5_context.init_custom(app_conf().GC_BLACK_BOX_CONFIG, GC_FALSE, app_conf().GC_DYNAMIC_TOKEN_CODE_CLOSURE)
        init_runtime()
        app_conf().GC_DYNAMIC_TOKEN_CODE_CLOSURE = new GroovyShell().evaluate(app_conf().GC_DYNAMIC_TOKEN_CODE) as Closure
    }

    private static void init_thread_context() {
        T_logging_base_5_context.init_custom(app_conf().GC_BLACK_BOX_CONFIG, GC_FALSE, app_conf().GC_DYNAMIC_TOKEN_CODE_CLOSURE)
        set_property(GC_JAVA_PROP_KEYSTORE_TYPE, app_conf().GC_KEYSTORE_TYPE)
        set_property(GC_JAVA_PROP_KEYSTORE_PATH, app_conf().GC_KEYSTORE_PATH)
        set_property(GC_JAVA_PROP_KEYSTORE_PASSWORD, app_conf().GC_KEYSTORE_PASSWORD)
        set_property(GC_JAVA_PROP_DEBUG_MODE, app_conf().GC_JAVAX_DEBUG)
    }

    private
    void do_any(HttpServletRequest io_http_servlet_request, HttpServletResponse io_http_servlet_response, String i_method) {
        try {
            if (is_null(getServletContext().getAttribute(GC_MIDDLEWARE_CONTEXT))) {
                init_app_context()
            }
            init_thread_context()
            do_any_with_logging(io_http_servlet_request, io_http_servlet_response, i_method)
        } catch (Throwable e_other_exception) {
            io_http_servlet_response.setStatus(GC_HTTP_RESP_CODE_SERVER_ERROR)
            test_print_client_error(e_other_exception, io_http_servlet_response)
        } finally {
            l().flush()
            T_logging_base_5_context.deinit()
        }
    }

    protected void doGet(HttpServletRequest io_http_servlet_request, HttpServletResponse io_http_servlet_response) throws ServletException, IOException {
        do_any(io_http_servlet_request, io_http_servlet_response, GC_METHOD_GET)
    }

    protected void doPost(HttpServletRequest io_http_servlet_request, HttpServletResponse io_http_servlet_response) throws ServletException, IOException {
        do_any(io_http_servlet_request, io_http_servlet_response, GC_METHOD_POST)
    }


    @I_black_box("error")
    private static void test_print_client_error(Throwable e_other_exception, HttpServletResponse io_http_servlet_response) {
        if (app_conf().GC_TEST_TRACE_MODE == GC_TRUE_STRING) {
            HashMap<String, Throwable> l_exception_template_param_map = new HashMap<String, Throwable>()
            l_exception_template_param_map.put(GC_E_OTHERS_TEMPLATE_VAR_NAME, e_other_exception)
            String l_trace_error_template_file_name = GC_TRACE_ERROR_TEMPLATE_FILE_NAME
            if (e_other_exception instanceof E_application_exception) {
                l_trace_error_template_file_name = GC_TRACE_ERROR_TEMPLATE_FILE_NAME_APP_EXCEPTION
            }
            io_http_servlet_response.getWriter().write(get_template_manager().get_json_template(l_trace_error_template_file_name).make(l_exception_template_param_map))
        }
    }

    @I_black_box("error")
    private
    static Boolean authenticate(HttpServletRequest io_http_servlet_request, String l_service_name, HttpServletResponse io_http_servlet_response, T_http_message l_incoming_rest_http_request) {
        Enumeration<String> l_headers = io_http_servlet_request.getHeaderNames()
        Boolean l_is_authenticated = GC_FALSE
        Boolean l_authorization_header_present = GC_FALSE
        String l_jwt
        while (l_headers.hasMoreElements()) {
            String l_header_name = l_headers.nextElement()
            if (l_header_name.toLowerCase() == GC_JWT_AUTHORIZATION_HEADER_NAME) {
                l_authorization_header_present = GC_TRUE
                if (not(app_conf().GC_NO_JWT_SERVICES.contains(GC_COMMA + l_service_name + GC_COMMA))) {
                    l_jwt = io_http_servlet_request.getHeader(l_header_name)
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
            l_incoming_rest_http_request.set_header(l_header_name, io_http_servlet_request.getHeader(l_header_name))
        }
        if (app_conf().GC_JWT_TEST_MODE != GC_TRUE_STRING || l_authorization_header_present) {
            if (not(l_is_authenticated) && not(app_conf().GC_NO_JWT_SERVICES.contains(GC_COMMA + l_service_name + GC_COMMA))) {
                io_http_servlet_response.setStatus(GC_HTTP_RESP_CODE_FORBIDDEN_403)
            } else {
                l_is_authenticated = GC_TRUE
            }
        } else {
            l_is_authenticated = GC_TRUE
        }
        return l_is_authenticated
    }

    @I_black_box("error")
    private
    static void do_any_with_logging(HttpServletRequest io_http_servlet_request, HttpServletResponse io_http_servlet_response, String i_method) {
        if (![GC_METHOD_GET, GC_METHOD_POST].contains(i_method)) {
            throw new E_application_exception(s.Unsupported_method_Z1, i_method)
        }
        String l_service_name = get_service_name(get_uri(io_http_servlet_request))
        I_http_message l_incoming_rest_http_request = new T_http_message()
        if (not(authenticate(io_http_servlet_request, l_service_name, io_http_servlet_response, l_incoming_rest_http_request))) {
            return
        }
        l_incoming_rest_http_request.set_method(i_method)
        l_incoming_rest_http_request.set_uri(get_uri(io_http_servlet_request))
        l_incoming_rest_http_request.set_service_name(l_service_name)
        l_incoming_rest_http_request.set_parameters(io_http_servlet_request.getParameterMap() as HashMap<String, String[]>)
        l_incoming_rest_http_request.set_payload_type(GC_PAYLOAD_TYPE_JSON)
        l_incoming_rest_http_request.set_payload(io_http_servlet_request.getReader().getText())
        if (i_method == GC_METHOD_GET) {
            l().put_to_context(nvl(io_http_servlet_request.getParameter(GC_PARAMETER_NAME_UNIQUE_ID), GC_UNKNOWN_UNIQUE_ID), GC_CONTEXT_NAME_UNIQUE_ID)
        } else {
            l().put_to_context(nvl(find_unique_id_json(l_incoming_rest_http_request.get_payload()), GC_UNKNOWN_UNIQUE_ID), GC_CONTEXT_NAME_UNIQUE_ID)
        }
        l().log_receive_http(l_incoming_rest_http_request)
        I_http_message l_rest_http_response = new T_router().dispatch_http_message(l_incoming_rest_http_request)
        io_http_servlet_response.getWriter().write(l_rest_http_response.get_payload())
        l().log_send_http(l_rest_http_response)
        io_http_servlet_response.setStatus(l_rest_http_response.get_status())
    }

}
