package com.amel.faerntourism.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.RuStoreAppUpdateManager
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability

class UpdateViewModel : ViewModel() {

    private lateinit var ruStoreAppUpdateManager: RuStoreAppUpdateManager

    private val _events = MutableSharedFlow<UpdateEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    private val installStateUpdateListener = InstallStateUpdateListener { installState ->
        when (installState.installStatus) {
            InstallStatus.DOWNLOADED -> {
                _events.tryEmit(UpdateEvent.UpdateCompleted)
            }

            InstallStatus.DOWNLOADING -> {
                Log.d(
                    TAG,
                    "Downloading update: ${installState.bytesDownloaded} / ${installState.totalBytesToDownload}"
                )
            }

            InstallStatus.FAILED -> {
                Log.e(TAG, "Downloading error")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ruStoreAppUpdateManager.unregisterListener(installStateUpdateListener)
    }

    fun init(context: Context) {
        ruStoreAppUpdateManager = RuStoreAppUpdateManagerFactory.create(context)
        checkForUpdates()
    }

    fun completeUpdateRequested() {
        ruStoreAppUpdateManager.completeUpdate(
            AppUpdateOptions.Builder().appUpdateType(AppUpdateType.FLEXIBLE).build()
        )
            .addOnFailureListener { throwable ->
                Log.e(TAG, "completeUpdate error", throwable)
            }
    }

    private fun checkForUpdates() {
        ruStoreAppUpdateManager
            .getAppUpdateInfo()
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability == UpdateAvailability.UPDATE_AVAILABLE) {
                    ruStoreAppUpdateManager.registerListener(installStateUpdateListener)
                    ruStoreAppUpdateManager
                        .startUpdateFlow(appUpdateInfo, AppUpdateOptions.Builder().build())
                        .addOnSuccessListener { resultCode ->
                            if (resultCode == Activity.RESULT_CANCELED) {
                                Log.d(TAG, "User canceled update flow.")
                            }
                        }
                        .addOnFailureListener { throwable ->
                            Log.e(TAG, "startUpdateFlow error", throwable)
                        }
                } else {
                    Log.d(TAG, "No updates available.")
                }
            }
            .addOnFailureListener { throwable ->
                Log.e(TAG, "getAppUpdateInfo error", throwable)
            }
    }

    companion object {
        private const val TAG = "RuStoreUpdate"
    }
}

/**
 * Simple sealed class for sending update-related events to UI.
 */
sealed class UpdateEvent {
    data object UpdateCompleted : UpdateEvent()
}
