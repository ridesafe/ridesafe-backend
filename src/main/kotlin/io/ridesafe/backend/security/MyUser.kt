package io.ridesafe.backend.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

/**
 * Created by evoxmusic on 13/04/16.
 *
 * Constructor.

 * @param username              the username presented to the
 * *                              `DaoAuthenticationProvider`
 * *
 * @param password              the password that should be presented to the
 * *                              `DaoAuthenticationProvider`
 * *
 * @param enabled               set to `true` if the user is enabled
 * *
 * @param accountNonExpired     set to `true` if the account has not expired
 * *
 * @param credentialsNonExpired set to `true` if the credentials have not expired
 * *
 * @param accountNonLocked      set to `true` if the account is not locked
 * *
 * @param authorities           the authorities that should be granted to the caller if they
 * *                              presented the correct username and password and the user is enabled. Not null.
 * *
 * @param id                    the id of the domain class instance used to populate this
 */
class MyUser(username: String, password: String, enabled: Boolean, accountNonExpired: Boolean, credentialsNonExpired: Boolean,
             accountNonLocked: Boolean, authorities: Collection<GrantedAuthority>?,
             var accountMap: Map<Any, Any>?) : User(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities) {

    companion object {
        private val serialVersionUID: Long = 1
    }

}
