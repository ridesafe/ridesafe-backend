package io.ridesafe.backend.services

import io.ridesafe.backend.extensions.*
import io.ridesafe.backend.models.AccelerationData
import io.ridesafe.backend.models.AccelerationField
import io.ridesafe.backend.models.UserAcceleration
import io.ridesafe.backend.security.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cassandra.core.AsynchronousQueryListener
import org.springframework.data.cassandra.core.CassandraTemplate
import org.springframework.data.cassandra.core.WriteListener
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

/**
 * Created by evoxmusic on 12/04/16.
 */
@Service
class AccelerationService @Autowired constructor(val cassandraTemplate: CassandraTemplate,
                                                 val authenticationService: AuthenticationService,
                                                 val req: HttpServletRequest) {

    val log by lazyLogger()

    @Value("\${cassandra.keyspace}")
    private var keyspace: String? = null

    private val writeListener = object : WriteListener<UserAcceleration> {
        override fun onException(x: Exception?) {
            log.error("an error occured while persisted UserAcceleration object")
            x?.printStackTrace()
        }

        override fun onWriteComplete(entities: MutableCollection<UserAcceleration>?) {
            "${entities?.size} 'UserAcceleration' entities persisted !"
        }
    }

    fun create(acceleration: AccelerationData?): AccelerationData? {
        cassandraTemplate.insertAsynchronously(acceleration?.toUserAcceleration(authenticationService.currentUserId, req.getDeviceId()), writeListener)
        return acceleration
    }

    fun create(accelerations: List<AccelerationData?>?): List<AccelerationData?>? {
        log.debug("Received ${accelerations?.size ?: 0} accelerations to persist")

        if (accelerations == null || accelerations.isEmpty())
            return emptyList()

        // convert large list to sublist to batch them
        accelerations.asSequence().collate(100).forEach {
            cassandraTemplate.insertAsynchronously(it.toUserAccelerations(authenticationService.currentUserId, req.getDeviceId()), writeListener)
        }

        return accelerations
    }

    fun list(deviceId: String, userId: Long = -1, startTimestamp: Long, endTimestamp: Long, cls: (List<UserAcceleration>?) -> Unit) {
        cassandraTemplate.queryAsynchronously("SELECT * FROM $keyspace.acceleration WHERE " +
                "${AccelerationField.DEVICE_ID}='$deviceId' AND " +
                "${AccelerationField.USER_ID}=$userId AND " +
                "${AccelerationField.TIMESTAMP}>$startTimestamp AND " +
                "${AccelerationField.TIMESTAMP}<$endTimestamp",

                AsynchronousQueryListener {
                    cls(it.get().map { UserAcceleration.from(it) })
                })
    }

    fun update(userAccelerations: List<UserAcceleration>?): List<UserAcceleration>? {
        cassandraTemplate.updateAsynchronously(userAccelerations, writeListener)
        return userAccelerations
    }

}