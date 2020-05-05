package com.lab422.vkanalyzer.utils.analytics

object TrackerConstants {
    const val EVENT_FIRST_LAUNCH = "First launch"
    const val EVENT_LAUNCH = "Launch"
    const val EVENT_AUTH_BY_VK_CLICKED = "Auth by vk clicked"
    const val EVENT_AUTH_BY_VK_SUCCESS = "Auth by vk success"
    const val EVENT_AUTH_BY_VK_FAILED = "Auth by vk failed"
    const val EVENT_AUTH_BY_VK_CANCELED = "Auth by vk canceled"
    const val EVENT_GET_USER_FROM_FRIEND_LIST = "Get user from friend list"
    const val EVENT_ON_SEARCH_CLICKED = "On search clicked"
    const val EVENT_OPEN_BY_LINK = "Open user by link"
    const val EVENT_FAILED_LOAD_USERS_ID = "Failed load users id"
    const val EVENT_FAILED_LOAD_MUTUAL_FRIENDS = "Failed load mutual friends"
    const val EVENT_SUCCESS_LOAD_MUTUAL_FRIENDS = "Success load mutual friends"

    // Photos Near
    const val EVENT_NEARBY_PHOTOS_LOADED = "Nearby photos loaded"
    const val EVENT_CURRENT_COORDINATES_RECEIVED = "Current coordinates received"

    // Settings
    const val EVENT_SHARE_APP_CLICKED = "Share app clicked"
    const val EVENT_SUPPORT_NEED_CLICKED = "Support need clicked"

    // Full screen photo
    const val EVENT_FULL_SCREEN_PHOTO_ERROR = "Full screen loading photo error"
}