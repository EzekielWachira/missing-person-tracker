package com.ezzy.core.domain

import java.io.Serializable

/**
 * This class represents an entity of a missing person
 * */
data class MissingPerson(
    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,
    val color: String? = null,
    val personStatus: String? = null,
    val age: String? = null,
    val gender: String? =  null,
    val height: Float? = null,
    val weight: Float? = null,
    val description: String? = null,
    val reporterId: String? = null,
    val foundStatus: Boolean? = null,
    val reportTime: Long? = null
): Serializable

enum class Gender {
    MALE,
    FEMALE
}