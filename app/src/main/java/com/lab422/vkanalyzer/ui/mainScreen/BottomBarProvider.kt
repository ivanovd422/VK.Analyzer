package com.lab422.vkanalyzer.ui.mainScreen

import com.lab422.vkanalyzer.R
import com.lab422.vkanalyzer.ui.friends.FriendsFragment
import com.lab422.vkanalyzer.ui.photosNear.PhotosNearFragment
import com.lab422.vkanalyzer.ui.settings.SettingsFragment
import com.lab422.vkanalyzer.utils.command.FriendsItemCommand
import com.lab422.vkanalyzer.utils.command.PhotosNearItemCommand
import com.lab422.vkanalyzer.utils.command.SettingsItemCommand



class BottomBarProvider {

    companion object {
        const val FRIENDS_ID = 0
        const val PHOTOS_NEAR_ID = 1
        const val SETTINGS_MENU_ID = 2
    }

    fun getBarItems(): List<BarItem> {
        return mutableListOf(
            BarItem(
                R.string.bottom_menu_friends,
                R.string.bottom_menu_friends,
                R.drawable.ic_mutual_friends_icon,
                FRIENDS_ID,
                FriendsItemCommand(),
                FriendsFragment.TAG
            ),
            BarItem(
                R.string.bottom_menu_photos_near,
                R.string.bottom_menu_photos_near,
                R.drawable.ic_near_photo,
                PHOTOS_NEAR_ID,
                PhotosNearItemCommand(),
                PhotosNearFragment.TAG
            ),
            BarItem(
                R.string.bottom_menu_settings,
                R.string.bottom_menu_settings,
                R.drawable.ic_bar_support,
                SETTINGS_MENU_ID,
                SettingsItemCommand(),
                SettingsFragment.TAG
            )
        )
    }
}