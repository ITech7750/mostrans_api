package ru.itech.sno_api.core.util
import io.jsonwebtoken.Claims
import ru.itech.sno_api.core.domain.User

interface TokenHelper {
    fun isTokenValid(token: String?): Boolean

    fun isRefreshToken(token: String): Boolean

    fun createToken(
        userDetails: User,
        claims: HashMap<String, Any?> = HashMap(),
        isAccessToken: Boolean = true,
    ): String

    fun getClaims(token: String?): Claims?
}