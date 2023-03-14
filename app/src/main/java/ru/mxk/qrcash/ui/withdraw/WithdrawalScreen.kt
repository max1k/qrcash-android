package ru.mxk.qrcash.ui.withdraw

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import ru.mxk.qrcash.R
import ru.mxk.qrcash.model.Card
import ru.mxk.qrcash.model.CardType
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.ui.CardListUiState
import ru.mxk.qrcash.model.ui.OperationUiState
import ru.mxk.qrcash.ui.common.LoadingScreen
import java.math.BigDecimal


@Composable
fun WithdrawalScreen(
    cardListUiState: CardListUiState,
    operationUiState: OperationUiState,
    onAmountChange: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!cardListUiState.isLoading) {
        ShowDetails(
            cardListUiState = cardListUiState,
            operationUiState = operationUiState,
            onAmountChange = onAmountChange,
            modifier = modifier)
    } else {
        LoadingScreen()
    }
}

@Composable
private fun ShowDetails(
    cardListUiState: CardListUiState,
    operationUiState: OperationUiState,
    onAmountChange: (BigDecimal) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(all = 16.dp)
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
            color = colorResource(id = R.color.secondary_font),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (cardListUiState.selectedCard != null) {
            SelectedCard(cardListUiState.selectedCard)
        }

        OutlinedTextField(
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = true,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            label = {
                Text(
                    text = stringResource(id = R.string.amount),
                    fontSize = 16.sp,
                    color = colorResource(id = R.color.secondary_font),
                )
            },
            value = operationUiState.amount?.toString() ?: "",
            onValueChange = { onAmountChange(convertToBigDecimal(it)) },
            modifier = Modifier
                .padding(top = 52.dp)
                .fillMaxWidth()
        )

        AmountButtons(onAmountChange, operationUiState)

        val context = LocalContext.current
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.withdrawal_troubleshooting))
                addStyle(
                    style = SpanStyle(
                        color = Color(0xff64B5F6),
                        textDecoration = TextDecoration.Underline
                    ), start = 76, end = 91
                )
            },
            fontSize = 16.sp,
            modifier = Modifier
                .clickable {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:88001234567"))
                    ContextCompat.startActivity(context, intent, null)
                }
        )

    }
}

@Composable
private fun AmountButtons(
    onAmountChange: (BigDecimal) -> Unit,
    operationUiState: OperationUiState
) {
    Row(
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        OutlinedBorderText(
            text = "100₽",
            onClick = { onAmountChange(addAmount(BigDecimal(100), operationUiState)) },
            modifier = Modifier.padding(top = 8.dp, end = 8.dp)
        )
        OutlinedBorderText(
            text = "500₽",
            onClick = { onAmountChange(addAmount(BigDecimal(500), operationUiState)) },
            modifier = Modifier.padding(top = 8.dp, end = 8.dp)
        )
        OutlinedBorderText(
            text = "1 000₽",
            onClick = { onAmountChange(addAmount(BigDecimal(1000), operationUiState)) },
            modifier = Modifier.padding(top = 8.dp, end = 8.dp)
        )
        OutlinedBorderText(
            text = "5 000₽",
            onClick = { onAmountChange(addAmount(BigDecimal(5000), operationUiState)) },
            modifier = Modifier.padding(top = 8.dp, end = 8.dp)
        )
    }
}

private fun addAmount(amount: BigDecimal, operationUiState: OperationUiState): BigDecimal {
    val currentAmount = operationUiState.amount ?: BigDecimal.ZERO
    return currentAmount.plus(amount)
}

@Composable
private fun OutlinedBorderText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clickable { onClick() }
    ) {
        Text(
            text = text,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.secondary_font),
                    shape = RoundedCornerShape(10.dp),
                )
                .padding(all = 12.dp)
        )
    }
}

fun convertToBigDecimal(amount: String): BigDecimal {
    return try {
        BigDecimal(amount)
    } catch (exception: Exception) {
        BigDecimal.ZERO
    }
}

@Composable
private fun SelectedCard(card: Card) {
    Column {
        Row {
            Column(
                modifier = Modifier
                    .weight(10F)
            ) {
                Row {
                    Text(
                        text = card.name,
                        fontSize = 16.sp
                    )

                    Text(
                        text = " • " + card.shortNumber,
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.secondary_font),
                    )
                }

                Text(
                    text = card.balance.toString() + "₽",
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.light_blue),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.card),
                contentDescription = card.paymentSystem,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(width = 24.dp, height = 18.dp)
                    .align(alignment = Alignment.CenterVertically)
            )
        }

        Divider(
            color = colorResource(id = R.color.secondary_font),
            thickness = 1.dp
        )
    }
}

@Preview
@Composable
fun WithdrawalScreenPreview() {
    WithdrawalScreen(
        cardListUiState = CardListUiState(
            selectedCard = Card(
                publicId = "123",
                name = "Цифровая карта",
                maskedNumber = "220024******0923",
                shortNumber = "0923",
                balance = BigDecimal(55000),
                paymentSystem = "MIR",
                systemPlacingName ="WWWF",
                cardType = CardType.PREPAID_CARD
            ),
            cardList = listOf()
        ),
        operationUiState = OperationUiState(BigDecimal.TEN, "", OperationType.WITHDRAW),
        onAmountChange = {}
    )
}