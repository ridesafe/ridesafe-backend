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

import io.ridesafe.backend.extensions.*
import io.ridesafe.backend.models.AccelerationData
import io.ridesafe.backend.models.AccelerationField
import io.ridesafe.backend.models.UserAcceleration
import io.ridesafe.backend.security.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cassandra.core.AsynchronousQueryListener
import org.springframework.data.cassandra.core.CassandraTemplate
import org.springframework.data.cassandra.core.WriteListener
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

/**
 * Created by evoxmusic on 12/04/16.
 */
@Service
class AccelerationService @Autowired constructor(val cassandraTemplate: CassandraTemplate,
                                                 val authenticationService: AuthenticationService,
                                                 val req: HttpServletRequest) {

    val log by lazyLogger()

    @Value("\${cassandra.keyspace}")
    private var keyspace: String? = null

    private val writeListener = object : WriteListener<UserAcceleration> {
        override fun onException(x: Exception?) {
            log.error("an error occured while persisted UserAcceleration object")
            x?.printStackTrace()
        }

        override fun onWriteComplete(entities: MutableCollection<UserAcceleration>?) {
            log.info("${entities?.size} 'UserAcceleration' entities persisted !")
        }
    }

    fun create(acceleration: AccelerationData?): AccelerationData? {
        cassandraTemplate.insertAsynchronously(acceleration?.toUserAcceleration(authenticationService.currentUserId, req.getDeviceId()), writeListener)
        return acceleration
    }

    fun create(accelerations: List<AccelerationData?>?): List<AccelerationData?>? {
        log.info("Received ${accelerations?.size ?: 0} accelerations to persist")

        if (accelerations == null || accelerations.isEmpty())
            return emptyList()

        // convert large list to sublist to batch them
        accelerations.asSequence().collate(100).forEach {
            cassandraTemplate.insertAsynchronously(it.toUserAccelerations(authenticationService.currentUserId, req.getDeviceId()), writeListener)
        }

        return accelerations
    }

    fun list(deviceId: String, userId: Long = -1, startTimestamp: Long, endTimestamp: Long, cls: (List<UserAcceleration?>?) -> Unit) {
        val query = "SELECT * FROM $keyspace.acceleration WHERE " +
                "${AccelerationField.DEVICE_ID}='$deviceId' AND " +
                "${AccelerationField.USER_ID}=$userId AND " +
                "${AccelerationField.TIMESTAMP}>$startTimestamp AND " +
                "${AccelerationField.TIMESTAMP}<$endTimestamp"

        log.debug(query)

        cassandraTemplate.queryAsynchronously(query, AsynchronousQueryListener {
            cls(it.get().map { UserAcceleration.from(it) })
        })
    }

    fun update(userAccelerations: List<UserAcceleration?>?): List<UserAcceleration?>? {
        cassandraTemplate.updateAsynchronously(userAccelerations, writeListener)
        return userAccelerations
    }

}