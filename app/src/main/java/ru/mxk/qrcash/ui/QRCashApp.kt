package ru.mxk.qrcash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.mxk.qrcash.model.OperationType
import ru.mxk.qrcash.model.SessionData
import ru.mxk.qrcash.ui.deposit.DepositScreen
import ru.mxk.qrcash.ui.navigation.QRCashScreen
import ru.mxk.qrcash.ui.withdraw.WithdrawalScreen
import ru.mxk.qrcash.viewmodel.CardSelectionViewModel
import ru.mxk.qrcash.viewmodel.OperationViewModel

@Composable
fun QRCashApp(
    modifier: Modifier = Modifier,
    cardSelectionViewModel: CardSelectionViewModel,
    operationViewModel: OperationViewModel,
    navController: NavHostController = rememberNavController(),
) {
    val cardListUiState by cardSelectionViewModel.uiState.collectAsState()
    val operationUiState by operationViewModel.uiState.collectAsState()
    val sessionData = SessionData("111", "222", "3333")

    NavHost(
        navController = navController,
        startDestination = QRCashScreen.OPERATION_CHOOSE.url
    ) {
        composable(route = QRCashScreen.OPERATION_CHOOSE.url) {
            OperationChooseScreen(
                onWithdrawClick = { navController.navigate(QRCashScreen.WITHDRAWAL.url) },
                onDepositClick = { navController.navigate(QRCashScreen.DEPOSIT.url) }
            )
        }

        composable(route = QRCashScreen.WITHDRAWAL.url) {
            cardSelectionViewModel.requestCardList(sessionData)
            operationViewModel.setOperationType(OperationType.WITHDRAW)

            WithdrawalScreen(
                cardListUiState = cardListUiState,
                operationUiState = operationUiState,
                onAmountChange = { operationViewModel.setAmount(it) }
            )
        }

        composable(route = QRCashScreen.DEPOSIT.url) {
            cardSelectionViewModel.requestCardList(sessionData)
            operationViewModel.setOperationType(OperationType.DEPOSIT)

            DepositScreen()
        }
    }
}
