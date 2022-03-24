package com.mobiledeveloper.vktube.ui.screens.video

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class VideoDB(
    val videoId: Int,
    val userImage: String,
    val userName: String,
    val subscribers: Int,
    val addingDate: Int? = null,
    val frame: Int? = null,
    val position: Int? = null
)
{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

}