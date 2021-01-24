package com.lab422.vkanalyzer.ui.mutualFriends.list.adapter

import com.lab422.vkanalyzer.ui.base.Diffable
import com.lab422.vkanalyzer.ui.base.Rawable

enum class FriendsListType : Rawable, Diffable {
    Friends,
    SelectableFriends;

    override val rawValue: Int = this.ordinal

    override fun isContentSame(same: Diffable): Boolean {
        if (same !is FriendsListType) {
            return false
        }
        return same.rawValue == rawValue
    }

    override fun isSame(same: Diffable): Boolean {
        if (same !is FriendsListType) {
            return false
        }

        return (same.rawValue == rawValue)
    }
}
