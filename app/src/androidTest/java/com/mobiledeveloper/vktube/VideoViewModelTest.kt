package com.mobiledeveloper.vktube

import android.content.Context
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mobiledeveloper.vktube.data.video.VideosRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.mobiledeveloper.vktube.ui.screens.video.VideoViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import org.robolectric.annotation.Config
import javax.inject.Inject


@HiltAndroidTest
class VideoRepositoryTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        Log.d("Test_tag", "Before")
        hiltRule.inject()
        //context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Inject
    lateinit var repo :VideosRepository

    @Test
    fun test1() {
        Log.d("Test_tag", repo.toString())
        assert(true)
    }

}
