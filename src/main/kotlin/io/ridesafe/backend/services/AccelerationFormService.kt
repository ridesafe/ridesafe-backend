package io.ridesafe.backend.services

import io.ridesafe.backend.extensions.collate
import io.ridesafe.backend.extensions.getDeviceId
import io.ridesafe.backend.extensions.lazyLogger
import io.ridesafe.backend.models.AccelerationForm
import io.ridesafe.backend.security.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest

/**
 * Created by evoxmusic on 17/04/16.
 */
@Service
class AccelerationFormService @Autowired constructor(val accelerationService: AccelerationService,
                                                     val authenticationService: AuthenticationService,
                                                     val req: HttpServletRequest) {

    val log by lazyLogger()

    fun create(accelerationForm: AccelerationForm): AccelerationForm {

        log.info("New form received from device_id: '${req.getDeviceId()}'")

        accelerationService.list(req.getDeviceId(), authenticationService.currentUserId,
                accelerationForm.startTimestamp!!, accelerationForm.endTimestamp!!) { userAccelerations ->

            log.info("${userAccelerations?.size ?: 0} rows are going to be updated")

            // asynchronously executed
            userAccelerations?.forEach { it?.merge(accelerationForm) }

            // batch update accelerations data (to avoid timeout)
            userAccelerations?.asSequence()?.collate(100)?.forEach { accelerationService.update(it) }
        }

        return accelerationForm
    }

}