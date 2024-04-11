package ru.mxk.qrcash.ui.withdraw

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mxk.qrcash.R
import ru.mxk.qrcash.model.Card
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.ui.OperationCreationUiState
import ru.mxk.qrcash.model.ui.enumeration.CreateScreenStatus
import ru.mxk.qrcash.ui.common.CallUsSection
import ru.mxk.qrcash.ui.common.CardSelectionPopup
import ru.mxk.qrcash.ui.common.LoadingScreen
import ru.mxk.qrcash.ui.common.SelectedCardSection
import ru.mxk.qrcash.ui.error.OperationErrorScreen
import ru.mxk.qrcash.ui.preview.PREVIEW_CARD_LIST_UI_STATE
import java.math.BigDecimal


@Composable
fun WithdrawalScreen(
    creationUiState: OperationCreationUiState,
    onAmountChange: (BigDecimal) -> Unit,
    onCreateOperation: () -> Unit,
    onNavigateBack: () -> Unit,
    onCardSelectionActivated: () -> Unit,
    onCardSelectionDeactivated: () -> Unit,
    onCardSelected: (Card) -> Unit,
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
            onCardSelectionActivated = onCardSelectionActivated,
            onCardSelectionDeactivated = onCardSelectionDeactivated,
            onCardSelected = onCardSelected,
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
    onCardSelectionActivated: () -> Unit,
    onCardSelectionDeactivated: () -> Unit,
    onCardSelected: (Card) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(all = 16.dp)
    ) {
        Column(modifier = Modifier.weight(10F)) {

            HeaderSection(navigateBack = onNavigateBack)

            CardSelectionSection(creationUiState, onCardSelectionActivated)

            AmountInputSection(creationUiState, onAmountChange)

            CallUsSection(type = OperationType.WITHDRAW)
        }

        ContinueButtonSection(creationUiState, onCreateOperation)

        if (creationUiState.cardList != null) {
            CardSelectionPopup(
                visible = creationUiState.cardSelectionActivated,
                cards = creationUiState.cardList,
                onCardSelectionDeactivated = onCardSelectionDeactivated,
                onCardSelected = onCardSelected
            )
        }
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
                    .padding(vertical = 8.dp)
            )
        } else {
            Text(
                text = stringResource(id = R.string.continue_caption),
                fontSize = 16.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

private fun allParamsAreCorrect(creationUiState: OperationCreationUiState): Boolean {
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
private fun CardSelectionSection(
    operationCreationUiState: OperationCreationUiState,
    onCardSelectionActivated: () -> Unit
) {
    if (operationCreationUiState.selectedCard != null) {
        SelectedCardSection(
            card = operationCreationUiState.selectedCard,
            operationType = OperationType.WITHDRAW,
            onCardSelectionActivated = onCardSelectionActivated
        )
    }
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

@Preview
@Composable
fun WithdrawalScreenPreview() {
    WithdrawalScreen(
        creationUiState = PREVIEW_CARD_LIST_UI_STATE,
        onAmountChange = {},
        onCreateOperation = {},
        onNavigateBack = {},
        onCardSelectionActivated = {},
        onCardSelectionDeactivated = {},
        onCardSelected = {}
    )
}