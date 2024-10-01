package ru.itech.sno_api.core.domain.request.user

import ru.itech.sno_api.core.domain.User


data class SignUpRequest(
    val login: String,
    val email: String,
    val password: String
) {
    fun asDomain(): User = User(
        0,
        login,
        email,
    )
}