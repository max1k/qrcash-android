package ru.mxk.qrcash.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.mxk.qrcash.ui.deposit.DepositScreen
import ru.mxk.qrcash.ui.navigation.QRCashScreen
import ru.mxk.qrcash.ui.withdraw.WithdrawalScreen
import ru.mxk.qrcash.viewmodel.OperationViewModel

@Composable
fun QRCashApp(
    modifier: Modifier = Modifier,
    viewModel: OperationViewModel,
    navController: NavHostController = rememberNavController(),
) {
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
            WithdrawalScreen()
        }

        composable(route = QRCashScreen.DEPOSIT.url) {
            DepositScreen()
        }
    }
}
