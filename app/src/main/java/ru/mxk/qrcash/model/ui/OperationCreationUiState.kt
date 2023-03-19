package ru.mxk.qrcash.model.ui

import ru.mxk.qrcash.model.Card
import ru.mxk.qrcash.model.Operation
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.ui.enumeration.CreateScreenStatus
import java.math.BigDecimal

data class OperationCreationUiState(
    val selectedCard: Card?,
    val cardList: List<Card>?,
    val status: CreateScreenStatus = CreateScreenStatus.INITIALIZING,
    val amount: BigDecimal?,
    val operation: Operation?,
    val type: OperationType?
)