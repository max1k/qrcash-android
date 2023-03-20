package ru.mxk.qrcash.model.ui

import ru.mxk.qrcash.model.ui.enumeration.CodeCheckStatus

data class CodeUiState(
    val code: String,
    val attempts: Int?,
    val status: CodeCheckStatus = CodeCheckStatus.INIT
)