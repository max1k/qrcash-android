package ru.mxk.qrcash.ui.withdraw

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mxk.qrcash.R


@Composable
fun WithdrawalScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.cash_withdrawal),
            fontSize = 22.sp,
            modifier = Modifier
                .padding(bottom = 32.dp)
        )

        Text(
            text = stringResource(id = R.string.withdrawal_card),
            fontSize = 16.sp,
            color = colorResource(id = R.color.secondary_font)
        )
    }

}

@Preview
@Composable
fun WithdrawalScreenPreview() {
    WithdrawalScreen()
}