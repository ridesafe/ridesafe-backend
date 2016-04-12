package io.ridesafe.backend.extensions

import io.ridesafe.backend.models.Acceleration
import io.ridesafe.backend.models.UserAcceleration

/**
 * Created by evoxmusic on 12/04/16.
 */
fun Acceleration?.toUserAcceleration(userId: Long = -1) = this?.let { UserAcceleration(userId, it) }

fun List<Acceleration?>?.toUserAccelerations(userId: Long = -1) = this?.map { e -> e?.let { it.toUserAcceleration(userId) } }