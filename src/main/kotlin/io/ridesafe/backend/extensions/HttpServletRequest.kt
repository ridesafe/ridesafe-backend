package io.ridesafe.backend.extensions

import javax.servlet.http.HttpServletRequest

/**
 * Created by evoxmusic on 17/04/16.
 */
fun HttpServletRequest.getDeviceId() = this.getHeader("Device-Id")