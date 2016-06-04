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
import io.ridesafe.backend.models.DataField
import io.ridesafe.backend.models.ProvidedData
import io.ridesafe.backend.models.UserData
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
class DataService @Autowired constructor(val cassandraTemplate: CassandraTemplate,
                                         val authenticationService: AuthenticationService,
                                         val req: HttpServletRequest) {

    val log by lazyLogger()

    @Value("\${cassandra.keyspace}")
    private var keyspace: String? = null

    private val writeListener = object : WriteListener<UserData> {
        override fun onException(x: Exception?) {
            log.error("an error occured while persisted UserData object")
            x?.printStackTrace()
        }

        override fun onWriteComplete(entities: MutableCollection<UserData>?) {
            log.info("${entities?.size} 'UserData' entities persisted !")
        }
    }

    fun create(data: ProvidedData?): ProvidedData? {
        cassandraTemplate.insertAsynchronously(data?.toUserData(authenticationService.currentUserId, req.getDeviceId()), writeListener)
        return data
    }

    fun create(datas: List<ProvidedData?>?): List<ProvidedData?>? {
        log.info("Received ${datas?.size ?: 0} datas to persist")

        if (datas == null || datas.isEmpty())
            return emptyList()

        // convert large list to sublist to batch them
        datas.asSequence().collate(100).forEach {
            cassandraTemplate.insertAsynchronously(it.toUserDatas(authenticationService.currentUserId, req.getDeviceId()), writeListener)
        }

        return datas
    }

    fun list(deviceId: String, userId: Long = -1, startTimestamp: Long, endTimestamp: Long, cls: (List<UserData?>?) -> Unit) {
        val query = "SELECT * FROM $keyspace.data WHERE " +
                "${DataField.DEVICE_ID}='$deviceId' AND " +
                "${DataField.USER_ID}=$userId AND " +
                "${DataField.TIMESTAMP}>$startTimestamp AND " +
                "${DataField.TIMESTAMP}<$endTimestamp"

        log.debug(query)

        cassandraTemplate.queryAsynchronously(query, AsynchronousQueryListener {
            cls(it.get().map { UserData.from(it) })
        })
    }

    fun update(userDatas: List<UserData?>?): List<UserData?>? {
        cassandraTemplate.updateAsynchronously(userDatas, writeListener)
        return userDatas
    }

}