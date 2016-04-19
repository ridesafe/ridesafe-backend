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
