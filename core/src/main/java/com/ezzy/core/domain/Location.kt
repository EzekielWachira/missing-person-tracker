package com.ezzy.core.domain

import java.io.Serializable

data class Location(
    val missingPersonId: String? = null,
    val latitude: Long? = null,
    val longitude: Long? = null,
    val locationName: String? = null
): Serializable
