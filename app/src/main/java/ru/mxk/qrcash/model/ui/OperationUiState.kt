package ru.mxk.qrcash.model.ui

import ru.mxk.qrcash.model.OperationType
import java.math.BigDecimal

data class OperationUiState(
    val amount: BigDecimal?,
    val orderId: String?,
    val type: OperationType?
)