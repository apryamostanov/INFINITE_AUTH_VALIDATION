package com.a9ae0b01f0ffc.infinite_auth_validation.server

import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import static com.a9ae0b01f0ffc.infinite_auth_validation.base.T_auth_validation_base_6_util.*

class T_main_servlet extends HttpServlet {


    private
    void do_any(HttpServletRequest io_http_servlet_request, HttpServletResponse io_http_servlet_response, String i_method) {

    }

    protected void doGet(HttpServletRequest io_http_servlet_request, HttpServletResponse io_http_servlet_response) throws ServletException, IOException {
        do_any(io_http_servlet_request, io_http_servlet_response, GC_METHOD_GET)
    }

    protected void doPost(HttpServletRequest io_http_servlet_request, HttpServletResponse io_http_servlet_response) throws ServletException, IOException {
        do_any(io_http_servlet_request, io_http_servlet_response, GC_METHOD_POST)
    }

    @Override
    protected void doHead(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doHead(req, resp)
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp)
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp)
    }

    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doOptions(req, resp)
    }

    @Override
    protected void doTrace(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doTrace(req, resp)
    }

    private
    static void do_any_with_logging(HttpServletRequest io_http_servlet_request, HttpServletResponse io_http_servlet_response, String i_method) {

    }

}
