package io.ridesafe.backend.services

import io.ridesafe.backend.extensions.debug
import io.ridesafe.backend.extensions.error
import io.ridesafe.backend.extensions.toUserAcceleration
import io.ridesafe.backend.extensions.toUserAccelerations
import io.ridesafe.backend.models.AccelerationData
import io.ridesafe.backend.models.UserAcceleration
import io.ridesafe.backend.security.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.cassandra.core.CassandraTemplate
import org.springframework.data.cassandra.core.WriteListener
import org.springframework.stereotype.Service

/**
 * Created by evoxmusic on 12/04/16.
 */
@Service
class AccelerationService @Autowired constructor(val cassandraTemplate: CassandraTemplate,
                                                 val authenticationService: AuthenticationService) {

    private val writeListener = object : WriteListener<UserAcceleration> {
        override fun onException(x: Exception?) {
            "an error occured while persisted UserAcceleration object".error(javaClass)
            x?.printStackTrace()
        }

        override fun onWriteComplete(entities: MutableCollection<UserAcceleration>?) {
            "${entities?.size} UserAcceleration entities persisted !"
        }

    }

    fun create(acceleration: AccelerationData?): AccelerationData? {
        cassandraTemplate.insertAsynchronously(acceleration?.toUserAcceleration(authenticationService.currentUserId), writeListener)
        return acceleration
    }

    fun create(accelerations: List<AccelerationData?>?): List<AccelerationData?>? {
        "Received ${accelerations?.size ?: 0} accelerations to persist".debug(javaClass)
        cassandraTemplate.insertAsynchronously(accelerations?.toUserAccelerations(authenticationService.currentUserId), writeListener)
        return accelerations
    }

}