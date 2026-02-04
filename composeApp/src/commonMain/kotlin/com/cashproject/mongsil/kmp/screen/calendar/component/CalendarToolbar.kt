package com.cashproject.mongsil.kmp.screen.calendar.component

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
import mongsil.composeapp.generated.resources.ic_shopping_bag
import mongsil.composeapp.generated.resources.ic_trash
import org.jetbrains.compose.resources.painterResource

@Composable
fun BoxScope.CalendarToolbar() {
    IconToolbar(
        modifier = Modifier
            .align(Alignment.TopCenter),
        leftContent = {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Res.drawable.ic_shopping_bag),
                contentDescription = ""
            )
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Res.drawable.ic_trash),
                contentDescription = ""
            )
        },
        rightContent = {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Res.drawable.ic_trash),
                contentDescription = ""
            )
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(Res.drawable.ic_trash),
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