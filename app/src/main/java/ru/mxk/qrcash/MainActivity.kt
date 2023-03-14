package ru.mxk.qrcash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import ru.mxk.qrcash.ui.QRCashApp
import ru.mxk.qrcash.ui.theme.QRCashTheme
import ru.mxk.qrcash.viewmodel.CardSelectionViewModel
import ru.mxk.qrcash.viewmodel.OperationViewModel
import ru.mxk.qrcash.viewmodel.factory.ViewModelFactory

class MainActivity : ComponentActivity() {
    private val cardSelectionViewModel: CardSelectionViewModel by viewModels { ViewModelFactory(applicationContext as App) }
    private val operationViewModel: OperationViewModel by viewModels { ViewModelFactory(applicationContext as App) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCashTheme {
                QRCashApp(
                    cardSelectionViewModel = cardSelectionViewModel,
                    operationViewModel = operationViewModel
                )
            }
        }
    }
}