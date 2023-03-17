package ru.mxk.qrcash.model.dto.request

import androidx.arch.core.util.Function

data class RequestResult<T>(val status: ResultStatus, private val value: T?) {

    val data: T get() = value ?: throw NoSuchElementException("Value is not set")

    fun isPresent(): Boolean = value != null
    fun isEmpty(): Boolean = !isPresent()

    fun <K> map(mapper: Function<T, K>): RequestResult<K> {
        return if (status == ResultStatus.DONE) {
            ofSuccess(mapper.apply(value))
        } else {
            RequestResult(status, null)
        }
    }

    companion object {
        fun <T> ofPending(): RequestResult<T> {
            return RequestResult(ResultStatus.PENDING, null)
        }

        fun <T> ofFail(): RequestResult<T> {
            return RequestResult(ResultStatus.FAIL, null)
        }

        fun <T> ofSuccess(data: T): RequestResult<T> {
            return RequestResult(ResultStatus.DONE, data)
        }
    }
}

enum class ResultStatus {
    PENDING, DONE, FAIL
}