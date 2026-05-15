package com.example.aidl.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AidlResult(
    val code: Int,
    val message: String,
    val deletedCount: Int = 0
) : Parcelable