package io.ridesafe.backend.security

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

/**
 * Created by evoxmusic on 13/04/16.
 */
class MyTokenValidationFilter(private val authenticationFailureHandler: AuthenticationFailureHandler?,
                              private val authenticationProvider: AuthenticationProvider?,
                              private val userDetailsService: UserDetailsService?) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest

        val tokenValue = httpRequest.getHeader(Security.TOKEN_HEADER)

        if (tokenValue.isNullOrBlank()) {
            chain.doFilter(request, response)
            return

        }

        val userDetails = (userDetailsService as MyUserDetailsService).loadUserByToken(tokenValue)

        if (userDetails?.isEnabled?.not()!!) {
            logger.info("User: '${userDetails?.username}' try to authenticate but its account is disabled !!!")
            chain.doFilter(request, response)
            return
        }

        val accessToken = MyAccessToken(userDetails, userDetails?.authorities, tokenValue, null, null)
        SecurityContextHolder.getContext().authentication = accessToken

        logger.debug("user: '${userDetails?.username}' authenticated and having following authorities: '${accessToken.authorities}'")

        chain.doFilter(request, response)
    }
}
