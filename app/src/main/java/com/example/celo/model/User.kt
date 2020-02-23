package com.example.celo.model

import android.os.Parcel
import android.os.Parcelable

class User() : Parcelable {

    var title: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var gender: String? = null
    var dateOfBirth: String? = null
    var email: String? = null
    var phone: String? = null
    var thumbnail: String? = null
    var largeImage: String? = null


    constructor(parcel: Parcel) : this() {
        title = parcel.readString()
        firstName = parcel.readString()
        lastName = parcel.readString()
        gender = parcel.readString()
        dateOfBirth = parcel.readString()
        email = parcel.readString()
        phone = parcel.readString()
        thumbnail = parcel.readString()
        largeImage = parcel.readString()
    }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(gender)
        parcel.writeString(dateOfBirth)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(thumbnail)
        parcel.writeString(largeImage)
    }
    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }
        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
