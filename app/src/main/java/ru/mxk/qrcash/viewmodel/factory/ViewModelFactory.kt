package ru.mxk.qrcash.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mxk.qrcash.App
import ru.mxk.qrcash.viewmodel.OperationViewModel


class ViewModelFactory(private val app: App) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            OperationViewModel::class.java -> OperationViewModel(app.qrCashService) as T
            else -> throw IllegalArgumentException("Unexpected class ${modelClass.simpleName}")
        }
    }
}