package ru.mxk.qrcash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mxk.qrcash.R
import ru.mxk.qrcash.model.OperationWithCommission
import ru.mxk.qrcash.ui.common.CallUsSection
import ru.mxk.qrcash.ui.common.DetailsSection
import ru.mxk.qrcash.ui.common.SelectedCardSection
import ru.mxk.qrcash.ui.preview.PREVIEW_OPERATION

@Composable
fun CompletedOperationScreen(
    operation: OperationWithCommission,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(color = colorResource(id = R.color.light_gray))
    ) {
        Image(
            painter = painterResource(id = R.drawable.done),
            contentDescription = stringResource(id = R.string.cash_withdrawal),
            modifier = Modifier.padding(
                top = 36.dp,
                bottom = 36.dp
            )
        )

        Text(
            text = operation.operation.type.description,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Text(text = "10 Июля 2021, 20:31",
            fontSize = 16.sp,
            color = colorResource(id = R.color.secondary_font),
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        Text(
            text = "${operation.operation.amount} ₽",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(bottom = 24.dp)
        )

        Column(
            modifier = Modifier
                .clip(shape = RoundedCornerShape(20.dp))
                .background(color = Color.White)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SelectedCardSection(
                card = operation.operation.card,
                operationType = operation.operation.type,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            mapOf(
                stringResource(id = R.string.withdraw_amount) to "${operation.operation.amount} ₽",
                stringResource(id = R.string.commission) to "${operation.commission} ₽",
            )
                .forEach { DetailsSection(it.key, it.value) }

            CallUsSection(
                modifier = Modifier
                    .weight(1F)
                    .padding(bottom = 48.dp)
            )

            Button(
                onClick = onDone,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.done),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun CompletedOperationScreenPreview() {
    CompletedOperationScreen(
        operation = PREVIEW_OPERATION,
        onDone = {}
    )
}