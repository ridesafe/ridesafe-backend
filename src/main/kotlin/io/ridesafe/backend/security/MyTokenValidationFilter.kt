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

        val accessToken = MyAccessToken(userDetails, userDetails?.authorities!!, tokenValue, null, null)
        SecurityContextHolder.getContext().authentication = accessToken

        logger.debug("user: '${userDetails?.username}' authenticated and having following authorities: '${accessToken.authorities}'")

        chain.doFilter(request, response)
    }
}
