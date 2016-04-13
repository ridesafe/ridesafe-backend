package io.ridesafe.backend.security

import io.ridesafe.backend.security.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService

/**
 * Created by evoxmusic on 13/04/16.
 */
class MyUserDetailsService : UserDetailsService {

    @Autowired
    var authenticationService: AuthenticationService? = null

    override fun loadUserByUsername(username: String): UserDetails? {
        return null
    }

    fun loadUserByToken(accessToken: String?): UserDetails? {
        return authenticationService?.getMyUserByToken(accessToken)
    }

}
