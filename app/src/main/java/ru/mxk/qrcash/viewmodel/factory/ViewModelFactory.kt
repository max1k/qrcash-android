package ru.mxk.qrcash.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.mxk.qrcash.App
import ru.mxk.qrcash.viewmodel.AtmCodeViewModel
import ru.mxk.qrcash.viewmodel.ConfirmViewModel
import ru.mxk.qrcash.viewmodel.OperationCreationViewModel


class ViewModelFactory(private val app: App) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T: ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            OperationCreationViewModel::class.java -> OperationCreationViewModel(app.qrCashService) as T
            AtmCodeViewModel::class.java -> AtmCodeViewModel(app.qrCashService) as T
            ConfirmViewModel::class.java -> ConfirmViewModel(app.qrCashService) as T
            else -> throw IllegalArgumentException("Unexpected class ${modelClass.simpleName}")
        }
    }
}