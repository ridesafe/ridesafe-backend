/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

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
