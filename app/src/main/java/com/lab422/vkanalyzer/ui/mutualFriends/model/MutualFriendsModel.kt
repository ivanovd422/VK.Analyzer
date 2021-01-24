package com.lab422.vkanalyzer.ui.mutualFriends.model

import android.os.Parcel
import android.os.Parcelable

data class MutualFriendsModel(
    val firstId: String,
    val secondId: String
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
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
        parcel.writeString(firstId)
        parcel.writeString(secondId)
    }

    override fun describeContents(): Int {
        return 0
    }
}
