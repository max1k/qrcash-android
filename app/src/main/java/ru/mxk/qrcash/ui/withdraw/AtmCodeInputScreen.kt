package ru.mxk.qrcash.ui.withdraw

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mxk.qrcash.R
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.ui.AtmCodeUiState
import ru.mxk.qrcash.model.ui.enumeration.CodeCheckStatus
import ru.mxk.qrcash.ui.error.OperationErrorScreen


@Composable
fun AtmCodeInputScreen(
    uiState: AtmCodeUiState,
    onCodeChange: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    when(uiState.status) {
        CodeCheckStatus.INIT,
        CodeCheckStatus.LOADING,
        CodeCheckStatus.INVALID_CODE,
        CodeCheckStatus.DONE -> CodeInputScreen(
            uiState = uiState,
            onCodeChange = onCodeChange,
            onClose = onClose,
            modifier = modifier
        )

        CodeCheckStatus.ERROR -> OperationErrorScreen(
            type = OperationType.WITHDRAW,
            onDoneClick = onClose
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CodeInputScreen(
    uiState: AtmCodeUiState,
    onCodeChange: (String) -> Unit,
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
                text = stringResource(id = R.string.input_atm_code),
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 32.dp)
            )

            val codeIsInvalid = uiState.status == CodeCheckStatus.INVALID_CODE
            val textFieldColor =
                if (codeIsInvalid)  {
                    TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Red
                    )
                } else {
                    TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                    )
                }

            val focusRequester = FocusRequester()
            val keyboardController = LocalSoftwareKeyboardController.current

            TextField(
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                label = {
                    Text(
                        text = stringResource(id = R.string.code),
                        fontSize = 16.sp,
                        color = colorResource(id = R.color.secondary_font),
                    )
                },
                value = uiState.code,
                onValueChange = { onCodeChange(it) },
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                colors = textFieldColor,
                modifier = Modifier
                    .padding(top = 52.dp, bottom = 8.dp, start = 64.dp, end = 64.dp)
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (it.isFocused) {
                            keyboardController?.show()
                        }
                    }
            )
            if (codeIsInvalid) {
                Text(
                    text = stringResource(id = R.string.invalid_code, uiState.attempts ?: 0),
                    color = Color.Red
                )
            }

            DisposableEffect(Unit) {
                focusRequester.requestFocus()
                onDispose { }
            }
        }
    }
}

@Preview
@Composable
fun AtmScreenPreview() {
    AtmCodeInputScreen(
        uiState = AtmCodeUiState("", 2, CodeCheckStatus.INVALID_CODE),
        onCodeChange = {},
        onClose = {},
    )
}
