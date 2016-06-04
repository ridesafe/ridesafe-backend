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

/**
 * Created by evoxmusic on 17/04/16.
 */
class DataForm : Rest {

    var activityType: ActivityType? = null
    var startTimestamp: Long? = null
    var endTimestamp: Long? = null
    var bikeType: BikeType? = null
    var roadType: RoadType? = null
    var roadCondition: RoadCondition? = null

    constructor() {
        // empty
    }

    constructor (activityType: ActivityType?, startTimestamp: Long, endTimestamp: Long,
                 bikeType: BikeType?, roadType: RoadType?, roadCondition: RoadCondition?) {

        this.activityType = activityType
        this.startTimestamp = startTimestamp
        this.endTimestamp = endTimestamp
        this.bikeType = bikeType
        this.roadType = roadType
        this.roadCondition = roadCondition
    }


    override fun getPropertiesMap(): Map<String, Any?> = mapOf(
            "activity_type" to activityType,
            "start_timestamp" to startTimestamp,
            "end_timestamp" to endTimestamp,
            "bike_type" to bikeType,
            "road_type" to roadType,
            "road_condition" to roadCondition
    )

}