package ru.itech.sno_api.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import ru.itech.sno_api.core.domain.request.PasswordResetRequest
import ru.itech.sno_api.core.domain.request.user.SignInRequest
import ru.itech.sno_api.core.domain.request.user.SignUpRequest
import ru.itech.sno_api.core.util.AuthTokenResponse
import ru.itech.sno_api.service.AuthService

@RestController
@RequestMapping("/api/auth")
@Tag(
    name = "Auth Service API",
    description = "Регистрация, авторизация и аутентификация",
)
class AuthServiceController(
    private val authService: AuthService,
) {
    @PostMapping("/login")
    fun authenticate(
        @RequestBody signInRequest: SignInRequest,
    ): AuthTokenResponse = authService.authenticate(signInRequest)

    @PostMapping("/refresh")
    fun refreshToken(
        @RequestParam("refresh_token") refreshToken: String,
    ): AuthTokenResponse = authService.refreshToken(refreshToken)

    @PostMapping("/register")
    fun registerUser(
        @RequestBody signUpRequest: SignUpRequest,
    ): AuthTokenResponse = authService.registerUser(signUpRequest)

    @PostMapping("/reset-password")
    fun requestPasswordReset(
        @RequestBody email: String,
    ) {
        authService.requestPasswordReset(email)
    }

    @PostMapping("/reset-password/confirm")
    fun confirmPasswordReset(
        @RequestBody request: PasswordResetRequest,
    ) {
        authService.confirmPasswordReset(request)
    }
}
