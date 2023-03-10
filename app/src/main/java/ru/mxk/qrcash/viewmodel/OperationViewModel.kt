package ru.mxk.qrcash.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import ru.mxk.qrcash.model.ui.OperationUiState
import ru.mxk.qrcash.service.QRCashService
import kotlin.coroutines.CoroutineContext

class OperationViewModel(private val qrCashService: QRCashService): ViewModel(), CoroutineScope {
    private val _uistate = MutableStateFlow(OperationUiState(""))

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO


}