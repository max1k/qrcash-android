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
import ru.mxk.qrcash.model.dto.AtmCodeRequest
import ru.mxk.qrcash.model.ui.CodeUiState
import ru.mxk.qrcash.model.ui.enumeration.CodeCheckStatus
import ru.mxk.qrcash.service.QRCashService
import java.math.BigDecimal
import kotlin.coroutines.CoroutineContext

class AtmCodeViewModel(
    private val qrCashService: QRCashService
): ViewModel(), CoroutineScope, Statused {
    companion object {
        const val OTP_CODE_LENGTH = 4
    }

    private val _uiState = MutableStateFlow(CodeUiState(code = "", attempts = null))
    val uiState: StateFlow<CodeUiState> = _uiState.asStateFlow()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    lateinit var operation: Operation
    lateinit var commission: BigDecimal

    override fun reset() {
        _uiState.update { currentState ->
            currentState.copy(code = "", status = CodeCheckStatus.INIT)
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

        changeStatus(CodeCheckStatus.LOADING)

        launch {
            val result = qrCashService.atmCodeCheck(
                AtmCodeRequest(operation.orderId, uiState.value.code),
                sessionData
            )

            if (result.isEmpty()) {
                changeStatus(CodeCheckStatus.ERROR)
                return@launch
            }

            val response = result.data
            if (response.success) {
                changeStatus(CodeCheckStatus.DONE)
                commission = response.commission ?: BigDecimal.ZERO

                withContext(Dispatchers.Main) {
                    onCodeCheckPass()
                }
            } else {
                val remainedAttempts = response.attemptsRemain ?: 0
                if (remainedAttempts > 0) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            status = CodeCheckStatus.INVALID_CODE,
                            attempts = remainedAttempts,
                            code = ""
                        )
                    }
                } else {
                    changeStatus(CodeCheckStatus.ERROR)
                }
            }
        }
    }

    private fun changeStatus(status: CodeCheckStatus) {
        _uiState.update { currentState ->
            currentState.copy(status = status)
        }
    }

}