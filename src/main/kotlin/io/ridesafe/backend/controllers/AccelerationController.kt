package io.ridesafe.backend.controllers

import io.ridesafe.backend.models.AccelerationData
import io.ridesafe.backend.services.AccelerationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * Created by evoxmusic on 12/04/16.
 */
@RestController
@RequestMapping("/api/v1/acceleration")
class AccelerationController @Autowired constructor(val accelerationService: AccelerationService) {

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun save(@RequestBody @Valid acceleration: AccelerationData) = accelerationService.create(acceleration)?.getPropertiesMap()

}