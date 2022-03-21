package com.mobiledeveloper.vktube.data.cache

import com.mobiledeveloper.vktube.data.video.VideoData
import com.mobiledeveloper.vktube.ui.screens.comments.CommentCellModel

object InMemoryCache {

    val clickedVideos: MutableList<VideoData> = mutableListOf()
    val comments: MutableMap<Long, List<CommentCellModel>> = mutableMapOf()
}