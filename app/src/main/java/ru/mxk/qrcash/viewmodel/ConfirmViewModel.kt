package ru.mxk.qrcash.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.mxk.qrcash.model.OperationWithCommission
import ru.mxk.qrcash.model.SessionData
import ru.mxk.qrcash.model.dto.WithdrawalConfirmationRequest
import ru.mxk.qrcash.model.ui.ConfirmUiState
import ru.mxk.qrcash.model.ui.enumeration.ViewModelStatus
import ru.mxk.qrcash.service.QRCashService
import kotlin.coroutines.CoroutineContext

class ConfirmViewModel(
    private val qrCashService: QRCashService
): ViewModel(), CoroutineScope, Statused {
    private val _uiState = MutableStateFlow(ConfirmUiState())
    val uiState: StateFlow<ConfirmUiState> = _uiState.asStateFlow()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    lateinit var operation: OperationWithCommission

    override fun reset() {
        _uiState.update { currentState ->
            currentState.copy(status = ViewModelStatus.INIT)
        }
    }

    fun confirm(sessionData: SessionData, onConfirmed: () -> Unit) {
        if (!uiState.value.status.canBeReprocessed) {
            return
        }

        changeStatus(ViewModelStatus.LOADING)

        launch {
            val result = qrCashService.withdrawalConfirm(
                WithdrawalConfirmationRequest(operation.operation.orderId),
                sessionData
            )

            if (result.isEmpty()) {
                changeStatus(ViewModelStatus.ERROR)
                return@launch
            }

            changeStatus(ViewModelStatus.DONE)
            onConfirmed()
        }
    }

    private fun changeStatus(status: ViewModelStatus) {
        _uiState.update { currentState ->
            currentState.copy(status = status)
        }
    }

}