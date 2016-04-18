package io.ridesafe.backend.models

import com.datastax.driver.core.Row
import org.springframework.data.annotation.Transient
import org.springframework.data.cassandra.mapping.*
import java.io.Serializable
import javax.validation.constraints.NotNull

/**
 * Created by evoxmusic on 12/04/16.
 */
object AccelerationField {
    val TIMESTAMP = "timestamp"
    val USER_ID = "user_id"
    val DEVICE_ID = "device_id"
    val ACTIVITY_TYPE = "activity_type"
    val ROAD_CONDITION = "road_condition"
    val ROAD_TYPE = "road_type"
    val BIKE_TYPE = "bike_type"
    val X = "x"
    val Y = "y"
    val Z = "z"
}

interface Acceleration {

    val timestamp: Long
    val x: Float
    val y: Float
    val z: Float

}

class AccelerationData : Acceleration, Rest {

    override var timestamp: Long = 0L
    override var x: Float = 0f
    override var y: Float = 0f
    override var z: Float = 0f

    constructor() {
        // empty
    }

    constructor(timestamp: Long, x: Float, y: Float, z: Float) {
        this.timestamp = timestamp
        this.x = x
        this.y = y
        this.z = z
    }

    @Transient
    override fun getPropertiesMap(): Map<String, Any> = mapOf(
            "timestamp" to timestamp,
            "x" to x,
            "y" to y,
            "z" to z
    )

}

@Table("acceleration")
class UserAcceleration : Acceleration {

    @Transient
    override var timestamp: Long = 0L

    @PrimaryKey("user_device_timestamp")
    @NotNull
    var userTimestamp: UserTimestamp? = null

    @Column
    @NotNull
    override var x: Float = 0f

    @Column
    @NotNull
    override var y: Float = 0f

    @Column
    @NotNull
    override var z: Float = 0f

    @Column("activity_type")
    private var mActivityType: String? = null

    @Column("road_type")
    private var mRoadType: String? = null

    @Column("bike_type")
    private var mBikeType: String? = null

    @Column("road_condition")
    private var mRoadCondition: String? = null

    var activityType: ActivityType?
        @Transient get() = mActivityType?.let { ActivityType.valueOf(it) }
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
        fun from(row: Row?): UserAcceleration? {
            if (row == null)
                return null

            val ua = UserAcceleration(
                    row.getLong(AccelerationField.USER_ID),
                    row.getString(AccelerationField.DEVICE_ID),
                    row.getLong(AccelerationField.TIMESTAMP),
                    row.getDecimal(AccelerationField.X).toFloat(),
                    row.getDecimal(AccelerationField.Y).toFloat(),
                    row.getDecimal(AccelerationField.Z).toFloat())

            ua.activityType = row.getString(AccelerationField.ACTIVITY_TYPE)?.let { ActivityType.valueOf(it.toUpperCase()) }
            ua.bikeType = row.getString(AccelerationField.BIKE_TYPE)?.let { BikeType.valueOf(it.toUpperCase()) }
            ua.roadType = row.getString(AccelerationField.ROAD_TYPE)?.let { RoadType.valueOf(it.toUpperCase()) }
            ua.roadCondition = row.getString(AccelerationField.ROAD_CONDITION)?.let { RoadCondition.valueOf(it.toUpperCase()) }

            return ua
        }
    }

    constructor() {
        // empty
    }

    constructor(userId: Long = -1, deviceId: String, acceleration: Acceleration) : this(userId, deviceId,
            acceleration.timestamp, acceleration.x, acceleration.y, acceleration.z)

    constructor(userId: Long = -1, deviceId: String, timestamp: Long, x: Float, y: Float, z: Float) {
        this.timestamp = timestamp
        this.x = x
        this.y = y
        this.z = z

        this.userTimestamp = UserTimestamp(deviceId, userId, timestamp)
    }

    fun merge(accelerationForm: AccelerationForm): UserAcceleration {
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