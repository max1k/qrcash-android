package ru.mxk.qrcash.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.mxk.qrcash.model.OperationWithCommission

@Composable
fun CompletedOperationScreen(
    operation: OperationWithCommission,
    onDone: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {

    }
}