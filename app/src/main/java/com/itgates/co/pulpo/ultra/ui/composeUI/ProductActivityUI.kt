package com.itgates.co.pulpo.ultra.ui.composeUI

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.itgates.co.pulpo.ultra.ui.activities.ProductsReportActivity
import com.itgates.co.pulpo.ultra.ui.theme.*

@Composable
fun ProductActivityUI(activity: ProductsReportActivity) {
    val isRoomDataFetchedToRefresh = activity.isRoomDataFetchedToRefresh.collectAsState()
    when(isRoomDataFetchedToRefresh.value) {
        true, false -> {
            ProductActivityScreen(activity)
        }
    }
}

@Composable
fun ProductActivityScreen(activity: ProductsReportActivity) {
    Box(modifier = Modifier.padding(horizontal = padding_24).padding(top = padding_4)) {
        LazyColumn(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(padding_8)
        ) {
            item {
                RowItemUI("Name", "ID")
            }
            items(activity.products) {
                RowItemUI(it.embedded.name, it.id.toString())
            }
        }
    }
}

@Composable
fun RowItemUI(name: String, id: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(padding_8)
    ) {
        Box(modifier = Modifier.weight(1F)) {
            TextFactory(text = name)
        }
        Box() {
            TextFactory(text = id)
        }
    }
}