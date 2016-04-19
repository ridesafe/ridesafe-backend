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

import java.util.regex.Pattern
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by evoxmusic on 13/04/16.
 */
class MyCORSFilter(webHostRegex: String?) : Filter {

    private val pattern: Pattern

    init {
        pattern = Pattern.compile(webHostRegex)
    }

    override fun init(filterConfig: FilterConfig) {

    }

    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
        val request = req as HttpServletRequest
        val response = res as HttpServletResponse

        val host = request.getHeader("Origin")
        if (!host.isNullOrBlank() && pattern.matcher(host).find()) {
            response.setHeader("Access-Control-Allow-Origin", host)
        }

        if (request.method != "OPTIONS") {
            chain.doFilter(req, res)
            return

        }

        response.status = 200
    }

    override fun destroy() {

    }

}
