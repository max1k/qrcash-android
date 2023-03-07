package ru.mxk.qrcash.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.mxk.qrcash.ui.navigation.QRCashScreen
import ru.mxk.qrcash.viewmodel.OperationViewModel

@Composable
fun QRCashApp(
    modifier: Modifier = Modifier,
    viewModel: OperationViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = QRCashScreen.OPERATION_CHOOSE.url
    ) {
        composable(route = QRCashScreen.OPERATION_CHOOSE.url) {
            OperationChooseScreen(
                onWithdrawClick = {},
                onDepositClick = {}
            )
        }
    }
}
