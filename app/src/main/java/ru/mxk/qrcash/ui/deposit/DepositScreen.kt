package ru.mxk.qrcash.ui.deposit

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mxk.qrcash.R
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.ui.OperationCreationUiState
import ru.mxk.qrcash.model.ui.enumeration.CreateScreenStatus
import ru.mxk.qrcash.ui.common.CallUsSection
import ru.mxk.qrcash.ui.common.LoadingScreen
import ru.mxk.qrcash.ui.common.SelectedCardSection
import ru.mxk.qrcash.ui.error.OperationErrorScreen
import ru.mxk.qrcash.ui.preview.PREVIEW_CARD_LIST_UI_STATE


@Composable
fun DepositScreen(
    creationUiState: OperationCreationUiState,
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

            CardSelectionSection(
                uiState = creationUiState,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            CallUsSection(type = OperationType.DEPOSIT)
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

private fun allParamsAreCorrect(creationUiState: OperationCreationUiState): Boolean {
    return creationUiState.selectedCard != null
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
    uiState: OperationCreationUiState,
    modifier: Modifier = Modifier
) {
    if (uiState.selectedCard != null) {
        SelectedCardSection(uiState.selectedCard, OperationType.DEPOSIT, modifier)
    }
}

@Preview
@Composable
fun DepositScreenPreview() {
    DepositScreen(
        creationUiState = PREVIEW_CARD_LIST_UI_STATE,
        onCreateOperation = {},
        onNavigateBack = {}
    )
}