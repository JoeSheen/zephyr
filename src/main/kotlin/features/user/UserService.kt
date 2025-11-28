package com.shoejs.features.user

import com.shoejs.utils.formatPhoneNumber

class UserService {

    fun getUserById(id: Long): UserDetailsResponse? =
        UserRepository.getUserById(id = id)?.toUserDetailsResponse()

    fun updateUser(id: Long, userUpdateRequest: UserUpdateRequest): UserDetailsResponse? {
        val phoneNumber = formatPhoneNumber(userUpdateRequest.phoneNumber)
        return UserRepository.updateUserById(
            id = id,
            username = userUpdateRequest.username,
            email = userUpdateRequest.email,
            phoneNumber = phoneNumber
        )?.toUserDetailsResponse()
    }
}
