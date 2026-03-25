package com.shoejs.features.user

import com.shoejs.common.validation.formatPhoneNumber

class UserService {

    fun getUserById(id: Long): UserDetailsResponse? =
        UserRepository.getUserById(id = id)?.toUserDetailsResponse()

    fun updateUser(id: Long, userUpdateRequest: UserUpdateRequest): UserDetailsResponse? {
        val phoneNumber = userUpdateRequest.phoneNumber?.let { numberToParse ->
            formatPhoneNumber(numberToParse, userUpdateRequest.countryCode)
        }
        val gender = userUpdateRequest.gender?.let { genderStr ->
            runCatching { Gender.valueOf(genderStr) }.getOrNull()
        }

        return UserRepository.updateUserById(
            id = id,
            username = userUpdateRequest.username,
            email = userUpdateRequest.email,
            gender = gender,
            phoneNumber = phoneNumber
        )?.toUserDetailsResponse()
    }

    fun deleteUserById(id: Long): Boolean =
        UserRepository.deleteUserById(id = id)
}
