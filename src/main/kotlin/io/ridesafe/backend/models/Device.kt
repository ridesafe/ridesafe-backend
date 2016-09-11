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

package io.ridesafe.backend.models

import org.springframework.data.annotation.Transient
import org.springframework.data.cassandra.mapping.*

/**
 * Created by evoxmusic on 11/09/16.
 */

object DeviceField {

    const val DEVICE_KEY = "device_key"
    const val USER_ID = "user_id"
    const val DEVICE_ID = "device_id"
    const val TIMESTAMP = "timestamp"

}

@Table("device")
data class Device(@Transient val userId: Long,
                  @Transient val deviceId: String) {

    @PrimaryKey(DeviceField.DEVICE_KEY)
    val deviceKey = DeviceKey(userId, deviceId)

    @Column(DeviceField.TIMESTAMP)
    val timestamp = System.currentTimeMillis()

}

@PrimaryKeyClass
data class DeviceKey(
        @PrimaryKeyColumn(ordinal = 1, type = org.springframework.cassandra.core.PrimaryKeyType.PARTITIONED) val user_id: Long,
        @PrimaryKeyColumn(ordinal = 2, type = org.springframework.cassandra.core.PrimaryKeyType.CLUSTERED) val device_id: String
)