package io.ridesafe.backend.services

import io.ridesafe.backend.extensions.debug
import io.ridesafe.backend.models.AccelerationData
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.cassandra.core.CassandraTemplate
import org.springframework.stereotype.Service

/**
 * Created by evoxmusic on 12/04/16.
 */
@Service
class AccelerationService @Autowired constructor(val cassandraTemplate: CassandraTemplate) {

    fun create(acceleration: AccelerationData): AccelerationData? {


        return acceleration
    }

    fun create(accelerations: List<AccelerationData>): List<AccelerationData>? {
        "Received ${accelerations.size} accelerations to persist".debug(javaClass)

        return accelerations
    }

}