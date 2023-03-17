package ru.mxk.qrcash.service

import android.util.Log
import ru.mxk.qrcash.model.SessionData
import ru.mxk.qrcash.model.dto.AccountSummary
import ru.mxk.qrcash.model.dto.AtmCodeRequest
import ru.mxk.qrcash.model.dto.AtmCodeResponse
import ru.mxk.qrcash.model.dto.OperationRequest
import ru.mxk.qrcash.model.dto.OperationResponse
import ru.mxk.qrcash.model.dto.OtpCodeRequest
import ru.mxk.qrcash.model.dto.OtpCodeResponse
import ru.mxk.qrcash.model.dto.WithdrawalConfirmationRequest
import ru.mxk.qrcash.model.dto.WithdrawalConfirmationResponse
import ru.mxk.qrcash.model.dto.request.RequestResult
import ru.mxk.qrcash.repository.QRCashRepository
import ru.mxk.qrcash.service.converter.convertToHeadersMap
import ru.mxk.qrcash.service.converter.getData

class QRCashService(private val qrCashRepository: QRCashRepository) {

    suspend fun start(sessionData: SessionData): RequestResult<AccountSummary> {
        return try {
            RequestResult.ofSuccess(
                qrCashRepository
                    .start(sessionData.convertToHeadersMap())
                    .getData()
            )
        } catch (exception: Exception) {
            Log.e("create", exception.message, exception)
            RequestResult.ofFail()
        }
    }

    suspend fun createOperation(
        operationRequest: OperationRequest,
        sessionData: SessionData
    ): RequestResult<OperationResponse> {
        return try {
            RequestResult.ofSuccess(
                qrCashRepository
                    .createOperation(operationRequest, sessionData.convertToHeadersMap())
                    .getData()
            )
        } catch (exception: Exception) {
            Log.e("create", exception.message, exception)
            RequestResult.ofFail()
        }
    }

    suspend fun atmCodeCheck(
        atmCodeRequest: AtmCodeRequest,
        sessionData: SessionData
    ): RequestResult<AtmCodeResponse> {
        return try {
            RequestResult.ofSuccess(
                qrCashRepository
                    .atmCodeCheck(atmCodeRequest, sessionData.convertToHeadersMap())
                    .getData()
            )
        } catch (exception: Exception) {
            Log.e("atm-check", exception.message, exception)
            RequestResult.ofFail()
        }
    }

    suspend fun withdrawalConfirm(
        request: WithdrawalConfirmationRequest,
        sessionData: SessionData
    ): RequestResult<WithdrawalConfirmationResponse> {
        return try {
            RequestResult.ofSuccess(
                qrCashRepository
                    .withdrawalConfirm(request, sessionData.convertToHeadersMap())
                    .getData()
            )
        } catch (exception: Exception) {
            Log.e("withdrawal-confirm", exception.message, exception)
            RequestResult.ofFail()
        }
    }

    suspend fun otpCodeCheck(
        otpCodeRequest: OtpCodeRequest,
        sessionData: SessionData
    ): RequestResult<OtpCodeResponse> {
        return try {
            RequestResult.ofSuccess(
                qrCashRepository
                    .otpCodeCheck(otpCodeRequest, sessionData.convertToHeadersMap())
                    .getData()
            )
        } catch (exception: Exception) {
            Log.e("otp-check", exception.message, exception)
            RequestResult.ofFail()
        }
    }
}