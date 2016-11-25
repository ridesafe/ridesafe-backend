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

package io.ridesafe.backend.services

import io.ridesafe.backend.extensions.getDevice
import io.ridesafe.backend.extensions.lazyLogger
import io.ridesafe.backend.models.Device
import io.ridesafe.backend.security.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.cassandra.core.CassandraTemplate
import org.springframework.data.cassandra.core.WriteListener
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

/**
 * Created by evoxmusic on 11/09/16.
 */
@Service
class DeviceService @Autowired constructor(val cassandraTemplate: CassandraTemplate,
                                           val authenticationService: AuthenticationService,
                                           val req: HttpServletRequest) {

    val log by lazyLogger()

    @Value("\${cassandra.keyspace}")
    private var keyspace: String? = null

    private val writeListener = object : WriteListener<Device> {
        override fun onException(x: Exception?) {
            log.error("an error occured while persisted Device object")
            x?.printStackTrace()
        }

        override fun onWriteComplete(entities: MutableCollection<Device>?) {
            log.info("${entities?.size} 'Device' entities persisted !")
        }
    }

    fun create(device: Device = req.getDevice(authenticationService.currentUserId)): Device {

        log.debug("Insert device: $device")
        cassandraTemplate.insertAsynchronously(device, writeListener)

        return device
    }

}