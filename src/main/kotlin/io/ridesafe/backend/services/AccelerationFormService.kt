package io.ridesafe.backend.services

import io.ridesafe.backend.extensions.getDeviceId
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

    fun create(accelerationForm: AccelerationForm): AccelerationForm {

        accelerationService.list(req.getDeviceId(), authenticationService.currentUserId,
                accelerationForm.startTimestamp!!, accelerationForm.endTimestamp!!) { userAccelerations ->

            // asynchronously executed
            userAccelerations?.forEach { it.merge(accelerationForm) }

            // update accelerations data
            accelerationService.update(userAccelerations)
        }

        return accelerationForm
    }

}