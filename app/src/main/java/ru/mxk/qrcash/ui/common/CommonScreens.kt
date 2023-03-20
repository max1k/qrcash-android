package ru.mxk.qrcash.ui.common

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import ru.mxk.qrcash.R
import ru.mxk.qrcash.model.Card
import ru.mxk.qrcash.model.OperationType

@Composable
fun LoadingScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun CallUsSection(
    modifier: Modifier = Modifier
) {
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
        modifier = modifier
            .clickable {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:88001234567"))
                ContextCompat.startActivity(context, intent, null)
            }
    )
}

@Composable
fun SelectedCardSection(
    card: Card,
    operationType: OperationType,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        val header = if (operationType == OperationType.WITHDRAW) {
            stringResource(id = R.string.withdrawal_card)
        } else {
            stringResource(id = R.string.deposit_card)
        }

        Text(
            text = header,
            fontSize = 16.sp,
            color = colorResource(id = R.color.secondary_font),
            modifier = Modifier.padding(bottom = 8.dp)
        )

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

@Composable
fun DetailsSection(
    header: String,
    info: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = header,
            fontSize = 16.sp,
            color = colorResource(id = R.color.secondary_font),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = info,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Divider(
            color = colorResource(id = R.color.secondary_font),
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
    }
}