package ru.mxk.qrcash.ui.withdraw

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mxk.qrcash.R
import ru.mxk.qrcash.model.ui.ConfirmUiState

@Composable
fun WithdrawalConfirmScreen(
    uiState: ConfirmUiState,
    onConfirm: () -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
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

        }
    }
}

@Preview
@Composable
fun WithdrawalConfirmScreenPreview() {
    WithdrawalConfirmScreen(
        uiState = ConfirmUiState(),
        onConfirm = {},
        onClose = {}
    )
}