package com.amel.faerntourism


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.amel.faerntourism.data.WorkManagerFaernRepository
import com.amel.faerntourism.service.createFcmNotificationChannel
import com.amel.faerntourism.ui.AuthViewModel
import com.amel.faerntourism.ui.PermissionsViewModel
import com.amel.faerntourism.ui.UpdateViewModel
import com.amel.faerntourism.ui.theme.FaernTourismTheme
import dagger.hilt.android.AndroidEntryPoint
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import javax.inject.Inject

@AndroidEntryPoint
class FaernActivity : ComponentActivity() {

    @Inject
    lateinit var workerRepository: WorkManagerFaernRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authViewModel by viewModels<AuthViewModel>()
        val updateViewModel by viewModels<UpdateViewModel>()

        workerRepository.enqueuePeriodicWork()

        createFcmNotificationChannel(this)

        setContent {
            FaernTourismTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()

                    val factory = rememberPermissionsControllerFactory()
                    val controller = remember(factory) { factory.createPermissionsController() }

                    BindEffect(controller)

                    val permissionsViewModel = viewModel { PermissionsViewModel(controller) }

                    permissionsViewModel.provideOrRequestRecordAllPermissions()

                    FaernNavHost(
                        navController,
                        authViewModel,
                        updateViewModel,
                        permissionsViewModel
                    )
                }
            }
        }
    }
}
