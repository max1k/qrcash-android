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
import ru.mxk.qrcash.model.dto.OperationRequest
import ru.mxk.qrcash.model.dto.OperationResponse
import ru.mxk.qrcash.model.ui.OperationCreationUiState
import ru.mxk.qrcash.model.ui.enumeration.CreateScreenStatus
import ru.mxk.qrcash.service.QRCashService
import java.math.BigDecimal
import kotlin.coroutines.CoroutineContext

class OperationCreationViewModel(
    private val qrCashService: QRCashService
): ViewModel(), CoroutineScope, Statused {
    private val _uiState = MutableStateFlow(OperationCreationUiState(
        selectedCard = null,
        cardList = null,
        amount = null,
        operation = null,
        type = null
    ))
    val uiState: StateFlow<OperationCreationUiState> = _uiState.asStateFlow()

    private var job = Job()
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


    override fun reset() {
        _uiState.update { currentState ->
            currentState.copy(
                cardList = null,
                selectedCard = null,
                amount = null,
                type = null,
                operation = null,
                status = CreateScreenStatus.INITIALIZING,
                cardSelectionActivated = false
            )
        }
    }

    fun requestCardList(sessionData: SessionData) {
        if (!uiState.value.status.canLoadCards) {
            return
        }

        updateStatus(CreateScreenStatus.CARD_LIST_LOADING)

        launch {
            val result = qrCashService.start(sessionData)
                .map { it.accounts.flatMap { account -> account.cards } }

            if (!result.isPresent()) {
                updateStatus(CreateScreenStatus.ERROR)
                return@launch
            }

            val cards = result.data
            _uiState.update { currentState ->
                currentState.copy(
                    cardList = cards,
                    selectedCard = cards.first(),
                    status = CreateScreenStatus.CARD_LIST_LOADED
                )
            }
        }
    }

    fun createOperation(
        sessionData: SessionData,
        card: Card,
        onCreated: () -> Unit
    ) {
        if (!uiState.value.status.canCreateOperation) {
            return
        }

        updateStatus(CreateScreenStatus.OPERATION_CREATION)

        launch {
            val result = qrCashService.createOperation(
                getOperationRequest(card, sessionData),
                sessionData
            )

            if (result.isEmpty()) {
                updateStatus(CreateScreenStatus.ERROR)
                return@launch
            }

            val data = result.data
            if (data.success) {
                handleOnCreated(data, card, onCreated)
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
                status = CreateScreenStatus.OPERATION_CREATED,
                operation = getOperation(orderId, card)
            )
        }

        withContext(Dispatchers.Main) {
            onCreated()
        }
    }

    private fun getOperation(orderId: String, card: Card): Operation =
        Operation(card, uiState.value.type!!, orderId, uiState.value.amount ?: BigDecimal(555))

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

    private fun updateStatus(status: CreateScreenStatus) {
        _uiState.update { currentState ->
            currentState.copy(status = status)
        }
    }

    fun activateCardSelection(active: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(cardSelectionActivated = active)
        }
    }

    fun selectCard(card: Card) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCard = card,
                cardSelectionActivated = false
            )
        }
    }

}