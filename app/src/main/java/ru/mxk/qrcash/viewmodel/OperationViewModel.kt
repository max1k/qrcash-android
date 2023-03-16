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
import ru.mxk.qrcash.model.Card
import ru.mxk.qrcash.model.Operation
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.SessionData
import ru.mxk.qrcash.model.ViewModelStatus
import ru.mxk.qrcash.model.dto.OperationRequest
import ru.mxk.qrcash.model.dto.OperationResponse
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

    fun createOperation(
        sessionData: SessionData,
        card: Card,
        onCreated: () -> Unit,
        onError: () -> Unit
    ) {
        if (!uiState.value.status.canBeReprocessed) {
            return
        }

        updateStatus(ViewModelStatus.LOADING)

        launch {
            val result = qrCashService.createOperation(
                getOperationRequest(card, sessionData),
                sessionData
            )

            if (result.isPresent()) {
                val data = result.data
                if (data.success) {
                    handleOnCreated(data, card, onCreated)
                }
            } else {
                handleError(onError)
            }
        }
    }

    private suspend fun handleOnCreated(
        responseBody: OperationResponse,
        card: Card,
        onCreated: () -> Unit
    ) {
        val orderId = responseBody.orderId ?: throw IllegalStateException("OrderId is not present")

        _uiState.update { currentState ->
            currentState.copy(
                status = ViewModelStatus.DONE,
                operation = getOperation(orderId, card)
            )
        }

        withContext(Dispatchers.Main) {
            onCreated()
        }
    }

    private suspend fun handleError(onError: () -> Unit) {
        updateStatus(ViewModelStatus.ERROR)

        withContext(Dispatchers.Main) {
            onError()
        }
    }

    private fun getOperation(orderId: String, card: Card): Operation =
        Operation(card, uiState.value.type!!, orderId, uiState.value.amount!!)

    private fun getOperationRequest(card: Card, sessionData: SessionData): OperationRequest {
        val operationType = uiState.value.type ?: throw IllegalStateException("Operation type is not set")

        return when(operationType) {
            OperationType.WITHDRAW -> OperationRequest.ofWithdraw(
                sessionData.atmId,
                uiState.value.amount ?: throw IllegalStateException("Amount is not set"),
                card.publicId,
                BigDecimal.ZERO
            )

            OperationType.DEPOSIT -> OperationRequest.ofDeposit(sessionData.atmId, card.publicId)
        }
    }

    private fun updateStatus(status: ViewModelStatus) {
        _uiState.update { currentState ->
            currentState.copy(status = status)
        }
    }

    fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                amount = null,
                type = null,
                operation = null,
                status = ViewModelStatus.INITIALIZING
            )
        }
    }
}