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

    private var lockLoad = false

    override fun obtainEvent(viewEvent: FeedEvent) {
        Log.d("Test_tag", viewEvent.javaClass.simpleName)
        when (viewEvent) {
            FeedEvent.Loading -> { lockLoad = true }
            FeedEvent.Loaded -> { lockLoad = false }
            FeedEvent.ScreenShown -> fetchVideos()
            FeedEvent.ScrollEnd -> if (!lockLoad) fetchVideos()
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
            obtainEvent(FeedEvent.Loading)
            val userId = try {
                userRepository.fetchLocalUser().userId
            } catch (e: Exception) {
                userRepository.fetchAndSaveUser()
                userRepository.fetchLocalUser().userId
            }

            val clubs = clubsRepository.fetchClubs(userId)

            frame++
            val videos =  videosRepository.getFrame(frame = frame, count = 2, clubs = clubs)

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
            obtainEvent(FeedEvent.Loaded)
        }
    }
}