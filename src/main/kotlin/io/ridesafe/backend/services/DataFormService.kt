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

import io.ridesafe.backend.extensions.collate
import io.ridesafe.backend.extensions.getDeviceId
import io.ridesafe.backend.extensions.lazyLogger
import io.ridesafe.backend.models.DataForm
import io.ridesafe.backend.security.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

/**
 * Created by evoxmusic on 17/04/16.
 */
@Service
class DataFormService @Autowired constructor(val dataService: DataService,
                                             val authenticationService: AuthenticationService,
                                             val req: HttpServletRequest) {

    val log by lazyLogger()

    fun create(dataForm: DataForm): DataForm {

        log.info("New form received from device_id: '${req.getDeviceId()}'")

        dataService.list(req.getDeviceId(), authenticationService.currentUserId,
                dataForm.startTimestamp!!, dataForm.endTimestamp!!) { userDatas ->

            log.info("${userDatas?.size ?: 0} rows are going to be updated")

            // asynchronously executed
            userDatas?.forEach { it?.merge(dataForm) }

            // batch update accelerations data (to avoid timeout)
            userDatas?.asSequence()?.collate(100)?.forEach { dataService.update(it) }
        }

        return dataForm
    }

}