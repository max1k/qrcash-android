package ru.mxk.qrcash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ru.mxk.qrcash.ui.QRCashApp
import ru.mxk.qrcash.ui.theme.QRCashTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QRCashTheme {
                QRCashApp()
            }
        }
    }
}