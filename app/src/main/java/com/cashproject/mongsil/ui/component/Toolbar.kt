package com.cashproject.mongsil.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.theme.primaryBackgroundColor
import com.cashproject.mongsil.ui.theme.primaryTextColor
import com.cashproject.mongsil.ui.theme.primaryTextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    title: String = "",
    navigationIcon: ImageVector? = null,
    navigationIconClicked: () -> Unit = {},
    actionIcon: ImageVector? = null,
    actionIconClicked: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier
            .background(primaryBackgroundColor)
            .height(56.dp),
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                style = primaryTextStyle,
            )
        },
        navigationIcon = {
            navigationIcon?.let {
                Image(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .noRippleClickable(onClick = navigationIconClicked),
                    colorFilter = ColorFilter.tint(primaryTextColor),
                    imageVector = navigationIcon,
                    contentDescription = ""
                )
            }
        },
        actions = {
            actionIcon?.let {
                Image(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .noRippleClickable(onClick = actionIconClicked),
                    imageVector = it,
                    colorFilter = ColorFilter.tint(primaryTextColor),
                    contentDescription = ""
                )
            }
        }
    )
}

@Composable
private fun TopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.align(Alignment.CenterStart)) {
            navigationIcon.invoke()
        }
        Box(modifier = Modifier.align(Alignment.Center)) {
            title.invoke()
        }
        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            actions.invoke()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewToolbar() {
    Toolbar(title = "제목")
}