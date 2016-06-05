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

package io.ridesafe.backend.security.services

import io.ridesafe.backend.parser.json.MyJsonParser
import io.ridesafe.backend.security.MyUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.*

/**
 * Created by evoxmusic on 13/04/16.
 */
@Service
class AuthenticationService {

    @Value("\${nm.core.account-url}")
    var accountURL: String? = null
    @Value("\${nm.core.login-url}")
    var loginURL: String? = null
    @Value("\${nm.core.logout-url}")
    var logoutURL: String? = null

    private val restTemplate = RestTemplate()

    val currentMyUser: MyUser?
        get() {
            if (SecurityContextHolder.getContext().authentication.principal is MyUser) {
                return SecurityContextHolder.getContext().authentication.principal as MyUser
            } else {
                return null
            }
        }

    val currentUserId: Long
        get() = currentMyUser?.accountMap?.let { it["id"] as Long } ?: -1

    fun doLogin(username: String, password: String): Map<Any, Any> {
        val map = mapOf("username" to username, "password" to password)
        val response = restTemplate.exchange(loginURL, HttpMethod.POST, HttpEntity<Any>(map), String::class.java).body

        return HashMap(MyJsonParser().parseMap(response))
    }

    fun doLogout(accessToken: String) {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("Authorization", accessToken)

        restTemplate.exchange(logoutURL, HttpMethod.POST, HttpEntity<String>(headers), String::class.java).body
    }

    fun getRiderAccount(accessToken: String): Map<Any, Any> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("Authorization", accessToken)

        val response = restTemplate.exchange(accountURL, HttpMethod.GET, HttpEntity<String>(headers), String::class.java).body
        return HashMap(MyJsonParser().parseMap(response))
    }

    fun getMyUserByToken(accessToken: String?): MyUser {
        var riderMap: Map<Any, Any>? = null
        try {
            accessToken?.let { riderMap = getRiderAccount(it) }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (riderMap == null)
            throw UsernameNotFoundException("An error occured and username was not found, is auth service is up and running ?")

        return MyUser(riderMap?.get("nickname") as String, "", true, true, true, true,
                (riderMap?.get("authorities") as List<*>).map { SimpleGrantedAuthority(it as String) }, riderMap)
    }


}
