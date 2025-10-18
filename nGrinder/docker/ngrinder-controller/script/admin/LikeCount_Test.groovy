import static net.grinder.script.Grinder.grinder
import static org.junit.Assert.*
import static org.hamcrest.Matchers.*
import net.grinder.script.GTest
import net.grinder.scriptengine.groovy.junit.GrinderRunner
import net.grinder.scriptengine.groovy.junit.annotation.BeforeProcess
import net.grinder.scriptengine.groovy.junit.annotation.BeforeThread
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.ngrinder.http.HTTPRequest
import org.ngrinder.http.HTTPRequestControl
import org.ngrinder.http.HTTPResponse
import org.ngrinder.http.cookie.Cookie
import org.ngrinder.http.cookie.CookieManager

import java.net.URLEncoder

@RunWith(GrinderRunner)
class TestRunner {

    public static GTest testLike
    public static GTest testUnlike
    public static HTTPRequest request
    public static Map<String, String> headers = [:]
    public static List<Cookie> cookies = []

    static String[] userIds = ["20175112@hallym.ac.kr", "20203305@hallym.ac.kr", "20205261@hallym.ac.kr", "testuser@hallym.ac.kr"]

    @BeforeProcess
    public static void beforeProcess() {
        HTTPRequestControl.setConnectionTimeout(300000)

        testLike = new GTest(1, "좋아요 추가")
        testUnlike = new GTest(2, "좋아요 취소")

        request = new HTTPRequest()
        headers.put("Content-Type", "application/x-www-form-urlencoded")

        grinder.logger.info("before process.")
    }

    @BeforeThread
    public void beforeThread() {
        testLike.record(this, "likeTest")
        testUnlike.record(this, "unlikeTest")

        grinder.statistics.delayReports = true
        grinder.logger.info("before thread.")
    }

    @Before
    public void before() {
        request.setHeaders(headers)
        CookieManager.addCookies(cookies)
        grinder.logger.info("before. init headers and cookies")
    }

    @Test
    public void likeTest() {
        def userId = userIds[grinder.threadNumber % userIds.size()]
        def encodedUserId = URLEncoder.encode(userId, "UTF-8")
        HTTPResponse response = request.POST("http://172.19.0.2:8080/api/board/101/like?userID=${encodedUserId}")
        grinder.logger.info("POST 좋아요 응답코드: ${response.statusCode} (userID=${userId})")
        assertThat(response.statusCode, isOneOf(200, 409))
    }

    @Test
    public void unlikeTest() {
        def userId = userIds[grinder.threadNumber % userIds.size()]
        def encodedUserId = URLEncoder.encode(userId, "UTF-8")
        HTTPResponse response = request.DELETE("http://172.19.0.2:8080/api/board/101/like?userID=${encodedUserId}")
        grinder.logger.info("DELETE 좋아요 취소 응답코드: ${response.statusCode} (userID=${userId})")
        assertThat(response.statusCode, isOneOf(200, 404))
    }

    private static org.hamcrest.Matcher<Integer> isOneOf(int... codes) {
        return anyOf(codes.collect { is(it) })
    }
}