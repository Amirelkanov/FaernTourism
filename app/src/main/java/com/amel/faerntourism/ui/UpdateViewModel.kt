package com.amel.faerntourism.ui

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.rustore.sdk.appupdate.errors.InstallErrorCode.Companion.ERROR_EXTERNAL_SOURCE_DENIED
import ru.rustore.sdk.appupdate.errors.RuStoreInstallException
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.RuStoreAppUpdateManager
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val ruStoreAppUpdateManager: RuStoreAppUpdateManager
) : ViewModel() {
    private val _events = MutableSharedFlow<UpdateEvent>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    private val installStateUpdateListener = InstallStateUpdateListener { installState ->
        when (installState.installStatus) {
            InstallStatus.DOWNLOADED -> {
                Log.d(TAG, "Update has been downloaded!")
                _events.tryEmit(UpdateEvent.UpdateDownloaded)
            }

            InstallStatus.DOWNLOADING -> {
                Log.d(
                    TAG,
                    "Downloading update: ${installState.bytesDownloaded} / ${installState.totalBytesToDownload}"
                )
            }

            InstallStatus.FAILED -> {
                Log.w(TAG, "Downloading error")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        ruStoreAppUpdateManager.unregisterListener(installStateUpdateListener)
    }

    init {
        checkForUpdates()
    }

    fun completeUpdateRequested() {
        ruStoreAppUpdateManager.completeUpdate(
            AppUpdateOptions.Builder().appUpdateType(AppUpdateType.FLEXIBLE).build()
        )
            .addOnFailureListener { throwable ->
                Log.w(TAG, "completeUpdate error", throwable)
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
                            if (resultCode == Activity.RESULT_OK) {
                                _events.tryEmit(UpdateEvent.UpdateDownloaded)
                            }
                            if (resultCode == Activity.RESULT_CANCELED) {
                                Log.d(TAG, "User canceled update flow.")
                            }
                        }
                        .addOnFailureListener { throwable ->
                            if (throwable is RuStoreInstallException && throwable.code == ERROR_EXTERNAL_SOURCE_DENIED) {
                                _events.tryEmit(UpdateEvent.UpdateDenied)
                            }
                            Log.w(TAG, "startUpdateFlow error", throwable)
                        }
                } else {
                    Log.d(TAG, "No updates available.")
                }
            }
            .addOnFailureListener { throwable ->
                Log.w(TAG, "getAppUpdateInfo error", throwable)
            }
    }

    companion object {
        private const val TAG = "RuStoreUpdate"
    }
}

/**
 * Simple sealed class for sending update-related events to UI.
 */
sealed interface UpdateEvent {
    data object UpdateDownloaded : UpdateEvent
    data object UpdateDenied : UpdateEvent
}
