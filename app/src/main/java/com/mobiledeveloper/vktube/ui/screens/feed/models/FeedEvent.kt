package com.mobiledeveloper.vktube.ui.screens.feed.models

import com.mobiledeveloper.vktube.data.video.VideoData


sealed class FeedEvent {
    object ScreenShown : FeedEvent()
    object ClearAction : FeedEvent()
    data class VideoClicked(val videoData: VideoData) : FeedEvent()
}