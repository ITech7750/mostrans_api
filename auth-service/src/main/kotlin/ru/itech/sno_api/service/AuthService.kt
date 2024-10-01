package ru.itech.sno_api.service

import org.springframework.stereotype.Service
import ru.itech.sno_api.core.domain.request.PasswordResetRequest
import ru.itech.sno_api.core.domain.request.user.SignInRequest
import ru.itech.sno_api.core.domain.request.user.SignUpRequest
import ru.itech.sno_api.core.util.AuthTokenResponse

@Service
interface AuthService {
    fun authenticate(signInRequest: SignInRequest): AuthTokenResponse

    fun refreshToken(refreshToken: String): AuthTokenResponse

    fun registerUser(signUpRequest: SignUpRequest): AuthTokenResponse

    fun requestPasswordReset(email: String)

    fun confirmPasswordReset(request: PasswordResetRequest)
}
