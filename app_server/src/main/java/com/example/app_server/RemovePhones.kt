package com.example.app_server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.aidl.PhoneSort
import com.example.aidl.base.AidlResult
import com.example.aidl.base.AidlException
import com.example.aidl.callback.AsyncCallback
import java.util.concurrent.Executors

class PhoneRemoveService : Service() {

    private val executor = Executors.newSingleThreadExecutor()
    private lateinit var contactUtil: ContactUtil

    override fun onCreate() {
        super.onCreate()
        contactUtil = ContactUtil(applicationContext)
    }

    private val binder = object : PhoneSort.Stub() {    // ← PhoneSort.Stub()
        override fun removeContacts(callback: AsyncCallback?) {
            if (callback == null) return

            executor.execute {
                try {
                    contactUtil.checkPermissions()
                    val deletedCount = contactUtil.deduplicateContacts()

                    val result = when (deletedCount) {
                        0 -> AidlResult(
                            code = 3,
                            message = "повторяющиеся контакты не найдены",
                            deletedCount = 0
                        )
                        else -> AidlResult(
                            code = 1,
                            message = "повторяющиеся контакты удалены успешно",
                            deletedCount = deletedCount
                        )
                    }
                    callback.onSuccess(result)

                } catch (e: IllegalStateException) {
                    callback.onError(
                        AidlException(
                            e.message,
                            AidlException.PERMISSION_DENIED
                        )
                    )
                } catch (e: Exception) {
                    callback.onError(
                        AidlException(
                            e.message,
                            AidlException.DELETE_OPERATION_FAILED
                        )
                    )
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder
}