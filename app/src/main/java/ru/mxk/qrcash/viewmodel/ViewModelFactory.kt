package ru.mxk.qrcash.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mxk.qrcash.App


class ViewModelFactory(private val app: App) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            OperationViewModel::class.java -> OperationViewModel(app.qrCashService) as T
            else -> throw IllegalArgumentException("Unexpected class ${modelClass.simpleName}")
        }
    }
}