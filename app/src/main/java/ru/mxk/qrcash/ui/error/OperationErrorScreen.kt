package ru.mxk.qrcash.ui.error


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.mxk.qrcash.R
import ru.mxk.qrcash.model.OperationType

@Composable
fun OperationErrorScreen(
    type: OperationType?,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
            .padding(all = 16.dp)
    )
    {
        val errorText =
            type?.errorDescription ?: stringResource(id = R.string.something_went_wrong)

        Image(
            painter = painterResource(id = R.drawable.error),
            contentDescription = errorText,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Text(
            text = errorText,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(id = R.string.working_on_it),
            modifier = Modifier.padding(bottom = 256.dp)
        )
        
        Button(
            onClick = onDoneClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.accepted),
                fontSize = 16.sp
            )
        }
    }

}

@Preview
@Composable
fun OperationErrorScreenPreview() {
    OperationErrorScreen(
        type = OperationType.WITHDRAW,
        onDoneClick = {}
    )
}
