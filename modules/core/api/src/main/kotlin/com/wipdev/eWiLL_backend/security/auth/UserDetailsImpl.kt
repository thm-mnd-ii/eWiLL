package com.wipdev.eWiLL_backend.security.auth

import com.wipdev.eWiLL_backend.database.tables.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class UserDetailsImpl(
    val id: Long,
    private var username: String,
    val email: String,
    private var authorities: MutableCollection<GrantedAuthority>
) : UserDetails {


    //build
    companion object {
        fun build(user: User): UserDetailsImpl {
            val authorities: MutableList<GrantedAuthority> = ArrayList()
            user.roles.forEach {
                authorities.add(SimpleGrantedAuthority(it.name!!.name))
            }
            return UserDetailsImpl(user.id!!, user.username, user.email, authorities)
        }

    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }


    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}