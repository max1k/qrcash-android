package ru.mxk.qrcash.ui.withdraw

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mxk.qrcash.R
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.OperationWithCommission
import ru.mxk.qrcash.model.SessionData
import ru.mxk.qrcash.model.ui.ConfirmUiState
import ru.mxk.qrcash.model.ui.enumeration.ViewModelStatus
import ru.mxk.qrcash.ui.common.CallUsSection
import ru.mxk.qrcash.ui.common.DetailsSection
import ru.mxk.qrcash.ui.common.SelectedCardSection
import ru.mxk.qrcash.ui.error.OperationErrorScreen
import ru.mxk.qrcash.ui.preview.PREVIEW_OPERATION

@Composable
fun WithdrawalConfirmScreen(
    operation: OperationWithCommission,
    sessionData: SessionData,
    uiState: ConfirmUiState,
    onConfirm: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    when(uiState.status) {
        ViewModelStatus.INIT,
        ViewModelStatus.LOADING,
        ViewModelStatus.DONE -> ConfirmContent(
            operation = operation,
            sessionData = sessionData,
            uiState = uiState,
            onConfirm = onConfirm,
            onClose = onClose,
            modifier = modifier
        )

        ViewModelStatus.ERROR -> OperationErrorScreen(
            type = OperationType.WITHDRAW,
            onDoneClick = onClose,
            modifier = modifier
        )
    }
}

@Composable
private fun ConfirmContent(
    operation: OperationWithCommission,
    sessionData: SessionData,
    uiState: ConfirmUiState,
    onConfirm: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(all = 16.dp)
    ) {

        Column(
            horizontalAlignment = Alignment.Start,
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .padding(bottom = 32.dp)
                    .clickable { onClose() }
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.confirm_withdrawal_operation),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 32.dp)
            )

            SelectedCardSection(
                card = operation.operation.card,
                operationType = operation.operation.type,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            mapOf(
                stringResource(id = R.string.withdraw_amount) to operation.operation.amount.toString(),
                stringResource(id = R.string.commission) to operation.commission.toString(),
                stringResource(id = R.string.atm_number) to sessionData.atmId
            )
                .forEach { DetailsSection(it.key, it.value) }

            CallUsSection(
                modifier = Modifier
                    .weight(1F)
                    .padding(bottom = 48.dp)
            )

            Button(
                onClick = onConfirm,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.status == ViewModelStatus.LOADING) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier
                            .size(16.dp)
                    )
                } else {
                    Text(
                        text = stringResource(id = R.string.confirm),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun WithdrawalConfirmScreenPreview() {
    WithdrawalConfirmScreen(
        operation = PREVIEW_OPERATION,
        sessionData = SessionData("111", "222", "333"),
        uiState = ConfirmUiState(),
        onConfirm = {},
        onClose = {}
    )
}