package io.ridesafe.backend.extensions

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by evoxmusic on 12/04/16.
 */

// Return logger for class
fun <T : Any> logger(forClass: Class<T>): Logger {
    return LoggerFactory.getLogger(forClass)
}

// return logger from extended class (or the enclosing class)
fun <T : Any> T.logger(): Logger {
    return logger(this.javaClass)
}

// return a lazy logger property delegate for enclosing class
fun <R : Any> R.lazyLogger(): Lazy<Logger> {
    return lazy { logger(this.javaClass) }
}

// return a logger property delegate for enclosing class
fun <R : Any> R.injectLogger(): Lazy<Logger> {
    return lazyOf(logger(this.javaClass))
}

// marker interface and related extension (remove extension for Any.logger() in favour of this)
interface Loggable {}

fun Loggable.logger(): Logger = logger(this.javaClass)

// abstract base class to provide logging, intended for companion objects more than classes but works for either
abstract class WithLogging : Loggable {
    val LOG = logger()
}