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

            authenticationResult = MyAccessToken(userDetails, userDetails!!.authorities, authenticationRequest.accessToken, null, null)
            logger.debug("Authentication result for '${userDetails.username}': ${authenticationResult.isAuthenticated}")
        }


        return authenticationResult
    }

    override fun supports(authentication: Class<*>): Boolean {
        return MyAccessToken.javaClass.isAssignableFrom(authentication)
    }

}
