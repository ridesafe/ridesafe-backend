package io.ridesafe.backend.controllers

import io.ridesafe.backend.models.AccelerationForm
import io.ridesafe.backend.services.AccelerationFormService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * Created by evoxmusic on 17/04/16.
 */
@RestController
@RequestMapping("/api/v1/acceleration/form")
class AccelerationFormController @Autowired constructor(val accelerationFormService: AccelerationFormService) {

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun save(@RequestBody @Valid accelerationForm: AccelerationForm) = accelerationFormService.create(accelerationForm).getPropertiesMap()

}