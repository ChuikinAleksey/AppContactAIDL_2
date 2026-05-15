package com.example.kotlin.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.lang.RuntimeException

@Parcelize
class AidlException(
    private val errorMessage: String?,
    private val errorCode: Int = RUNTIME_EXCEPTION
) : Parcelable {

    companion object {
        const val RUNTIME_EXCEPTION = 1000
        //const val ARITHMETIC_EXCEPTION = 1001
        // TODO другие коды ошибок...
        const val PERMISSION_DENIED = 2001
        const val READ_CONTACTS_FAILED = 2002
        const val WRITE_CONTACTS_FAILED = 2003
        const val DELETE_OPERATION_FAILED = 2004
        // TODO другие коды ошибок...
    }

    // сторона клиента
    fun toException(): Exception {
        return when (errorCode) {
            RUNTIME_EXCEPTION -> RuntimeException(errorMessage)
            //ARITHMETIC_EXCEPTION -> ArithmeticException(errorMessage)
            PERMISSION_DENIED -> SecurityException(errorMessage)
            READ_CONTACTS_FAILED -> IllegalStateException(errorMessage)
            WRITE_CONTACTS_FAILED -> IllegalStateException(errorMessage)
            DELETE_OPERATION_FAILED -> RuntimeException(errorMessage)
            else -> RuntimeException(errorMessage)
        }
    }
}