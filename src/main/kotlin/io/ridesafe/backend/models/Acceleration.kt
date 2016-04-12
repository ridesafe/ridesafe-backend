package io.ridesafe.backend.models

/**
 * Created by evoxmusic on 12/04/16.
 */
data class Acceleration(val timestamp: Long, val x: Float, val y: Float, val z: Float) : Rest {

    override fun getPropertiesMap(): Map<String, Any> = mapOf(
            "timestamp" to timestamp,
            "x" to x,
            "y" to y,
            "z" to z
    )

}