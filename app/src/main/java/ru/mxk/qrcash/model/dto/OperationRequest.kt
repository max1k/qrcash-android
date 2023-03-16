package ru.mxk.qrcash.model.dto

import com.google.gson.annotations.SerializedName
import ru.mxk.qrcash.model.OperationType
import java.math.BigDecimal

data class OperationRequest(
    @SerializedName("ATMNUM")
    val atmId: String,
    val operationType: OperationType,
    val publicId: String,
    val amount: BigDecimal?,
    val commission: BigDecimal?
) {
    companion object {
        fun ofWithdraw(
            atmId: String,
            amount: BigDecimal,
            publicId: String,
            commission: BigDecimal
        ): OperationRequest
        {
            return OperationRequest(atmId, OperationType.WITHDRAW, publicId, amount, commission)
        }

        fun ofDeposit(
            atmId: String,
            publicId: String
        ): OperationRequest
        {
            return OperationRequest(atmId, OperationType.WITHDRAW, publicId, null, null)
        }
    }
}

data class OperationResponse(
    val orderId: String?,
    val success: Boolean
)