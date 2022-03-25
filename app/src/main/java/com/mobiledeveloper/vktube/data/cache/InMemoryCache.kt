package com.mobiledeveloper.vktube.data.cache

import com.mobiledeveloper.vktube.ui.common.cell.VideoCellModel
import com.mobiledeveloper.vktube.ui.screens.comments.CommentCellModel
import com.mobiledeveloper.vktube.ui.screens.video.VideoDB

object InMemoryCache {

    val loadedVideos: MutableList<VideoDB> = mutableListOf()
    val clickedVideos: MutableList<VideoCellModel> = mutableListOf()
    val comments: MutableMap<Int, List<CommentCellModel>> = mutableMapOf()
}