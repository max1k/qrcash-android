package ru.mxk.qrcash.ui.common

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
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
    type: OperationType,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isWithdrawal = type == OperationType.WITHDRAW
    val text = if (isWithdrawal) {
        stringResource(id = R.string.withdrawal_troubleshooting)
    } else {
        stringResource(id = R.string.deposit_troubleshooting)
    }

    Text(
        text = buildAnnotatedString {
            append(text)
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
    onCardSelectionActivated: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable{ onCardSelectionActivated() }
    ) {
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

        CardInfo(card)

        Divider(
            color = colorResource(id = R.color.secondary_font),
            thickness = 1.dp
        )
    }
}

@Composable
private fun CardInfo(
    card: Card,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
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
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CardSelectionPopup(
    visible: Boolean,
    cards: List<Card>,
    onCardSelectionDeactivated: () -> Unit,
    onCardSelected: (Card) -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInHorizontally(),
        exit = fadeOut() + slideOutHorizontally()
    ) {
        Popup(
            onDismissRequest = onCardSelectionDeactivated,
            properties = PopupProperties(
                focusable = true,
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                excludeFromSystemGesture = true,
            )
        ) {
            val backgroundColor by transition.animateColor(label = "ColorAnimation") { enterExistState ->
                if (enterExistState == EnterExitState.Visible) Color(0xDDAAAAAA) else Color.Transparent
            }

            val cardBackgroundColor by transition.animateColor(label = "ColorAnimation") { enterExistState ->
                if (enterExistState == EnterExitState.Visible) Color.White else colorResource(id = R.color.light_gray)
            }

            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .animateEnterExit(
                        enter = slideInHorizontally(),
                        exit = slideOutHorizontally()
                    )
            ) {
                Column(
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(20.dp))
                        .background(color = cardBackgroundColor)
                        .padding(all = 16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = stringResource(id = R.string.withdrawal_card),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(vertical = 22.dp)
                                .weight(1F)
                        )

                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = stringResource(R.string.back),
                            modifier = Modifier
                                .padding(bottom = 32.dp)
                                .clickable { onCardSelectionDeactivated() }
                        )
                    }

                    for (card in cards) {
                        CardInfo(
                            card = card,
                            modifier = Modifier
                                .clickable { onCardSelected(card) }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
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