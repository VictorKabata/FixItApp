package com.vickikbt.fixitapp.data.cache.daos

import androidx.room.*
import com.vickikbt.fixitapp.models.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAuthenticatedUser(user: User)

    @Query("SELECT * FROM user_table")
    fun getAuthenticatedUser(): User

    //@Query("DELETE FROM user_table")
    //suspend fun deleteAuthenticatedUser()

}