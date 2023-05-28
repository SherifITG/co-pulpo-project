package com.itgates.ultra.pulpo.cira.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.itgates.ultra.pulpo.cira.ui.composeUI.LoadingDialog
import com.itgates.ultra.pulpo.cira.ui.composeUI.NoInternetDialog
import com.itgates.ultra.pulpo.cira.ui.composeUI.TextFactory
import com.itgates.ultra.pulpo.cira.ui.theme.*
import com.itgates.ultra.pulpo.cira.ui.utils.BaseDataActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DataCenterActivity : BaseDataActivity() {

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
                        Column {
                            Box {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(padding_8)
                                ) {
                                    item {
                                        ReportItemComposeView("GET ALL DATA", true,  R.drawable.data_all_data_icon) {
                                            if (loadingAllStateFlow.value == 0) {
                                                loadingAllStateFlow.value = 1 // fired
                                                updateMasterData()
                                                updateAccountAndDoctorData()
                                                updatePlannedVisitData()
                                                updatePresentationAndSlideData()
                                            }
                                        }
                                    }
                                    item {
                                        ReportItemComposeView("GET MASTER DATA", false,  R.drawable.data_master_data_icon) {
                                            if (loadingAllStateFlow.value == 0) {
                                                loadingAllStateFlow.value = 4
                                                updateMasterData()
                                            }
                                        }
                                    }
                                    item {
                                        ReportItemComposeView("GET ACCOUNTS DATA", false, R.drawable.report_account_icon) {
                                            if (loadingAllStateFlow.value == 0) {
                                                loadingAllStateFlow.value = 4
                                                updateAccountAndDoctorData()
                                            }
                                        }
                                    }
                                    item {
                                        ReportItemComposeView("GET PLANNED VISITS DATA", false, R.drawable.report_planned_visits_icon) {
                                            if (loadingAllStateFlow.value == 0) {
                                                loadingAllStateFlow.value = 4
                                                updatePlannedVisitData()
                                            }
                                        }
                                    }
                                    item {
                                        ReportItemComposeView("GET PRESENTATIONS DATA", false, R.drawable.report_planned_visits_icon) {
                                            if (loadingAllStateFlow.value == 0) {
                                                loadingAllStateFlow.value = 4
                                                updatePresentationAndSlideData()
                                            }
                                        }
                                    }
                                }
                            }
                            NoInternetDialog(internetStateFlow)
                            LoadingDialog(loadingStateFlow, "Loading")
                        }
                    }
                }
            }
        }
        setDataObservers()
    }

    @Composable
    fun ReportItemComposeView(text: String, isFirstCard: Boolean, @DrawableRes iconId: Int, onClickAction: () -> Unit) {
        val modifier = if (isFirstCard) Modifier.padding(top = padding_4) else Modifier
        Card(
            shape = ITGatesCardCornerShape,
            modifier = modifier
                .padding(horizontal = padding_16),
            elevation = padding_16
        ) {
            Box(
                modifier = Modifier
                    .clickable { onClickAction() }
            ) {
                Box(modifier = Modifier.padding(horizontal = padding_18, vertical = padding_12)) {
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
                            TextFactory(
                                text = text,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Icon(
                            modifier = Modifier.size(padding_36),
                            painter = painterResource(R.drawable.data_download_icon),
                            contentDescription = "Report Icon",
                            tint = ITGatesPrimaryColor
                        )
                    }
                }
            }
        }
    }

}