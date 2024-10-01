package ru.itech.sno_api.service.implementation

import jakarta.persistence.EntityNotFoundException
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.itech.sno_api.core.JwtHelper
import ru.itech.sno_api.core.domain.User
import ru.itech.sno_api.core.domain.request.PasswordResetRequest
import ru.itech.sno_api.core.domain.request.user.SignInRequest
import ru.itech.sno_api.core.domain.request.user.SignUpRequest
import ru.itech.sno_api.core.util.AuthTokenResponse
import ru.itech.sno_api.dto.UserDTO
import ru.itech.sno_api.dto.toEntity
import ru.itech.sno_api.repository.UserRepository
import ru.itech.sno_api.service.AuthService
import java.util.UUID

@Service
@Transactional
open class AuthServiceImpl(
    private val userRepository: UserRepository,
    private val jwtHelper: JwtHelper,
    private val passwordEncoder: PasswordEncoder,
) : AuthService {
    override fun authenticate(signInRequest: SignInRequest): AuthTokenResponse {
        val userEntity =
            userRepository
                .findByLogin(signInRequest.login)
                .orElseThrow { EntityNotFoundException("User with login ${signInRequest.login} not found") }

        if (!passwordEncoder.matches(signInRequest.password, userEntity.password)) {
            throw BadCredentialsException("Invalid username or password")
        }

        val user =
            User(
                id = userEntity.userId,
                login = userEntity.login,
                email = userEntity.email,
            )

        val accessToken = jwtHelper.createToken(user, HashMap(), isAccessToken = true)
        val refreshToken = jwtHelper.createToken(user, HashMap(), isAccessToken = false)

        return AuthTokenResponse(accessToken, refreshToken)
    }

    override fun refreshToken(refreshToken: String): AuthTokenResponse {
        if (!jwtHelper.isRefreshToken(refreshToken)) {
            throw IllegalArgumentException("Invalid token type. Expected a refresh token.")
        }

        if (!jwtHelper.isTokenValid(refreshToken)) {
            throw IllegalArgumentException("Refresh token has expired. Please login again.")
        }

        val claims =
            jwtHelper.getClaims(refreshToken)
                ?: throw IllegalArgumentException("Failed to parse claims from the refresh token.")

        val userId = claims["id"]?.toString()?.toLongOrNull() ?: throw IllegalArgumentException("User ID is missing in the refresh token.")

        val userEntity =
            userRepository
                .findById(userId)
                .orElseThrow { EntityNotFoundException("User with ID $userId not found.") }

        val user =
            User(
                id = userEntity.userId,
                login = userEntity.login,
                email = userEntity.email,
            )

        // Генерация нового токена доступа и обновления refresh token
        val newAccessToken = jwtHelper.createToken(user, HashMap(), isAccessToken = true)
        val newRefreshToken = jwtHelper.createToken(user, HashMap(), isAccessToken = false)

        return AuthTokenResponse(newAccessToken, newRefreshToken)
    }

    override fun registerUser(signUpRequest: SignUpRequest): AuthTokenResponse {
        if (userRepository.findByEmail(signUpRequest.email).isPresent) {
            throw IllegalArgumentException("User with email ${signUpRequest.email} already exists")
        }

        val hashedPassword = passwordEncoder.encode(signUpRequest.password)

        val userDTO =
            UserDTO(
                login = signUpRequest.login,
                email = signUpRequest.email,
                password = hashedPassword,
            )

        val userEntity = userDTO.toEntity()
        val savedUserEntity = userRepository.save(userEntity)

        val user =
            User(
                id = savedUserEntity.userId,
                login = savedUserEntity.login,
                email = savedUserEntity.email,
            )

        val accessToken = jwtHelper.createToken(user, HashMap(), isAccessToken = true)
        val refreshToken = jwtHelper.createToken(user, HashMap(), isAccessToken = false)

        return AuthTokenResponse(accessToken, refreshToken)
    }

    override fun requestPasswordReset(email: String) {
        val user =
            userRepository.findByEmail(email).orElseThrow {
                EntityNotFoundException("User with email $email not found")
            }

        // Генерация токена сброса пароля
        val resetToken = UUID.randomUUID().toString()
        /*
        В работе...
        Предполагается сохранение токена сброса пароля в базе данных
        тут будет код для сохранения токена и его срока действия
        далее отправка ссылки на сброс пароля на указанный email
        код для отправки email
         */
    }

    override fun confirmPasswordReset(request: PasswordResetRequest) {
        val user =
            userRepository.findByEmail(request.email).orElseThrow {
                EntityNotFoundException("User with email ${request.email} not found")
            }

        /*
        Проверка валидности токена сброса пароля
        код для проверки токена и срока его действия
        обновление пароля
         */

        val hashedPassword = passwordEncoder.encode(request.newPassword)
        user.password = hashedPassword
        userRepository.save(user)
    }
}
