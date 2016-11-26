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

package io.ridesafe.backend.extensions

import io.ridesafe.backend.models.Data
import io.ridesafe.backend.models.UserData

/**
 * Created by evoxmusic on 12/04/16.
 */
fun Data?.toUserData(userId: Long = -1, deviceId: String) = this?.let { UserData(userId, deviceId, it) }

fun List<Data?>?.toUserDatas(userId: Long = -1, deviceId: String) = this?.map { e -> e?.toUserData(userId, deviceId) }