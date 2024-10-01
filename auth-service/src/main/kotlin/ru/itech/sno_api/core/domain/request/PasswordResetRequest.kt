package ru.itech.sno_api.core.domain.request



data class PasswordResetRequest(
    val email: String, // Электронная почта пользователя
    val resetToken: String,// Токен для сброса пароля
    val newPassword: String // Новый пароль пользователя
)
