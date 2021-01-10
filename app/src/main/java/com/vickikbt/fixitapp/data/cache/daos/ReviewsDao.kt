package com.vickikbt.fixitapp.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vickikbt.fixitapp.models.entity.Review
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllReviews(review: List<Review>)

    @Query("SELECT * FROM review_table ORDER BY Created_At ASC") //TODo: Compare if latest first or last first
    fun getAllReviews():Flow<MutableList<Review>>

    @Query("SELECT COUNT(*) FROM review_table")
    suspend fun isReviewCacheAvailable(): Int

}