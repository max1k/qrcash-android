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
import kotlinx.coroutines.withContext
import ru.mxk.qrcash.model.Operation
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

    var otpCodeLength: Int? = null

    override fun reset() {
        _uiState.update { currentState ->
            currentState.copy(status = ViewModelStatus.INIT)
        }
    }

    fun confirm(
        operation: Operation,
        sessionData: SessionData,
        onOtpCheck: () -> Unit,
        onOperationCompleted: () -> Unit
    ) {
        if (!uiState.value.status.canBeReprocessed) {
            return
        }

        changeStatus(ViewModelStatus.LOADING)

        launch {
            val result = qrCashService.withdrawalConfirm(
                WithdrawalConfirmationRequest(operation.orderId),
                sessionData
            )

            if (result.isEmpty() || !result.data.success) {
                changeStatus(ViewModelStatus.ERROR)
                return@launch
            }

            val response = result.data
            otpCodeLength = response.countNum
            changeStatus(ViewModelStatus.DONE)

            withContext(Dispatchers.Main) {
                if (response.needOtp!!) {
                    onOtpCheck()
                } else {
                    onOperationCompleted()
                }
            }
        }
    }

    private fun changeStatus(status: ViewModelStatus) {
        _uiState.update { currentState ->
            currentState.copy(status = status)
        }
    }

}