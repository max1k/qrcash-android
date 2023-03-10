package ru.mxk.qrcash.repository

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import ru.mxk.qrcash.model.dto.AccountSummary
import ru.mxk.qrcash.model.dto.AtmCodeRequest
import ru.mxk.qrcash.model.dto.AtmCodeResponse
import ru.mxk.qrcash.model.dto.OperationRequest
import ru.mxk.qrcash.model.dto.OperationResponse
import ru.mxk.qrcash.model.dto.OtpCodeRequest
import ru.mxk.qrcash.model.dto.OtpCodeResponse
import ru.mxk.qrcash.model.dto.WithdrawalConfirmationRequest
import ru.mxk.qrcash.model.dto.WithdrawalConfirmationResponse

interface QRCashRepository {

    @GET("start")
    suspend fun start(@HeaderMap convertToHeadersMap: Map<String, String>): Response<AccountSummary>

    @POST("create")
    suspend fun createOperation(
        @Body operationRequest: OperationRequest,
        @HeaderMap sessionHeaders: Map<String, String>
    ): Response<OperationResponse>

    @POST("atm-code/check")
    suspend fun atmCodeCheck(
        @Body atmCodeRequest: AtmCodeRequest,
        @HeaderMap sessionHeaders: Map<String, String>
    ): Response<AtmCodeResponse>

    @POST("cash-withdrawal/confirm")
    suspend fun withdrawalConfirm(
        @Body request: WithdrawalConfirmationRequest,
        @HeaderMap sessionHeaders: Map<String, String>
    ): Response<WithdrawalConfirmationResponse>

    @POST("otp-code/check")
    suspend fun otpCodeCheck(
        @Body otpCodeRequest: OtpCodeRequest,
        @HeaderMap sessionHeaders: Map<String, String>
    ): Response<OtpCodeResponse>

}