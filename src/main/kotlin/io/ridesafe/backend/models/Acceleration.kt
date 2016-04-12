package io.ridesafe.backend.models

import org.springframework.data.annotation.Transient
import org.springframework.data.cassandra.mapping.*
import java.io.Serializable
import javax.validation.constraints.NotNull

/**
 * Created by evoxmusic on 12/04/16.
 */
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

    @PrimaryKey("user_timestamp")
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

    constructor() {
        // empty
    }

    constructor(userId: Long = -1, acceleration: Acceleration) : this(userId,
            acceleration.timestamp, acceleration.x, acceleration.y, acceleration.z)

    constructor(userId: Long = -1, timestamp: Long, x: Float, y: Float, z: Float) {
        this.timestamp = timestamp
        this.x = x
        this.y = y
        this.z = z

        this.userTimestamp = UserTimestamp(userId, timestamp)
    }

}

@PrimaryKeyClass
data class UserTimestamp(
        @PrimaryKeyColumn(ordinal = 1, type = org.springframework.cassandra.core.PrimaryKeyType.PARTITIONED) val userId: Long,
        @NotNull val timestamp: Long
) : Serializable