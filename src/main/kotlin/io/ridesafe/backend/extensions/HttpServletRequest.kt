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

import io.ridesafe.backend.models.Device
import javax.servlet.http.HttpServletRequest

/**
 * Created by evoxmusic on 17/04/16.
 */
fun HttpServletRequest.getDeviceId(): String = this.getHeader("Device-Id")

fun HttpServletRequest.getDeviceBrand(): String? = this.getHeader("Device-Brand")

fun HttpServletRequest.getDeviceModel(): String? = this.getHeader("Device-Model")

fun HttpServletRequest.getDeviceRawModel(): String? = this.getHeader("Device-Raw-Model")

fun HttpServletRequest.getDevice(userId: Long, key: String? = null): Device = Device(userId, getDeviceId(), key, getDeviceBrand(), getDeviceModel(), getDeviceRawModel())