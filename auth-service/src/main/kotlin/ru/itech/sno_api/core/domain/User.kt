package ru.itech.sno_api.core.domain


import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

enum class Role {
    ADMIN,
    USER
}

data class User(
    val id: Long = -1,
    val login: String? = null,
    @JvmSynthetic
    val email: String? = null,
    val role: MutableList<Role> = mutableListOf(Role.USER),
) : Domain, UserDetails {
    fun commonInfo(): User =
        User(
            id,
            login,
            email
        )

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = role.map { SimpleGrantedAuthority(it.name) }.toMutableList()

    override fun getPassword(): String? = password

    override fun getUsername(): String? = login

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = false

    override fun isEnabled(): Boolean = true
}