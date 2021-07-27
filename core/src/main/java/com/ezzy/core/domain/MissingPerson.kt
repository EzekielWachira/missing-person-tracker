package com.ezzy.core.domain

import java.io.Serializable

data class MissingPerson(
    val name: String? = null,
    val age: String? = null,
    val gender: Gender? =  null,
    val height: Long? = null,
    val weight: Long? = null,
    val description: String? = null
): Serializable

enum class Gender {
    MALE,
    FEMALE
}