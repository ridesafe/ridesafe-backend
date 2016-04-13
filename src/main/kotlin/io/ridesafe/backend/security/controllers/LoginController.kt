package io.ridesafe.backend.security.controllers

import io.ridesafe.backend.security.services.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletResponse

/**
 * Created by evoxmusic on 13/04/16.
 */
@RestController
@RequestMapping("/api/v1/login")
class LoginController @Autowired constructor(val authenticationService: AuthenticationService) {

    @RequestMapping(method = arrayOf(RequestMethod.POST))
    fun save(@RequestBody params: Map<Any, Any>, response: HttpServletResponse): Map<Any, Any> {
        try {
            return authenticationService.doLogin(params["username"] as String, params["password"] as String)
        } catch (e: InvalidCredentialsException) {
            response.status = HttpServletResponse.SC_BAD_REQUEST
            return mapOf("error" to "bad credentials")
        }
    }

}
