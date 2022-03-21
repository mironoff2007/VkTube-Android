package com.mobiledeveloper.vktube.ui.screens.feed.models

import com.mobiledeveloper.vktube.data.user.StoredUser
import com.mobiledeveloper.vktube.data.video.VideoData

data class FeedState(
    val items: List<VideoData> = emptyList(),
    val currentUser: StoredUser? = null
)