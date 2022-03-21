package com.mobiledeveloper.vktube.data.video

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class VideoData(
    @PrimaryKey
    val videoId: Long,
    val subscribers: Int,
    val title: String,
    val previewUrl: String,
    val userImage: String,
    val userName: String,
    val viewsCount: Int,
    val dateAdded: Int,
    val likes: Int,
    val likesByMe: Boolean,
    val videoUrl: String,
    val ownerId: Long
)