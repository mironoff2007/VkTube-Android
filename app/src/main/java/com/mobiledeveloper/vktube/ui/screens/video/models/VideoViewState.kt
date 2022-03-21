package com.mobiledeveloper.vktube.ui.screens.video.models

import com.mobiledeveloper.vktube.data.user.StoredUser
import com.mobiledeveloper.vktube.data.video.VideoData
import com.mobiledeveloper.vktube.ui.screens.comments.CommentCellModel

data class VideoViewState(
    val video: VideoData? = null,
    val isLoadingVideo: Boolean? = null,
    val currentUser: StoredUser? = null,
    val comments: List<CommentCellModel> = emptyList()
)