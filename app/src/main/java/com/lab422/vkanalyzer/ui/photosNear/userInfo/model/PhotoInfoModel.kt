package com.lab422.vkanalyzer.ui.photosNear.userInfo.model

import android.os.Parcel
import android.os.Parcelable

data class PhotoInfoModel(
    val userId: String,
    val lat: Double?,
    val long: Double?,
    val clickedPhotoUrl :String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userId)
        parcel.writeDouble(lat ?: 0.0)
        parcel.writeDouble(long ?: 0.0)
        parcel.writeString(clickedPhotoUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoInfoModel> {
        override fun createFromParcel(parcel: Parcel): PhotoInfoModel {
            return PhotoInfoModel(parcel)
        }

        override fun newArray(size: Int): Array<PhotoInfoModel?> {
            return arrayOfNulls(size)
        }
    }
}