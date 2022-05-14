package com.mobiledeveloper.vktube

import com.mobiledeveloper.vktube.data.videos.VideosRepository
import com.mobiledeveloper.vktube.ui.common.cell.VideoCellGroupInfo
import com.mobiledeveloper.vktube.ui.screens.feed.models.VideoCellModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import javax.inject.Inject

@HiltAndroidTest
class VideoHistoryRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltRule.inject()
    }

    @After
    fun after() {
        repo.clearVideos()
    }

    @Inject
    lateinit var repo: VideosRepository

    @Test
    fun test1() {
        repo.clearVideos()
        val video = VideoCellModel(
            videoId = 1,
            title = "1",
            previewUrl = "this.previewUrl",
            viewsCount = 10,
            dateAdded = 10000000,
            likes = 0,
            likesByMe = false,
            videoUrl = "this.videoUrl",
            ownerId = 1,
            groupInfo = VideoCellGroupInfo(
                id = 1,
                userImage = "this.groupUserImage",
                userName = "this.groupUserName",
                subscribers = 2),
            groupOrder = 1)

        repo.saveVideo(video)

        val videoLoaded: VideoCellModel
        runBlocking { videoLoaded = repo.getVideo(1).first().toVideoCellModel() }

        assert(videoLoaded == video)
    }

}
