package ru.mxk.qrcash.model.dto

data class WithdrawalConfirmationRequest(
    val orderId: String
)

data class WithdrawalConfirmationResponse(
    val success: Boolean,
    val messageCode: ResponseMessageCode?,
    val needOtp: Boolean?,
    val countNum: Int?,
    val timeoutResend: Int?
)