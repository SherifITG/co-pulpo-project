package com.itgates.co.pulpo.ultra.ui.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.IdAndNameEntity
import com.itgates.co.pulpo.ultra.ui.composeUI.AppBarComposeView
import com.itgates.co.pulpo.ultra.ui.composeUI.ProductActivityUI
import com.itgates.co.pulpo.ultra.ui.theme.PulpoUltraTheme
import com.itgates.co.pulpo.ultra.viewModels.CacheViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import com.itgates.co.pulpo.ultra.R

@AndroidEntryPoint
class ProductsReportActivity : ComponentActivity() {

    private val cacheViewModel: CacheViewModel by viewModels()
    var products: List<IdAndNameEntity> = listOf()
    val isRoomDataFetchedToRefresh = MutableStateFlow(false)

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
                        ProductActivityUI(activity = this@ProductsReportActivity)
                    }
                }
            }
        }
        setObservers()
        loadRelationalPlannedVisitData()

    }
    private fun setObservers() {
        cacheViewModel.idAndNameEntityData.observe(this@ProductsReportActivity) {
            println(it)
            println(it.size)
            products = it
            isRoomDataFetchedToRefresh.value = !isRoomDataFetchedToRefresh.value
        }
    }

    private fun loadRelationalPlannedVisitData() {
        cacheViewModel.loadProductsData()

    }
}