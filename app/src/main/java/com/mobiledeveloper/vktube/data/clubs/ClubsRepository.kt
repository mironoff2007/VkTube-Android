package com.mobiledeveloper.vktube.data.clubs

import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.requests.VKRequest
import com.vk.dto.common.id.UserId
import com.vk.dto.common.id.abs
import com.vk.dto.common.id.unaryMinus
import com.vk.sdk.api.groups.GroupsService
import com.vk.sdk.api.groups.dto.GroupsFields
import com.vk.sdk.api.groups.dto.GroupsGetObjectExtendedResponse
import com.vk.sdk.api.video.VideoService
import com.vk.sdk.api.video.dto.VideoGetResponse
import com.vk.sdk.api.video.dto.VideoVideoFull
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

data class VideoDataModel(
    val item: VideoVideoFull,
    val userImage: String,
    val userName: String,
    val subscribers: Int
)

fun VideoVideoFull.mapToVideoDataModel(
    userImage: String,
    userName: String,
    subscribers: Int
) = VideoDataModel(
    item = this,
    userImage = userImage,
    userName = userName,
    subscribers = subscribers
)

class ClubsRepository @Inject constructor() {

    suspend fun fetchClubs(userId: Long): GroupsGetObjectExtendedResponse {
        return suspendCoroutine { continuation ->
            VK.execute(
                GroupsService().groupsGetExtended(userId = UserId(userId), count = 100, fields = listOf(GroupsFields.MEMBERS_COUNT)),
                object : VKApiCallback<GroupsGetObjectExtendedResponse> {
                    override fun fail(error: Exception) {
                        continuation.resumeWithException(error)
                    }

                    override fun success(result: GroupsGetObjectExtendedResponse) {
                        continuation.resume(result)
                    }
                })
        }
    }
}