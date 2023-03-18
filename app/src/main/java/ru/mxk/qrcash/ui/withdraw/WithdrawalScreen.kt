package ru.mxk.qrcash.ui.withdraw

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import ru.mxk.qrcash.model.CreateScreenStatus
import ru.mxk.qrcash.model.ui.OperationCreationUiState
import ru.mxk.qrcash.ui.common.LoadingScreen
import ru.mxk.qrcash.ui.error.OperationErrorScreen
import ru.mxk.qrcash.ui.preview.PREVIEW_CARD_LIST_UI_STATE
import java.math.BigDecimal


@Composable
fun WithdrawalScreen(
    creationUiState: OperationCreationUiState,
    onAmountChange: (BigDecimal) -> Unit,
    onCreateOperation: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    when(creationUiState.status) {
        CreateScreenStatus.INITIALIZING,
        CreateScreenStatus.CARD_LIST_LOADING -> LoadingScreen()

        CreateScreenStatus.CARD_LIST_LOADED,
        CreateScreenStatus.OPERATION_CREATION,
        CreateScreenStatus.OPERATION_CREATED -> ShowDetails(
            creationUiState = creationUiState,
            onAmountChange = onAmountChange,
            onCreateOperation = onCreateOperation,
            onNavigateBack = onNavigateBack,
            modifier = modifier,
        )

        CreateScreenStatus.ERROR -> OperationErrorScreen(
            type = creationUiState.type,
            onDoneClick = onNavigateBack
        )
    }
}

@Composable
private fun ShowDetails(
    creationUiState: OperationCreationUiState,
    onAmountChange: (BigDecimal) -> Unit,
    onCreateOperation: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(all = 16.dp)
    ) {
        Column(modifier = Modifier.weight(10F)) {

            HeaderSection(navigateBack = onNavigateBack)

            CardSelectionSection(creationUiState)

            AmountInputSection(creationUiState, onAmountChange)

            CallUsSection()
        }

        ContinueButtonSection(creationUiState, onCreateOperation)
    }
}

@Composable
private fun ContinueButtonSection(
    creationUiState: OperationCreationUiState,
    onNextClick: () -> Unit
) {
    Button(
        enabled = allParamsAreCorrect(creationUiState),
        onClick = onNextClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (creationUiState.status == CreateScreenStatus.OPERATION_CREATION) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier
                    .size(16.dp)
            )
        } else {
            Text(
                text = stringResource(id = R.string.continue_caption),
                fontSize = 16.sp
            )
        }
    }
}

fun allParamsAreCorrect(creationUiState: OperationCreationUiState): Boolean {
    val currentAmount = creationUiState.amount ?: return false
    val cardBalance = creationUiState.selectedCard?.balance ?: return false

    return cardBalance >= currentAmount
}

@Composable
private fun HeaderSection(navigateBack: () -> Unit) {
    Column(horizontalAlignment = Alignment.Start) {

        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back),
            modifier = Modifier
                .padding(bottom = 32.dp)
                .clickable { navigateBack() }
        )

        Text(
            text = stringResource(id = R.string.cash_withdrawal),
            fontSize = 22.sp,
            modifier = Modifier
                .padding(bottom = 32.dp)
        )
    }
}

@Composable
private fun CardSelectionSection(operationCreationUiState: OperationCreationUiState) {
    Text(
        text = stringResource(id = R.string.withdrawal_card),
        fontSize = 16.sp,
        color = colorResource(id = R.color.secondary_font),
        modifier = Modifier.padding(bottom = 8.dp)
    )

    if (operationCreationUiState.selectedCard != null) {
        SelectedCardSection(operationCreationUiState.selectedCard)
    }
}

@Composable
private fun CallUsSection() {
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

@Composable
private fun AmountInputSection(
    creationUiState: OperationCreationUiState,
    onAmountChange: (BigDecimal) -> Unit
) {
    val focusManager = LocalFocusManager.current
    TextField(
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { focusManager.clearFocus() }
        ),
        label = {
            Text(
                text = stringResource(id = R.string.amount),
                fontSize = 16.sp,
                color = colorResource(id = R.color.secondary_font),
            )
        },
        value = creationUiState.amount?.toString() ?: "",
        onValueChange = { onAmountChange(convertToBigDecimal(it)) },
        colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
        modifier = Modifier
            .padding(top = 52.dp)
            .fillMaxWidth()
    )

    AmountButtons(onAmountChange, creationUiState)
}

@Composable
private fun AmountButtons(
    onAmountChange: (BigDecimal) -> Unit,
    creationUiState: OperationCreationUiState
) {
    Row(
        modifier = Modifier.padding(bottom = 32.dp)
    ) {
        OutlinedBorderText(
            text = "100₽",
            onClick = { onAmountChange(addAmount(BigDecimal(100), creationUiState)) },
            modifier = Modifier.padding(top = 8.dp, end = 8.dp)
        )
        OutlinedBorderText(
            text = "500₽",
            onClick = { onAmountChange(addAmount(BigDecimal(500), creationUiState)) },
            modifier = Modifier.padding(top = 8.dp, end = 8.dp)
        )
        OutlinedBorderText(
            text = "1 000₽",
            onClick = { onAmountChange(addAmount(BigDecimal(1000), creationUiState)) },
            modifier = Modifier.padding(top = 8.dp, end = 8.dp)
        )
        OutlinedBorderText(
            text = "5 000₽",
            onClick = { onAmountChange(addAmount(BigDecimal(5000), creationUiState)) },
            modifier = Modifier.padding(top = 8.dp, end = 8.dp)
        )
    }
}

private fun addAmount(amount: BigDecimal, creationUiState: OperationCreationUiState): BigDecimal {
    val currentAmount = creationUiState.amount ?: BigDecimal.ZERO
    return currentAmount + amount
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
private fun SelectedCardSection(card: Card) {
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
        creationUiState = PREVIEW_CARD_LIST_UI_STATE,
        onAmountChange = {},
        onCreateOperation = {},
        onNavigateBack = {}
    )
}