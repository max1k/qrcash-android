package ru.mxk.qrcash.model.ui

import ru.mxk.qrcash.model.Operation
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.ViewModelStatus
import java.math.BigDecimal

data class OperationUiState(
    val amount: BigDecimal?,
    val operation: Operation?,
    val type: OperationType?,
    val status: ViewModelStatus = ViewModelStatus.INITIALIZING
)