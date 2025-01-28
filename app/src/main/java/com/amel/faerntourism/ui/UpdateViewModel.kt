package com.amel.faerntourism.ui

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.RuStoreAppUpdateManager
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val appUpdateManager: RuStoreAppUpdateManager
) : ViewModel() {

    private val _events = MutableSharedFlow<UpdateEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    init {
        checkForUpdates()
    }

    fun completeUpdateRequested() {
        appUpdateManager.completeUpdate(
            AppUpdateOptions.Builder().appUpdateType(AppUpdateType.FLEXIBLE).build()
        ).addOnFailureListener { throwable ->
            Log.e(TAG, "completeUpdate error", throwable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Stop receiving update installation state callbacks
        appUpdateManager.unregisterListener(installStateUpdateListener)
    }

    private fun checkForUpdates() {
        appUpdateManager.getAppUpdateInfo()
            .addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability == UpdateAvailability.UPDATE_AVAILABLE) {
                    appUpdateManager.registerListener(installStateUpdateListener)

                    appUpdateManager
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


    private val installStateUpdateListener = InstallStateUpdateListener { installState ->
        when (installState.installStatus) {
            InstallStatus.DOWNLOADED -> {
                // Notify UI that update has finished downloading and is ready to be installed
                _events.tryEmit(UpdateEvent.UpdateDownloaded)
            }

            InstallStatus.DOWNLOADING -> {
                Log.d(
                    TAG,
                    "Downloading update: ${installState.bytesDownloaded} / ${installState.totalBytesToDownload}"
                )
            }

            InstallStatus.FAILED -> {
                Log.e(TAG, "Update download failed")
            }
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
    data object UpdateDownloaded : UpdateEvent()
}
