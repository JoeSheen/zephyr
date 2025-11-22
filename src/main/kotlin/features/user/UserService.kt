package com.shoejs.features.user

class UserService {

    fun getUserById(id: Long): UserDetailsResponse? =
        UserRepository.getUserById(id = id)?.toUserDetailsResponse()
}
