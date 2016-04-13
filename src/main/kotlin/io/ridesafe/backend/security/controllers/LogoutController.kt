package io.ridesafe.backend.security.controllers

import io.ridesafe.backend.security.Security
import io.ridesafe.backend.security.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

import javax.servlet.http.HttpServletRequest

/**
 * Created by evoxmusic on 13/04/16.
 */
@RestController
@RequestMapping("/api/v1/logout")
class LogoutController @Autowired constructor(val authenticationService: AuthenticationService) {

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun save(request: HttpServletRequest): Any {
        return authenticationService.doLogout(request.getHeader(Security.TOKEN_HEADER))
    }

}
