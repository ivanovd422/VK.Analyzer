package com.lab422.vkanalyzer.utils.command

import android.content.Context
import androidx.fragment.app.Fragment
import com.lab422.vkanalyzer.ui.friends.FriendsFragment
import com.lab422.vkanalyzer.ui.photosNear.PhotosNearFragment
import com.lab422.vkanalyzer.ui.settings.SettingsFragment

class FriendsItemCommand : FragmentCommand {
    override fun execute(context: Context): Fragment = FriendsFragment.newInstance()
}

class PhotosNearItemCommand : FragmentCommand {
    override fun execute(context: Context): Fragment = PhotosNearFragment.newInstance()
}

class SettingsItemCommand : FragmentCommand {
    override fun execute(context: Context): Fragment = SettingsFragment.newInstance()
}
