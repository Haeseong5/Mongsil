package com.cashproject.mongsil.kmp.screen.calendar.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cashproject.mongsil.kmp.designsystem.MongsilTheme
import com.cashproject.mongsil.kmp.designsystem.component.IconToolbar
import mongsil.composeapp.generated.resources.Res
import mongsil.composeapp.generated.resources.ic_article
import mongsil.composeapp.generated.resources.ic_bar_chart
import mongsil.composeapp.generated.resources.ic_more
import mongsil.composeapp.generated.resources.ic_search
import mongsil.composeapp.generated.resources.ic_shopping_bag
import org.jetbrains.compose.resources.painterResource

@Composable
fun BoxScope.CalendarToolbar(
    onNavigateToSetting: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
) {
    IconToolbar(
        modifier = Modifier
            .align(Alignment.TopCenter),
        leftContent = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onNavigateToSetting() },
                painter = painterResource(Res.drawable.ic_more),
                contentDescription = "설정"
            )
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_shopping_bag),
                contentDescription = ""
            )
        },
        rightContent = {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onNavigateToSearch() },
                painter = painterResource(Res.drawable.ic_search),
                contentDescription = "검색"
            )
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_bar_chart),
                contentDescription = ""
            )
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(Res.drawable.ic_article),
                contentDescription = ""
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CalendarToolbarPreview() {
    MongsilTheme {
        Box {
            CalendarToolbar()
        }
    }
}
