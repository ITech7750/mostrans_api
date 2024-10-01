package ru.itech.sno_api.core.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.stereotype.Component
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import ru.itech.sno_api.core.AuthEntryPoint
import ru.itech.sno_api.core.AuthTokenFilter

@EnableWebSecurity
@Configuration
@Component
open class WebSecurityConfig {
    @Value("\${cors.frontend.host}")
    private lateinit var frontendHost: String

    @Bean
    open fun provideAuthEntryPoint(): AuthenticationEntryPoint = AuthEntryPoint()

    @Bean
    open fun provideAuthTokenFilter(): AuthTokenFilter = AuthTokenFilter()

    @Bean
    open fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager = authConfig.authenticationManager

    @Bean
    open fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    open fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.apply {
            addAllowedOriginPattern("*") // доступ с любых доменов
            HttpMethod.values().forEach {
                addAllowedMethod(it) // все методы
            }
            addAllowedHeader("*") //  все заголовки
            allowCredentials = true // отправка кук
            maxAge = 3600L // время жизни кэша предзапросов (preflight)
        }

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    open fun filterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .cors { cors ->
                cors.configurationSource(corsConfigurationSource())
            }.csrf { it.disable() }
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(
                        "/api/auth/login",
                        "/api/auth/register",
                        "/api/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/favicon.ico",
                        "/webjars/**",
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
            }.exceptionHandling { exception ->
                exception.authenticationEntryPoint(provideAuthEntryPoint())
            }.addFilterAfter(provideAuthTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)
            .build()
}
