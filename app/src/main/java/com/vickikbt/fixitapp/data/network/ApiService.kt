package com.vickikbt.fixitapp.data.network

import com.vickikbt.fixitapp.models.entity.*
import com.vickikbt.fixitapp.models.network.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    //Endpoint to login an already registered user.
    @POST("login")
    suspend fun userLogin(@Body user: LoginRequest): Response<AuthResponse>

    //Endpoint to upload user profile picture to AWS and return profile image URL.
    @Multipart
    @POST("profile")
    suspend fun uploadProfilePicture(@Part upload: MultipartBody.Part): Response<PhotoUploadResponse>

    //Endpoint to register a new user.
    @POST("register")
    suspend fun userRegistration(@Body user: RegistrationRequest): Response<AuthResponse>

    //Endpoint to get a user based on the user id
    @GET("users/{id}")
    suspend fun getUser(@Path("id") id: Int): Response<User>

    //Endpoint to get all post from the server.
    @GET("posts")
    suspend fun fetchAllPosts(): Response<MutableList<Post>>

    //Endpoint to upload post picture to AWS and return the post image URL.
    @Multipart
    @POST("postpic")
    suspend fun uploadPostPicture(@Part upload: MultipartBody.Part): Response<PhotoUploadResponse>

    //Endpoint to upload a post to the api
    @POST("posts")
    suspend fun uploadPost(
        @Header("Authorization") token: String,
        @Body uploadPost: UploadPostRequest
    ): Response<Post>

    //Endpoint to update a specific post.
    @PUT("post/{id}")
    suspend fun updatePost(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body uploadPost: UpdatePostRequest
    ): Response<Post>

    //Endpoint to get user reviews
    @GET("review/{id}")
    suspend fun getUserReviews(@Path("id") userId: Int): Response<List<Review>>

    @POST("review")
    suspend fun reviewUser(
        @Header("Authorization") token: String,
        @Body reviewUserRequest: ReviewUserRequest
    ): Response<Any>

    //Get list of user's posts
    @GET("posts/user/{id}")
    suspend fun getUserPosts(@Path("id") userId: Int): Response<List<Post>>

    //Get list of user's posts
    @GET("work/user/{id}")
    suspend fun getUserWorks(@Path("id") userId: Int): Response<List<Work>>

    //Endpoint to book work.
    @POST("booking")
    suspend fun bookWork(
        @Header("Authorization") token: String,
        @Body bookWorkRequestBody: BookWorkRequest
    ): Response<Post>

    //Endpoint to get booking made for a particular post
    @GET("posts/booking/{id}")
    suspend fun getPostBooking(@Path("id") id: Int): Response<List<Booking>>

    //Endpoint to reject or accept post booking
    @PUT("booking/{id}")
    suspend fun updateBooking(
        @Path("id") bookingId: Int,
        @Body updateBooking: UpdateBookingRequest
    ): Response<Booking>

    //Create work
    @POST("work")
    suspend fun createWork(
        @Header("Authorization") token: String,
        @Body workRequestBody: WorkRequest
    ): Response<Work>

    //Get work
    @GET("work/{id}")
    suspend fun getWork(@Path("id") id: Int): Response<Work>

    //Update work progress
    @PUT("work/{id}")
    suspend fun updateWork(
        @Path("id") id: Int,
        @Body workUpdateRequestRequestBody: WorkUpdateRequest
    ): Response<Work>


    //Get user transactions
    @GET("/transaction/user/{id}")
    suspend fun getUserTransactions(
        @Header("Authorization") token: String,
        @Path("id") userId: Int
    ): Response<List<TransactionResponse>>

    //Create a new transaction
    @POST("transactions")
    suspend fun createTransaction(
        @Header("Authorization") token: String,
        @Body transactionRequestBody: TransactionRequest
    ): Response<TransactionResponse>
}