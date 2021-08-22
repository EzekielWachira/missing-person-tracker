package com.ezzy.core.domain

import java.io.Serializable

data class MissingPerson(
    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,
    val color: String? = null,
    val personStatus: String? = null,
    val age: String? = null,
    val gender: String? =  null,
    val height: Long? = null,
    val weight: Long? = null,
    val description: String? = null,
    val reporterId: String? = null
): Serializable

enum class Gender {
    MALE,
    FEMALE
}