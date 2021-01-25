package com.vickikbt.fixitapp.data.cache.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vickikbt.fixitapp.models.entity.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveAllPosts(posts: List<Post>)

    @Query("SELECT * FROM post_table ORDER BY `Created_At` DESC")
    fun getAllPosts(): Flow<MutableList<Post>>

    @Query("SELECT * FROM post_table WHERE id=:id")
    fun getPost(id: Int): Post//Flow<Post>

    @Query("SELECT COUNT(*) FROM post_table")
    suspend fun isPostCacheAvailable(): Int
}