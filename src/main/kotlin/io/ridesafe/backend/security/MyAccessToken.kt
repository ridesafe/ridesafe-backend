package io.ridesafe.backend.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.SpringSecurityCoreVersion
import org.springframework.security.core.userdetails.UserDetails

/**
 * Created by evoxmusic on 29/06/15.
 *
 * Creates a token with the supplied array of authorities.

 * @param authorities the collection of GrantedAuthoritys for the
 * *                    principal represented by this authentication object.
 */
class MyAccessToken(authorities: Collection<GrantedAuthority>) : AbstractAuthenticationToken(authorities) {

    init {
        super.setAuthenticated(true)
    }

    @JvmOverloads constructor(principal: UserDetails?, authorities: Collection<GrantedAuthority>,
                              accessToken: String?, refreshToken: String? = null, expiration: Int? = null) : this(authorities) {

        this.principal = principal
        this.accessToken = accessToken
        this.refreshToken = refreshToken
        this.expiration = expiration
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun setAuthenticated(authenticated: Boolean) {
        if (authenticated) {
            throw IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead")
        }


        super.setAuthenticated(false)
    }

    override fun getPrincipal(): UserDetails? {
        return principal
    }

    fun setPrincipal(principal: UserDetails) {
        this.principal = principal
    }

    var accessToken: String? = null
    var expiration: Int? = null
    var refreshToken: String? = null
    private var principal: UserDetails? = null

    companion object {
        val serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID
    }
}
