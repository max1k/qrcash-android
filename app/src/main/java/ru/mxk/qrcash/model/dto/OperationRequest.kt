package ru.mxk.qrcash.model.dto

import com.google.gson.annotations.SerializedName
import ru.mxk.qrcash.model.OperationType
import java.math.BigDecimal

interface OperationRequest {
    val atmId: String
    val operationType: OperationType
    val publicId: String
}

data class WithdrawalOperationRequest(
    @SerializedName("ATMNUM")
    override val atmId: String,
    val amount: BigDecimal,
    override val publicId: String,
    val commission: BigDecimal
): OperationRequest {
    override val operationType: OperationType = OperationType.WITHDRAW
}

data class DepositOperationRequest(
    override val atmId: String,
    override val publicId: String
): OperationRequest {
    override val operationType: OperationType = OperationType.DEPOSIT
}

data class OperationResponse(
    val orderId: String?,
    val success: Boolean
)