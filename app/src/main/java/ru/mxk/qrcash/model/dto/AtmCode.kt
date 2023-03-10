package ru.mxk.qrcash.model.dto

import java.math.BigDecimal

data class AtmCodeRequest (
    val orderId: String,
    val code: String

)

data class AtmCodeResponse (
    val success: Boolean,
    val commission: BigDecimal?,
    val messageCode: ResponseMessageCode?,
    val attemptsRemain: Int?
)
