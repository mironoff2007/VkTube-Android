package com.mobiledeveloper.vktube.data.video

import android.util.Log
import com.mobiledeveloper.vktube.ui.screens.video.VideoDB
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.requests.VKRequest
import com.vk.dto.common.id.abs
import com.vk.dto.common.id.unaryMinus
import com.vk.sdk.api.groups.dto.GroupsGetObjectExtendedResponse
import com.vk.sdk.api.video.VideoService
import com.vk.sdk.api.video.dto.VideoGetResponse
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class VideosRepository @Inject constructor(private val videosDao : VideosDatabase) {

   fun saveToDB(videos: List<VideoDB>){
      videosDao.videosDao().insertAllVideoData(videos)
   }

   suspend fun fetchVideos(clubs: GroupsGetObjectExtendedResponse, count: Int, frame:Int): List<VideoDB> {
      val requests = clubs.items.map {
         VideoService().videoGet(count = count, ownerId = -it.id, offset = frame*count)
      }

      val listResponse = mutableListOf<VideoGetResponse>()
      requests.forEach {
         try {
            listResponse.add(fetchVideo(it))
         } catch (e: java.lang.Exception) {
            println(e.localizedMessage)
         }
      }

      val videoItems = mutableListOf<VideoDB>()
      listResponse.forEach { response ->
         var i=-1
         videoItems.addAll(response.items.map { videoFull ->
            val group = clubs.items.firstOrNull { it.id.abs() == videoFull.ownerId?.abs() }
            i++
            VideoDB(
               videoId = videoFull.id ?: 0,
               userName = group?.name.orEmpty(),
               userImage = group?.photo100.orEmpty(),
               subscribers = group?.membersCount ?: 0,
               frame = frame,
               position = i,
               addingDate = videoFull.addingDate,
               name = videoFull.title ?: ""
            )
         })
      }

      videoItems.sortBy { it.addingDate }
      val rev = videoItems.reversed()
      var i=0
      rev.forEach { if(it.position==count) Log.d("Test_tag", "ind=$i")
         i++}
      return rev
   }

   private suspend fun fetchVideo(videoGetRequest: VKRequest<VideoGetResponse>): VideoGetResponse {
      return suspendCoroutine { continuation ->
         VK.execute(request = videoGetRequest,
            object : VKApiCallback<VideoGetResponse> {
               override fun fail(error: Exception) {
                  continuation.resumeWithException(error)
               }

               override fun success(result: VideoGetResponse) {
                  continuation.resume(result)
               }
            })
      }
   }
}