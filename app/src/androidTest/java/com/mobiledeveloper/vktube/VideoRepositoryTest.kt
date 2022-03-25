package com.mobiledeveloper.vktube

import android.util.Log
import com.mobiledeveloper.vktube.data.clubs.ClubsRepository
import com.mobiledeveloper.vktube.data.video.VideosRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltAndroidTest
class VideoRepositoryTest {

    private val userId=16172565L

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        Log.d("Test_tag", "Before")
        hiltRule.inject()

    }

    @Inject
    lateinit var videoRepository: VideosRepository

    @Inject
    lateinit var clubsRepository: ClubsRepository

    @Test
    fun test1() {
        var size=0
        runBlocking {
            val clubs = clubsRepository.fetchClubs(userId)
            val videos = videoRepository.fetchVideos(clubs = clubs, count = 4, frame = 0)
            size = videos.size
        }
        assert(size>=0)
    }

}
