package com.amel.faerntourism.ui

import android.os.Build
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import kotlinx.coroutines.launch

class PermissionsViewModel(
    private val controller: PermissionsController
) : ViewModel() {

    var permissionsMap = mutableStateMapOf<Permission, PermissionState>()
        private set

    private val allPermissions = listOfNotNull(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) Permission.REMOTE_NOTIFICATION else null,
        Permission.COARSE_LOCATION,
        Permission.LOCATION
    )

    init {
        viewModelScope.launch {
            try {
                allPermissions.forEach { permission ->
                    permissionsMap[permission] = controller.getPermissionState(permission)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun provideOrRequestRecordAllPermissions() {
        allPermissions.forEach { permission -> provideOrRequestRecordPermission(permission) }
    }

    private fun provideOrRequestRecordPermission(permission: Permission) {
        viewModelScope.launch {
            try {
                controller.providePermission(permission)
                permissionsMap[permission] = PermissionState.Granted
            } catch (e: DeniedAlwaysException) {
                permissionsMap[permission] = PermissionState.DeniedAlways
            } catch (e: DeniedException) {
                permissionsMap[permission] = PermissionState.Denied
            } catch (e: RequestCanceledException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
