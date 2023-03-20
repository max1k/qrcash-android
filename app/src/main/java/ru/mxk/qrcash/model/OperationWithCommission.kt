package ru.mxk.qrcash.model

import java.math.BigDecimal

data class OperationWithCommission(
    val operation: Operation,
    val commission: BigDecimal
)