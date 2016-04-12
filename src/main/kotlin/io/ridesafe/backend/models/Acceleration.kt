package io.ridesafe.backend.models

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

data class AccelerationData(override val timestamp: Long,
                            override val x: Float,
                            override val y: Float,
                            override val z: Float) : Acceleration, Rest {

    override fun getPropertiesMap(): Map<String, Any> = mapOf(
            "timestamp" to timestamp,
            "x" to x,
            "y" to y,
            "z" to z
    )

}

@Table("acceleration")
class UserAcceleration(var userId: Long = 0, acceleration: Acceleration) : Acceleration by acceleration {

    @PrimaryKey("user_timestamp")
    @NotNull
    val userTimestamp = UserTimestamp(userId, timestamp)

    @Column("x")
    @NotNull
    val xAxis = x

    @Column("y")
    @NotNull
    val yAxis = y

    @Column("z")
    @NotNull
    val zAxis = z

}

@PrimaryKeyClass
data class UserTimestamp(
        @PrimaryKeyColumn(ordinal = 1, type = org.springframework.cassandra.core.PrimaryKeyType.PARTITIONED) val userId: Long,
        @NotNull val timestamp: Long
) : Serializable