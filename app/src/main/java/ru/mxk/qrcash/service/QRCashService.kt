package ru.mxk.qrcash.service

import retrofit2.Response
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
import ru.mxk.qrcash.repository.QRCashRepository
import ru.mxk.qrcash.service.converter.convertToHeadersMap

class QRCashService(private val qrCashRepository: QRCashRepository) {

    suspend fun start(sessionData: SessionData): Response<AccountSummary> {
        return qrCashRepository.start(sessionData.convertToHeadersMap())
    }

    suspend fun createOperation(
        operationRequest: OperationRequest,
        sessionData: SessionData
    ): Response<OperationResponse> {
        return qrCashRepository.createOperation(operationRequest, sessionData.convertToHeadersMap())
    }

    suspend fun atmCodeCheck(
        atmCodeRequest: AtmCodeRequest,
        sessionData: SessionData
    ): Response<AtmCodeResponse> {
        return qrCashRepository.atmCodeCheck(atmCodeRequest, sessionData.convertToHeadersMap())
    }

    suspend fun withdrawalConfirm(
        request: WithdrawalConfirmationRequest,
        sessionData: SessionData
    ): Response<WithdrawalConfirmationResponse> {
        return qrCashRepository.withdrawalConfirm(request, sessionData.convertToHeadersMap())
    }

    suspend fun otpCodeCheck(
        otpCodeRequest: OtpCodeRequest,
        sessionData: SessionData
    ): Response<OtpCodeResponse> {
        return qrCashRepository.otpCodeCheck(otpCodeRequest, sessionData.convertToHeadersMap())
    }
}