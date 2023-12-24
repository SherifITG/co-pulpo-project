package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.itgates.co.pulpo.ultra.R
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Account
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.ActualVisit
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.Doctor
import com.itgates.co.pulpo.ultra.roomDataBase.entity.generalData.PlannedVisit
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.*
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.TablesNames
import com.itgates.co.pulpo.ultra.ui.composeUI.AppBarComposeView
import com.itgates.co.pulpo.ultra.ui.composeUI.TextFactory
import com.itgates.co.pulpo.ultra.ui.theme.*
import dagger.hilt.android.AndroidEntryPoint
import com.itgates.co.pulpo.ultra.utilities.PassedValues

@AndroidEntryPoint
class DBReportsActivity : ComponentActivity() {

    val tables = listOf(
        TablesNames.IdAndNameTable,
        TablesNames.AccountTypeTable,
        TablesNames.LineTable,
        TablesNames.BrickTable,
        TablesNames.DivisionTable,
        TablesNames.SettingTable,
        TablesNames.VacationTypeTable,
        TablesNames.AccountTable,
        TablesNames.DoctorTable,
        TablesNames.NewPlanTable,
        TablesNames.VacationTable,
        TablesNames.ActualVisitTable,
        TablesNames.PlannedVisitTable,
        TablesNames.PresentationTable,
        TablesNames.SlideTable,
        TablesNames.OfflineLogTable,
        TablesNames.OfflineLocTable
    )

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PulpoUltraTheme {
                Scaffold(
                    topBar = { AppBarComposeView(text = getString(R.string.app_name), goToHomeContext = this) }
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
                                itemsIndexed(tables) {index, item ->
                                    ReportItemComposeView(item, index == 0) {
                                        PassedValues.dbTableReportActivity_tableName = item
                                        startActivity(Intent(this@DBReportsActivity, DBTableReportActivity::class.java))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ReportItemComposeView(text: String, isFirstCard: Boolean, onClickAction: () -> Unit) {
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
                        painter = painterResource(R.drawable.data_all_data_icon),
                        contentDescription = "Report Icon",
                        tint = ITGatesPrimaryColor
                    )
                    Box(modifier = Modifier.weight(1F)) {
                        TextFactory(text = text.removeSuffix("table").replace("_", " "))
                    }
                }
            }
        }
    }
}