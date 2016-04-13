package io.ridesafe.backend.security

import java.io.IOException
import java.util.regex.Pattern
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by evoxmusic on 13/04/16.
 */
class MyCORSFilter(webHostRegex: String?) : Filter {

    private val pattern: Pattern

    init {
        pattern = Pattern.compile(webHostRegex)
    }

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {

    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val request = req as HttpServletRequest
        val response = res as HttpServletResponse

        val host = request.getHeader("Origin")
        if (!host.isNullOrBlank() && pattern.matcher(host).find()) {
            response.setHeader("Access-Control-Allow-Origin", host)
        }

        if (request.method != "OPTIONS") {
            chain.doFilter(req, res)
            return

        }

        response.status = 200
    }

    override fun destroy() {

    }

}
