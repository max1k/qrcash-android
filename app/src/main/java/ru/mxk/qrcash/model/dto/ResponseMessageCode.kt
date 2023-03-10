package ru.mxk.qrcash.model.dto

enum class ResponseMessageCode(val errorCode: Int) {
    INVALID_ATM_CODE(40),
    ALL_ATTEMPTS_ATM_CODE_EXHAUSTED(41),
    INVALID_OTP_CODE(42),
    ALL_ATTEMPTS_OTP_CODE_EXHAUSTED(43),
    SERVER_ERROR(50)
}