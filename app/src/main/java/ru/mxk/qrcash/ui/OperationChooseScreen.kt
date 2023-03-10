package ru.mxk.qrcash.ui


import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import ru.mxk.qrcash.R


@Composable
fun OperationChooseScreen(
    onWithdrawClick: () -> Unit,
    onDepositClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(all = 16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.deposit_withdraw),
            fontSize = 22.sp,
            modifier = modifier
                .padding(top = 18.dp, bottom = 24.dp)
        )

        Row(
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            OperationButton(
                label = stringResource(id = R.string.withdraw_cash),
                icon = painterResource(id = R.drawable.withdraw),
                onClick = onWithdrawClick,
                modifier = Modifier.weight(weight = 1.0F)
            )

            Spacer(modifier = Modifier.size(16.dp))

            OperationButton(
                label = stringResource(id = R.string.deposit_cash),
                icon = painterResource(id = R.drawable.deposit),
                onClick = onDepositClick,
                modifier = Modifier.weight(weight = 1.0F)
            )
        }

        val context = LocalContext.current
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.operation_troubleshooting))
                addStyle(
                    style = SpanStyle(
                        color = Color(0xff64B5F6),
                        textDecoration = TextDecoration.Underline
                    ), start = 90, end = 105
                )
            },
            fontSize = 16.sp,
            modifier = Modifier
                .clickable {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:88001234567"))
                    startActivity(context, intent, null)
                }
        )
    }
}

@Composable
private fun OperationButton(
    label: String,
    icon: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(colorResource(id = R.color.light_gray))
            .clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            .clip(CircleShape)
            .size(56.dp)
            .background(color = Color.White),
            contentAlignment = Alignment.Center
        )
        {
            Image(
                painter = icon,
                contentDescription = label,
                modifier = Modifier.size(32.dp)
            )
        }

        Text(
            text = label,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp)
        )
    }
}

@Preview
@Composable
fun OperationChoosePreview() {
    OperationChooseScreen(onWithdrawClick = {}, onDepositClick = {})
}
