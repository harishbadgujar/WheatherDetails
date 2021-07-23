package com.example.weatherforecast.database.dao.bookmarkdao

import androidx.room.*
import com.example.weatherforecast.database.entity.Bookmark

/**
 * Created by Harish on 18-07-2021
 */
@Dao
interface BookMarkDao {

    @Insert
    suspend fun insert(bookmark : Bookmark) : Long

    @Query("SELECT * FROM bookmark ORDER BY id DESC")
    suspend fun getBookmarks() : List<Bookmark>

    @Delete
    suspend fun delete(bookmark : Bookmark)

}