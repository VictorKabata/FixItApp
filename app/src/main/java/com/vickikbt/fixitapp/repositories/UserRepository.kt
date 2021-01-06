package com.vickikbt.fixitapp.repositories

import com.vickikbt.fixitapp.data.cache.AppDatabase
import com.vickikbt.fixitapp.data.network.ApiService
import com.vickikbt.fixitapp.models.entity.User
import com.vickikbt.fixitapp.models.network.AuthResponse
import com.vickikbt.fixitapp.models.network.LoginRequest
import com.vickikbt.fixitapp.models.network.PhotoUploadResponse
import com.vickikbt.fixitapp.models.network.RegistrationRequest
import com.vickikbt.fixitapp.utils.SafeApiRequest
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiService: ApiService, private val appDatabase: AppDatabase):SafeApiRequest() {

    suspend fun saveAuthenticatedUser(user: User)=appDatabase.userDAO().saveAuthenticatedUser(user)

    fun getAuthenticatedUser()= flow { emit(appDatabase.userDAO().getAuthenticatedUser()) }

    suspend fun logoutUser()=appDatabase.clearAllTables()

    suspend fun loginUser(email:String, password:String): AuthResponse {
        val loginRequestBody=LoginRequest(email,password)
        return safeApiRequest { apiService.userLogin(loginRequestBody) }
    }

    suspend fun registerUser(
        username: String,
        email: String,
        phoneNumber: String,
        imageUrl: String,
        specialisation: String,
        password: String,
        latitude: Double,
        longitude: Double,
        region: String,
        address: String,
        country: String
    ): AuthResponse {
        val registrationRequestBody = RegistrationRequest(
            username,
            email,
            phoneNumber,
            imageUrl,
            specialisation,
            password,
            latitude,
            longitude,
            region,
            address,
            country
        )

        return safeApiRequest { apiService.userRegistration(registrationRequestBody) }
    }

    suspend fun uploadProfilePicture(profilePicture: MultipartBody.Part): PhotoUploadResponse {
        return safeApiRequest { apiService.uploadProfilePicture(profilePicture) }
    }

}