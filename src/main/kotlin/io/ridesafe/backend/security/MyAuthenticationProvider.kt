package io.ridesafe.backend.security

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.util.Assert

/**
 * Created by evoxmusic on 13/04/16.
 */
class MyAuthenticationProvider : AuthenticationProvider {

    private val logger = LoggerFactory.getLogger(MyAuthenticationProvider::class.java)

    @Autowired
    var userDetailsService: UserDetailsService? = null

    override fun authenticate(authentication: Authentication): Authentication? {
        Assert.isInstanceOf(MyAccessToken::class.java, authentication, "Only AccessToken is supported")
        val authenticationRequest = authentication as MyAccessToken
        var authenticationResult: MyAccessToken? = null

        if (!authenticationRequest.accessToken.isNullOrBlank()) {
            val userDetails = (userDetailsService as MyUserDetailsService).loadUserByToken(authenticationRequest.accessToken)
            if (userDetails?.isEnabled?.not()!!)
                return null

            authenticationResult = MyAccessToken(userDetails, userDetails?.authorities, authenticationRequest.accessToken, null, null)
            logger.debug("Authentication result for '${userDetails?.username}': ${authenticationResult?.isAuthenticated}")
        }


        return authenticationResult
    }

    override fun supports(authentication: Class<*>): Boolean {
        return MyAccessToken.javaClass.isAssignableFrom(authentication)
    }

}
