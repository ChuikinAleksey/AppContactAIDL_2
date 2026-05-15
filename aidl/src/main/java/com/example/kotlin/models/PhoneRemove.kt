package com.example.aidl.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhoneRemove(
    val deletedCount: Int,
    val message: String
) : Parcelable