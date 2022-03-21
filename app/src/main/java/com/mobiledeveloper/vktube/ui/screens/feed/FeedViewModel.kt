package com.mobiledeveloper.vktube.ui.screens.feed

import androidx.lifecycle.viewModelScope
import com.mobiledeveloper.vktube.base.BaseViewModel
import com.mobiledeveloper.vktube.data.cache.InMemoryCache
import com.mobiledeveloper.vktube.data.clubs.ClubsRepository
import com.mobiledeveloper.vktube.data.user.UserRepository
import com.mobiledeveloper.vktube.data.video.VideoData
import com.mobiledeveloper.vktube.ui.common.cell.mapToVideoCellModel
import com.mobiledeveloper.vktube.ui.screens.feed.models.FeedAction
import com.mobiledeveloper.vktube.ui.screens.feed.models.FeedEvent
import com.mobiledeveloper.vktube.ui.screens.feed.models.FeedState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val clubsRepository: ClubsRepository,
    private val userRepository: UserRepository
) : BaseViewModel<FeedState, FeedAction, FeedEvent>(initialState = FeedState()) {
    override fun obtainEvent(viewEvent: FeedEvent) {
        when (viewEvent) {
            FeedEvent.ScreenShown -> fetchVideos()
            FeedEvent.ClearAction -> clearAction()
            is FeedEvent.VideoClicked -> obtainVideoClick(viewEvent.videoData)
        }
    }

    private fun obtainVideoClick(videoData: VideoData) {
        viewModelScope.launch {
            InMemoryCache.clickedVideos.add(videoData)
            viewAction = FeedAction.OpenVideoDetail(videoData.videoId)
        }
    }

    private fun clearAction() {
        viewModelScope.launch {
            viewAction = null
        }
    }

    private fun fetchVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = try {
                userRepository.fetchLocalUser().userId
            } catch (e: Exception) {
                userRepository.fetchAndSaveUser()
                userRepository.fetchLocalUser().userId
            }

            val clubs = clubsRepository.fetchClubs(userId)
            val videos = clubsRepository.fetchVideos(clubs = clubs, count = 20)
            viewState = viewState.copy(
                items = videos.mapNotNull { model ->
                    model.item.mapToVideoCellModel(
                        userImage = model.userImage,
                        userName = model.userName,
                        subscribers = model.subscribers
                    )
                }
            )
        }
    }
}