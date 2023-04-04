package com.wipdev.eWiLL_backend.security.auth

import com.wipdev.eWiLL_backend.database.tables.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails


class UserDetailsImpl(val id:Long,username:String,password:String,val email:String,authorities:MutableCollection<GrantedAuthority>):UserDetails {

    private var username:String = username
    private var password:String = password
    private var authorities:MutableCollection<GrantedAuthority> = authorities



    //build
    companion object{
        fun build(user: User): UserDetailsImpl {
            val authorities:MutableList<GrantedAuthority> = ArrayList()
            user.roles.forEach{
                authorities.add(SimpleGrantedAuthority(it.name!!.name))
            }
            return UserDetailsImpl(user.id,user.username,user.password,user.email,authorities)
        }

    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorities
    }

    override fun getPassword(): String {
        return password
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