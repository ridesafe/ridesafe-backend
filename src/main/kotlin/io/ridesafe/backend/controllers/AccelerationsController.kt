package io.ridesafe.backend.controllers

import io.ridesafe.backend.services.AccelerationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

/**
 * Created by evoxmusic on 12/04/16.
 */
@RestController
@RequestMapping("/api/v1/accelerations")
class AccelerationsController @Autowired constructor(val accelerationService: AccelerationService) {

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun save(@RequestBody jsons: List<Map<String, Any>>): Map<String, String> {
        accelerationService.create(jsons)
        return mapOf("result" to "ok")
    }

}