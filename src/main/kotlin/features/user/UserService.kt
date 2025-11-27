package com.shoejs.features.user

class UserService {

    fun getUserById(id: Long): UserDetailsResponse? =
        UserRepository.getUserById(id = id)?.toUserDetailsResponse()

    fun updateUser(id: Long, userUpdateRequest: UserUpdateRequest): UserDetailsResponse? =
        UserRepository.updateUserById(
            id = id, username = userUpdateRequest.username, email = userUpdateRequest.email
        )?.toUserDetailsResponse()
}
