package ru.mxk.qrcash.model.dto

data class OtpCodeRequest(
    val orderId: String,
    val otpCode: String
)

data class OtpCodeResponse(
    val success: Boolean,
    val messageCode: ResponseMessageCode?,
    val attemptsRemain: Int?
)