package com.mobiledeveloper.vktube.data.video

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VideosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addVideoData(videoData: VideoData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllVideoData(list: List<VideoData>)

    @Update
    fun updateVideoData(videoData: VideoData)

    @Delete
    fun deleteVideoData(videoData: VideoData)

    @Query("DELETE FROM VideoData")
    fun resetTable( )

    @Query("SELECT * FROM VideoData Where VideoData.videoId Like :id")
    fun getVideoDataById(id: Int): Flow<VideoData>

}