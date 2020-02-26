package com.lab422.vkanalyzer.ui.mutualFriends.model

import android.os.Parcel
import android.os.Parcelable

data class MutualFriendsModel(
    val firstId: Long,
    val secondId: Long
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readLong()
    )

    companion object CREATOR : Parcelable.Creator<MutualFriendsModel> {
        override fun createFromParcel(parcel: Parcel): MutualFriendsModel {
            return MutualFriendsModel(parcel)
        }

        override fun newArray(size: Int): Array<MutualFriendsModel?> {
            return arrayOfNulls(size)
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(firstId)
        parcel.writeLong(secondId)
    }

    override fun describeContents(): Int {
        return 0
    }
}