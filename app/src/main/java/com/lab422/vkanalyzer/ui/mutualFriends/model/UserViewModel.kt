package com.lab422.vkanalyzer.ui.mutualFriends.model

import com.lab422.vkanalyzer.ui.base.Diffable

data class UserViewModel(
    val id: Long,
    val userName: String,
    val isOnline: Boolean,
    val photoUrl: String?
) : Diffable {
    override fun isSame(same: Diffable): Boolean {
        if (same !is UserViewModel) {
            return false
        }
        return same.id == id
    }

    override fun isContentSame(same: Diffable): Boolean {
        if (same !is UserViewModel) {
            return false
        }
        return same.userName == userName &&
            same.isOnline == isOnline &&
            same.id == id &&
            same.photoUrl == photoUrl
    }
}
