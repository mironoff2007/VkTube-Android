package com.mobiledeveloper.vktube.ui.screens.video

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VideoDB(
    val videoId: Int,
    val userImage: String,
    val userName: String,
    val subscribers: Int,
    val addingDate: Int,
    val frame: Int? = null,
    val position: Int? = null,
    val title:String,
    val previewUrl: String,
    val viewsCount: Int,
    val likes: Int,
    val likesByMe: Boolean,
    val videoUrl: String,
    val ownerId: Long
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}