package io.ridesafe.backend.filters

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by evoxmusic on 16/04/16.
 *
 * Check that the 'Device-Id' header parameter exists and is not null or empty.
 * This field is mandatory
 */
class DeviceIdHeaderFilter : Filter {

    override fun init(filterConfig: FilterConfig?) {
    }

    override fun destroy() {
    }

    override fun doFilter(req: ServletRequest?, res: ServletResponse?, chain: FilterChain?) {
        val request = req as HttpServletRequest
        val response = res as HttpServletResponse

        val deviceIdHeader = request.getHeader("Device-Id")
        if (deviceIdHeader.isNullOrBlank()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Header parameter 'Device-Id' is mandatory")
        }

        chain?.doFilter(request, response)
    }


}