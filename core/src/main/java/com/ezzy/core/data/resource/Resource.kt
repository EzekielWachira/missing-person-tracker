package com.ezzy.core.data.resource

sealed class Resource<out T> {
    object Loading: Resource<Nothing>()
    object Empty: Resource<Nothing>()
    data class Success<out T>(val data: T): Resource<T>()
    data class Failure<out T>(val errorMessage: String?): Resource<T>()
}