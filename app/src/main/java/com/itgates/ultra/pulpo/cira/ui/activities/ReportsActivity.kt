package com.itgates.ultra.pulpo.cira.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.itgates.ultra.pulpo.cira.R
import com.itgates.ultra.pulpo.cira.ui.composeUI.AppBarComposeView
import com.itgates.ultra.pulpo.cira.ui.composeUI.TextFactory
import com.itgates.ultra.pulpo.cira.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import com.itgates.ultra.pulpo.cira.utilities.PassedValues

@AndroidEntryPoint
class ReportsActivity : ComponentActivity() {

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PulpoUltraTheme {
                Scaffold(
                    topBar = { AppBarComposeView(text = getString(R.string.app_name)) }
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Box {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(padding_8)
                            ) {
                                item {
                                    ReportItemComposeView("Product", true, R.drawable.report_product_data_icon) {
                                        startActivity(Intent(this@ReportsActivity, ProductsReportActivity::class.java))
                                    }
                                }
                                item {
                                    ReportItemComposeView("Accounts", false, R.drawable.report_account_icon) {
                                        startActivity(Intent(this@ReportsActivity, AccountsReportActivity::class.java))
                                    }
                                }
                                item {
                                    ReportItemComposeView("Actual Visits", false, R.drawable.report_actual_visits_icon) {
                                        startActivity(Intent(this@ReportsActivity, ActualVisitReportActivity::class.java))
                                    }
                                }
                                item {
                                    ReportItemComposeView("Planned Visits", false, R.drawable.report_planned_visits_icon) {
                                        PassedValues.plannedActivity_isToday = false
                                        startActivity(Intent(this@ReportsActivity, PlannedVisitActivity::class.java))
                                    }
                                }
//                                item {
//                                    ReportItemComposeView("New Plans", false, R.drawable.report_planned_visits_icon) {
//                                        startActivity(Intent(this@ReportsActivity, NewPlanReportActivity::class.java))
//                                    }
//                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ReportItemComposeView(text: String, isFirstCard: Boolean, @DrawableRes iconId: Int, onClickAction: () -> Unit) {
        val modifier = if (isFirstCard) Modifier.padding(top = padding_4) else Modifier
        Card(
            shape = ITGatesCardCornerShape,
            modifier = modifier
                .padding(horizontal = padding_16)
                .clickable { onClickAction() },
            elevation = padding_16,
        ) {
            Box(modifier = Modifier.padding(horizontal = padding_24, vertical = padding_16)) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(padding_12)
                ) {
                    Icon(
                        modifier = Modifier.size(padding_36),
                        painter = painterResource(iconId),
                        contentDescription = "Report Icon",
                        tint = ITGatesPrimaryColor
                    )
                    Box(modifier = Modifier.weight(1F)) {
                        TextFactory(text = text)
                    }
                }
            }
        }
    }
}