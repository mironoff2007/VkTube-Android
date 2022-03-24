package com.mobiledeveloper.vktube.data.video

import androidx.room.*
import com.mobiledeveloper.vktube.ui.screens.video.VideoDB
import kotlinx.coroutines.flow.Flow

@Dao
interface VideosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addVideoData(videoData: VideoDB)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllVideoData(list: List<VideoDB>)

    @Update
    fun updateVideoData(videoData: VideoDB)

    @Delete
    fun deleteVideoData(videoData: VideoDB)

    @Query("DELETE FROM VideoDB")
    fun resetTable( )

    @Query("SELECT * FROM VideoDB Where VideoDB.videoId Like :id")
    fun getVideoDataById(id: Int): Flow<VideoDB>

}