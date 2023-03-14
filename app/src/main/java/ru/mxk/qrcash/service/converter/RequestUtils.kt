package ru.mxk.qrcash.service.converter

import retrofit2.Response
import ru.mxk.qrcash.model.SessionData
import java.util.UUID


fun SessionData.convertToHeadersMap(): Map<String, String> = mapOf(
    HeaderConst.UNC_HEADER_NAME to uncId,
    HeaderConst.MDM_HEADER_NAME to mdmId,
    HeaderConst.CHANNEL_HEADER_NAME to HeaderConst.MOBILE_BANK_HEADER,
    HeaderConst.USER_SESSION_HEADER_NAME to UUID.randomUUID().toString()
)

fun <T> Response<T>.getData(): T {
    val body = body()
    if (isSuccessful && body != null) {
        return body
    }

    throw FailedResponseException()
}

class FailedResponseException : Exception()
