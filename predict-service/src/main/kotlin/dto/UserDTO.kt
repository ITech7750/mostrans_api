package ru.itech.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.transaction.annotation.Transactional
import ru.itech.entity.UserEntity


@Schema(description = "Полная информация о пользователе")
data class UserDTO(
    @Schema(description = "Логин пользователя", example = "john_doe")
    val login: String = "",
    @Schema(description = "Пароль для аутентификации пользователя", example = "secure_password")
    var password: String = "",
    @Schema(description = "Электронная почта пользователя", example = "john.doe@example.com")
    val email: String = "",
    @Schema(description = "Уникальный идентификатор пользователя", example = "1001")
    val userId: Long = 0,
    @Schema(description = "Имя пользователя", example = "John", required = false)
    val firstName: String? = null,
    @Schema(description = "Фамилия пользователя", example = "Doe", required = false)
    val lastName: String? = null,
    @Schema(description = "Отчество пользователя", example = "Michael", required = false)
    val middleName: String? = null,
    @Schema(description = "Роль пользователя в системе", example = "Администратор", required = false)
    val role: String? = null,
)

@Transactional
fun UserDTO.toEntity(): UserEntity {
    val userEntity =
        UserEntity.create(
            login = this.login,
            password = this.password,
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName,
            middleName = this.middleName,
            role = this.role,
        )
    userEntity.userId = this.userId
    return userEntity
}
