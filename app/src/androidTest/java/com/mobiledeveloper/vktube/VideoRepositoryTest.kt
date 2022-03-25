package com.mobiledeveloper.vktube

import android.util.Log
import com.mobiledeveloper.vktube.data.cache.InMemoryCache
import com.mobiledeveloper.vktube.data.clubs.ClubsRepository
import com.mobiledeveloper.vktube.data.video.VideosRepository
import com.mobiledeveloper.vktube.ui.screens.video.VideoDB
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.merge
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

            val videos1 = videoRepository.fetchVideos(clubs = clubs, count = 4, frame = 0)
            printId(videos1,4, 0)
            InMemoryCache.loadedVideos.addAll(videos1)

            val videos2 = videoRepository.fetchVideos(clubs = clubs, count = 4, frame = 1)
            InMemoryCache.loadedVideos.addAll(videos2)

            InMemoryCache.loadedVideos.sortedBy { it.addingDate }.reversed()

            printId(InMemoryCache.loadedVideos,4, 1)

            size = InMemoryCache.loadedVideos.size
        }
        assert(size>0)
    }

    private fun <T> merge(first: List<T>, second: List<T>): List<T> {
        val list: MutableList<T> = ArrayList()
        list.addAll(first)
        list.addAll(second)
        return list
    }

    fun printId(list:List<VideoDB>, count:Int, frame:Int){
        var i = 0
        list.forEach {
            if (it.position == count*(frame+1) - 1) {
                Log.d("Test_tag", "ind=$i")
                return
            }
            i++
        }
    }
}
