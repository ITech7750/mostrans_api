package ru.itech.sno_api.core.domain.request.user

data class ResetPasswordRequest(
    val oldPassword: String,
    val password: String
)