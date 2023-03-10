package ru.mxk.qrcash.model

import java.math.BigDecimal

data class Operation(
    val card: Card,
    val type: OperationType,
    val orderId: String,
    val amount: BigDecimal
)
