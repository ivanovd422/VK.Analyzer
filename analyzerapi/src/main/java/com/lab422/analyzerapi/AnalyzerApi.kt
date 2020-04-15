package com.lab422.analyzerapi

import com.lab422.analyzerapi.models.mutualFriendsList.MutualFriendsList
import com.lab422.analyzerapi.models.friendsList.FriendsResponse
import com.lab422.analyzerapi.models.users.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface AnalyzerApi {

    @GET("friends.getMutual")
    suspend fun getMutualFriends(
        @Query("source_uid") sourceUid: String,
        @Query("target_uid") targetUid: String
    ): MutualFriendsList

    @GET("friends.get")
    suspend fun getFriendsList(
        @Query("fields") fields: String = "photo_100",
        @Query("lang") lang: String = "ru"
    ): FriendsResponse

    @GET("users.get")
    suspend fun getUsersByIds(
        @Query("user_ids") listUsersIds: String
    ): UserResponse
}