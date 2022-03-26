package com.mobiledeveloper.vktube.ui.screens.feed

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mobiledeveloper.vktube.base.BaseViewModel
import com.mobiledeveloper.vktube.data.cache.InMemoryCache
import com.mobiledeveloper.vktube.data.clubs.ClubsRepository
import com.mobiledeveloper.vktube.data.user.UserRepository
import com.mobiledeveloper.vktube.data.video.VideosRepository
import com.mobiledeveloper.vktube.ui.common.cell.VideoCellModel
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
    private val userRepository: UserRepository,
    private val videosRepository: VideosRepository
) : BaseViewModel<FeedState, FeedAction, FeedEvent>(initialState = FeedState()) {
    override fun obtainEvent(viewEvent: FeedEvent) {
        when (viewEvent) {
            FeedEvent.ScreenShown -> fetchVideos()
            FeedEvent.ScrollEnd -> fetchVideos()
            FeedEvent.ClearAction -> clearAction()
            is FeedEvent.VideoClicked -> obtainVideoClick(viewEvent.videoCellModel)
        }
    }

    private fun obtainVideoClick(videoCellModel: VideoCellModel) {
        viewModelScope.launch {
            InMemoryCache.clickedVideos.add(videoCellModel)
            viewAction = FeedAction.OpenVideoDetail(videoCellModel.videoId)
        }
    }

    private fun clearAction() {
        viewModelScope.launch {
            viewAction = null
        }
    }

    var frame = -1

    private fun fetchVideos() {
        viewModelScope.launch(Dispatchers.IO) {
            val userId = try {
                userRepository.fetchLocalUser().userId
            } catch (e: Exception) {
                userRepository.fetchAndSaveUser()
                userRepository.fetchLocalUser().userId
            }

            Log.e("Test_tag", "Loading videos")

            val clubs = clubsRepository.fetchClubs(userId)
            //val videos = clubsRepository.fetchVideos(clubs = clubs, count = 20)

            frame++
            val videos =  videosRepository.getFrame(frame = frame, count = 2, clubs = clubs)

            Log.e("Test_tag", "videos size ${videos.size}")

            viewState = viewState.copy(
                items = videos.mapNotNull { model ->
                    VideoCellModel(
                        videoId = model.videoId,
                        userImage = model.userImage,
                        userName = model.userName,
                        subscribers = model.subscribers,
                        title = model.title,
                        viewsCount = model.viewsCount,
                        dateAdded = model.addingDate,
                        likes = model.likes,
                        likesByMe = model.likesByMe,
                        videoUrl = model.videoUrl,
                        previewUrl = model.previewUrl,
                        ownerId = model.ownerId
                    )
                }
            )
        }
    }
}