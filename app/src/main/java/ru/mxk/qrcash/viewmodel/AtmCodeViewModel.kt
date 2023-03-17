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
import ru.mxk.qrcash.model.Operation
import ru.mxk.qrcash.model.SessionData
import ru.mxk.qrcash.model.ViewModelStatus
import ru.mxk.qrcash.model.dto.AtmCodeRequest
import ru.mxk.qrcash.model.ui.AtmCodeUiState
import ru.mxk.qrcash.service.QRCashService
import kotlin.coroutines.CoroutineContext

class AtmCodeViewModel(private val qrCashService: QRCashService): ViewModel(), CoroutineScope {
    companion object {
        const val OTP_CODE_LENGTH = 4
    }

    private val _uiState = MutableStateFlow(AtmCodeUiState(code = ""))
    val uiState: StateFlow<AtmCodeUiState> = _uiState.asStateFlow()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    lateinit var operation: Operation

    fun reset() {
        _uiState.update { currentState ->
            currentState.copy(code = "", status = ViewModelStatus.INIT)
        }
    }

    fun onCodeChange(code: String) {
        _uiState.update { currentState ->
            currentState.copy(code = code)
        }
    }

    fun checkCode(sessionData: SessionData, onCodeCheckPass: () -> Unit) {
        if (uiState.value.code.length != OTP_CODE_LENGTH) {
            return
        }

        if (!uiState.value.status.canBeReprocessed) {
            return
        }

        changeStatus(ViewModelStatus.LOADING)

        launch {
            val result = qrCashService.atmCodeCheck(
                AtmCodeRequest(operation.orderId, uiState.value.code),
                sessionData
            )

            if (result.isEmpty()) {
                changeStatus(ViewModelStatus.ERROR)
                return@launch
            }

            changeStatus(ViewModelStatus.DONE)
            onCodeCheckPass()
        }
    }

    private fun changeStatus(status: ViewModelStatus) {
        _uiState.update { currentState ->
            currentState.copy(status = status)
        }
    }


}