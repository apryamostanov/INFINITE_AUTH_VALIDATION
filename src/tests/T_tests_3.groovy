import base.T_middleware_base_6_util
import org.junit.Test

class T_tests_3 {


    @Test
    void test_01() {

        T_middleware_base_6_util.init_app_context("C:\\TPN\\conf\\WORKER\\freeway_worker_normal.conf")

        System.out.println(T_middleware_base_6_util.app_conf().GC_TEST_HTTP_PLAINTEXT_MODE)
        System.out.println(T_middleware_base_6_util.GC_TRUE_STRING)
        System.out.println(T_middleware_base_6_util.app_conf().GC_TEST_HTTP_PLAINTEXT_MODE == T_middleware_base_6_util.GC_TRUE_STRING)
        System.out.println(T_middleware_base_6_util.app_conf().get_conf_file_name())

    }


}
