package com.mobiledeveloper.vktube

import android.content.Context
import android.util.Log
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.platform.app.InstrumentationRegistry
import com.mobiledeveloper.vktube.data.videos.VideosRepository
import com.mobiledeveloper.vktube.ui.common.cell.VideoCellGroupInfo
import com.mobiledeveloper.vktube.ui.screens.feed.models.VideoCellModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class VideoHistoryRepositoryTest {

    val TEST_TAG = "Test_tag"

    lateinit var instrumentationContext: Context

    @Before
    fun setup() {

    }

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun before() {
        hiltRule.inject()
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @After
    fun after() {
        repo.clearVideos()
    }

    @Inject
    lateinit var repo: VideosRepository

    @Test
    fun testSaveAndLoadVideo() = runBlocking{
        Log.d(TEST_TAG, "testSaveAndLoadVideo")
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
                subscribers = 2
            ),
            groupOrder = 1
        )

        repo.saveVideo(video)

        var videoLoaded: VideoCellModel? = null

        launch {
            videoLoaded = repo.reviewedVideos.first().first().toVideoCellModel()
        }


        while (videoLoaded == null) {
            delay(100)
            repo.getVideo(1)
        }

        assert(videoLoaded == video)
    }

    @Test
    fun testLoadAllVideos() = runBlocking{
        repo.clearVideos()

        val video1 = VideoCellModel(
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
                subscribers = 2
            ),
            groupOrder = 1
        )

        val video2 = VideoCellModel(
            videoId = 2,
            title = "2",
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
                subscribers = 2
            ),
            groupOrder = 1
        )

        repo.saveVideos(listOf(video1,video2))

        var videoLoaded: List<VideoCellModel>? = null

        launch {
            videoLoaded = repo.reviewedVideos.first().map { it.toVideoCellModel() }
        }

        while (videoLoaded == null) {
            delay(100)
            repo.getAllVideos()
        }

        assert(videoLoaded?.size == 2)
    }

}
