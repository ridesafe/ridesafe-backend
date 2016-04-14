package io.ridesafe.backend.models

import io.ridesafe.models.BikeType
import io.ridesafe.models.RoadType
import io.ridesafe.models.TripType
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
    val TRIP_TYPE = "trip_type"
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
    private var activity: String? = null

    @Column("road_type")
    private var road: String? = null

    @Column("bike_type")
    private var bike: String? = null

    @Column("trip_type")
    private var trip: String? = null

    var activityType: ActivityType?
        @Transient get() = activity?.let { ActivityType.valueOf(it) }
        @Transient set(value) {
            this.activity = value?.name?.toLowerCase()
        }

    var roadType: RoadType?
        @Transient get() = road?.let { RoadType.valueOf(it) }
        @Transient set(value) {
            this.road = value?.name?.toLowerCase()
        }

    var bikeType: BikeType?
        @Transient get() = bike?.let { BikeType.valueOf(it) }
        @Transient set(value) {
            this.bike = value?.name?.toLowerCase()
        }

    var tripType: TripType?
        @Transient get() = trip?.let { TripType.valueOf(it) }
        @Transient set(value) {
            this.trip = value?.name?.toLowerCase()
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

}

@PrimaryKeyClass
data class UserTimestamp(
        @PrimaryKeyColumn(ordinal = 1, type = org.springframework.cassandra.core.PrimaryKeyType.PARTITIONED) val device_id: String,
        @PrimaryKeyColumn(ordinal = 2, type = org.springframework.cassandra.core.PrimaryKeyType.CLUSTERED) val user_id: Long,
        @PrimaryKeyColumn(ordinal = 3, type = org.springframework.cassandra.core.PrimaryKeyType.CLUSTERED) val timestamp: Long
) : Serializable