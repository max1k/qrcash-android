package ru.mxk.qrcash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import ru.mxk.qrcash.ui.QRCashApp
import ru.mxk.qrcash.ui.theme.QRCashTheme
import ru.mxk.qrcash.viewmodel.AtmCodeViewModel
import ru.mxk.qrcash.viewmodel.ConfirmViewModel
import ru.mxk.qrcash.viewmodel.OperationCreationViewModel
import ru.mxk.qrcash.viewmodel.OperationViewModel
import ru.mxk.qrcash.viewmodel.OtpCodeViewModel
import ru.mxk.qrcash.viewmodel.factory.ViewModelFactory

class MainActivity : ComponentActivity() {
    private val operationCreationViewModel: OperationCreationViewModel by viewModels { ViewModelFactory(applicationContext as App) }
    private val atmCodeViewModel: AtmCodeViewModel by viewModels { ViewModelFactory(applicationContext as App) }
    private val confirmViewModel: ConfirmViewModel by viewModels { ViewModelFactory(applicationContext as App) }
    private val otpCodeViewModel: OtpCodeViewModel by viewModels { ViewModelFactory(applicationContext as App) }
    private val operationViewModel: OperationViewModel by viewModels { ViewModelFactory(applicationContext as App) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCashTheme {
                QRCashApp(
                    creationViewModel = operationCreationViewModel,
                    atmCodeViewModel =  atmCodeViewModel,
                    confirmViewModel = confirmViewModel,
                    otpCodeViewModel = otpCodeViewModel,
                    operationViewModel = operationViewModel,
                )
            }
        }
    }
}