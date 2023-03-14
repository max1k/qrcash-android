package ru.mxk.qrcash.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.ui.OperationUiState
import ru.mxk.qrcash.service.QRCashService
import java.math.BigDecimal
import kotlin.coroutines.CoroutineContext

class OperationViewModel(private val qrCashService: QRCashService): ViewModel(), CoroutineScope {
    private val _uiState = MutableStateFlow(OperationUiState(null, null, null))
    val uiState: StateFlow<OperationUiState> = _uiState.asStateFlow()

    private val job = Job()
    override val coroutineContext: CoroutineContext = job + Dispatchers.IO

    fun setAmount(amount: BigDecimal) {
        _uiState.update { currentState ->
            currentState.copy(
                amount = amount
            )
        }
    }

    fun setOperationType(operationType: OperationType) {
        _uiState.update { currentState ->
            currentState.copy(
                type = operationType
            )
        }
    }
}