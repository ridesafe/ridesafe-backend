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

import com.datastax.driver.core.Row
import org.springframework.data.annotation.Transient
import org.springframework.data.cassandra.mapping.*
import java.io.Serializable
import javax.validation.constraints.NotNull

/**
 * Created by evoxmusic on 12/04/16.
 */
object DataField {
    val TIMESTAMP = "timestamp"
    val USER_ID = "user_id"
    val DEVICE_ID = "device_id"
    val ACTIVITY_TYPE = "activity_type"
    val ROAD_CONDITION = "road_condition"
    val ROAD_TYPE = "road_type"
    val BIKE_TYPE = "bike_type"
    val accX = "acc_x"
    val accY = "acc_y"
    val accZ = "acc_z"
    val gyrX = "gyr_x"
    val gyrY = "gyr_y"
    val gyrZ = "gyr_z"
}

interface Data {

    val timestamp: Long
    val accX: Float
    val accY: Float
    val accZ: Float
    val gyrX: Float
    val gyrY: Float
    val gyrZ: Float

}

class ProvidedData : Data, Rest {

    override var timestamp: Long = 0L
    override var accX: Float = 0f
    override var accY: Float = 0f
    override var accZ: Float = 0f
    override var gyrX: Float = 0f
    override var gyrY: Float = 0f
    override var gyrZ: Float = 0f

    constructor() {
        // empty
    }

    constructor(timestamp: Long, x: Float, y: Float, z: Float) {
        this.timestamp = timestamp
        this.accX = x
        this.accY = y
        this.accZ = z
    }

    @Transient
    override fun getPropertiesMap(): Map<String, Any> = mapOf(
            "timestamp" to timestamp,
            "acc_x" to accX,
            "acc_y" to accY,
            "acc_z" to accZ,
            "gyr_x" to gyrX,
            "gyr_y" to gyrY,
            "gyr_z" to gyrZ
    )

}

@Table("data")
class UserData : Data {

    @Transient
    override var timestamp: Long = 0L

    @PrimaryKey("user_device_timestamp")
    @NotNull
    var userTimestamp: UserTimestamp? = null

    @NotNull
    @Column("acc_x")
    override var accX: Float = 0f

    @NotNull
    @Column("acc_y")
    override var accY: Float = 0f

    @NotNull
    @Column("acc_z")
    override var accZ: Float = 0f

    @NotNull
    @Column("gyr_x")
    override var gyrX: Float = 0f

    @NotNull
    @Column("gyr_y")
    override var gyrY: Float = 0f

    @NotNull
    @Column("gyr_z")
    override var gyrZ: Float = 0f

    @Column("activity_type")
    private var mActivityType: String? = ""

    @Column("road_type")
    private var mRoadType: String? = null

    @Column("bike_type")
    private var mBikeType: String? = null

    @Column("road_condition")
    private var mRoadCondition: String? = null

    var activityType: ActivityType?
        @Transient get() {
            if (mActivityType.isNullOrBlank())
                return null

            return ActivityType.valueOf(mActivityType!!)
        }
        @Transient set(value) {
            this.mActivityType = value?.name
        }

    var roadType: RoadType?
        @Transient get() = mRoadType?.let { RoadType.valueOf(it) }
        @Transient set(value) {
            this.mRoadType = value?.name
        }

    var bikeType: BikeType?
        @Transient get() = mBikeType?.let { BikeType.valueOf(it) }
        @Transient set(value) {
            this.mBikeType = value?.name
        }

    var roadCondition: RoadCondition?
        @Transient get() = mRoadCondition?.let { RoadCondition.valueOf(it) }
        @Transient set(value) {
            this.mRoadCondition = value?.name
        }

    companion object {
        fun from(row: Row?): UserData? {
            if (row == null)
                return null

            val ua = UserData(
                    row.getLong(DataField.USER_ID),
                    row.getString(DataField.DEVICE_ID),
                    row.getLong(DataField.TIMESTAMP),
                    row.getDecimal(DataField.accX).toFloat(),
                    row.getDecimal(DataField.accY).toFloat(),
                    row.getDecimal(DataField.accZ).toFloat(),
                    row.getDecimal(DataField.gyrX).toFloat(),
                    row.getDecimal(DataField.gyrY).toFloat(),
                    row.getDecimal(DataField.gyrZ).toFloat())

            val activityTypeStr = row.getString(DataField.ACTIVITY_TYPE)
            if (!activityTypeStr.isNullOrBlank()) {
                ua.activityType = ActivityType.valueOf(activityTypeStr.toUpperCase())
            }

            ua.bikeType = row.getString(DataField.BIKE_TYPE)?.let { BikeType.valueOf(it.toUpperCase()) }
            ua.roadType = row.getString(DataField.ROAD_TYPE)?.let { RoadType.valueOf(it.toUpperCase()) }
            ua.roadCondition = row.getString(DataField.ROAD_CONDITION)?.let { RoadCondition.valueOf(it.toUpperCase()) }

            return ua
        }
    }

    constructor() {
        // empty
    }

    constructor(userId: Long = -1, deviceId: String, data: Data) : this(userId, deviceId, data.timestamp,
            data.accX, data.accY, data.accZ,
            data.gyrX, data.gyrY, data.gyrZ)

    constructor(userId: Long = -1, deviceId: String, timestamp: Long,
                accX: Float, accY: Float, accZ: Float,
                gyrX: Float, gyrY: Float, gyrZ: Float) {

        this.timestamp = timestamp
        this.accX = accX
        this.accY = accY
        this.accZ = accZ
        this.gyrX = gyrX
        this.gyrY = gyrY
        this.gyrZ = gyrZ

        this.userTimestamp = UserTimestamp(deviceId, userId, timestamp)
    }

    fun merge(accelerationForm: DataForm): UserData {
        this.activityType = accelerationForm.activityType
        this.bikeType = accelerationForm.bikeType
        this.roadType = accelerationForm.roadType
        this.roadCondition = accelerationForm.roadCondition
        return this
    }

}

@PrimaryKeyClass
data class UserTimestamp(
        @PrimaryKeyColumn(ordinal = 1, type = org.springframework.cassandra.core.PrimaryKeyType.PARTITIONED) val device_id: String,
        @PrimaryKeyColumn(ordinal = 2, type = org.springframework.cassandra.core.PrimaryKeyType.CLUSTERED) val user_id: Long,
        @PrimaryKeyColumn(ordinal = 3, type = org.springframework.cassandra.core.PrimaryKeyType.CLUSTERED) val timestamp: Long
) : Serializable