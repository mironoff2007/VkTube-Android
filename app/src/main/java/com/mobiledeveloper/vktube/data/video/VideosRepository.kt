package com.mobiledeveloper.vktube.data.video

import android.util.Log
import com.mobiledeveloper.vktube.data.cache.InMemoryCache
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.requests.VKRequest
import com.vk.dto.common.id.abs
import com.vk.dto.common.id.unaryMinus
import com.vk.sdk.api.groups.dto.GroupsGetObjectExtendedResponse
import com.vk.sdk.api.video.VideoService
import com.vk.sdk.api.video.dto.VideoGetResponse
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class VideosRepository @Inject constructor(private val videosDao : VideosDatabase) {

   fun saveToDB(videos: List<VideoDB>){
      videosDao.videosDao().insertAllVideoData(videos)
   }

   suspend fun getFrame(clubs: GroupsGetObjectExtendedResponse, count: Int, frame:Int): List<VideoDB> {
      InMemoryCache.loadedVideos.addAll(fetchVideos(clubs, count, frame))
      InMemoryCache.loadedVideos.sortWith(Comparator { video1, video2 -> video2.addingDate - video1.addingDate })
      val id = getFrameEndId(InMemoryCache.loadedVideos,count, frame )
      Log.e("Test_tag", "frame ends at $id")
      return InMemoryCache.loadedVideos.subList(0,id+1)
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
         var i=count*(frame+1)
         videoItems.addAll(response.items.sortedBy { it.addingDate }.map { videoFull ->
            val group = clubs.items.firstOrNull { it.id.abs() == videoFull.ownerId?.abs() }
            i--
            VideoDB(
               videoId = videoFull.id ?: 0,
               userName = group?.name.orEmpty(),
               userImage = group?.photo100.orEmpty(),
               subscribers = group?.membersCount ?: 0,
               frame = frame,
               position = i,
               addingDate = videoFull.addingDate ?:0,
               title = videoFull.title ?: "",
               viewsCount = videoFull.views ?: 0,
               likes = videoFull.likes?.count ?: 0,
               likesByMe = videoFull.likes?.userLikes?.value == 1,
               videoUrl = videoFull.player.orEmpty(),
               previewUrl = videoFull.image?.reversed()?.firstOrNull()?.url ?: "",
               ownerId = videoFull.ownerId?.value ?: 0
            )
         })
      }

      videoItems.sortBy { it.addingDate }
      return videoItems.reversed()
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

   fun getFrameEndId(list:List<VideoDB>, count:Int, frame:Int): Int{
      var i = 0
      list.forEach {
         if (it.position == count*(frame+1) - 1) {
            return i
         }
         i++
      }
      return 0
   }
}