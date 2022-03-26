package com.mobiledeveloper.vktube.ui.screens.feed.models

import com.mobiledeveloper.vktube.ui.common.cell.VideoCellModel

sealed class FeedEvent {
    object Loaded : FeedEvent()
    object Loading : FeedEvent()
    object ScreenShown : FeedEvent()
    object ClearAction : FeedEvent()
    object ScrollEnd : FeedEvent()
    data class VideoClicked(val videoCellModel: VideoCellModel) : FeedEvent()
}