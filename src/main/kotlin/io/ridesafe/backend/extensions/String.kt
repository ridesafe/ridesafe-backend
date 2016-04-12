package io.ridesafe.backend.extensions

import org.slf4j.LoggerFactory

/**
 * Created by evoxmusic on 12/04/16.
 */
fun <T> String.debug(clazz: Class<T>) = LoggerFactory.getLogger(clazz).debug(this)

fun <T> String.warn(clazz: Class<T>) = LoggerFactory.getLogger(clazz).warn(this)

fun <T> String.error(clazz: Class<T>) = LoggerFactory.getLogger(clazz).error(this)