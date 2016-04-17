package io.ridesafe.backend.models

import io.ridesafe.backend.models.BikeType
import io.ridesafe.backend.models.RoadCondition
import io.ridesafe.backend.models.RoadType

/**
 * Created by evoxmusic on 17/04/16.
 */
class AccelerationForm : Rest {

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