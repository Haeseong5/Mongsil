package com.cashproject.mongsil.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.R
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.theme.gamjaflowerFamily


@Composable
fun Toolbar(
    modifier: Modifier = Modifier,
    title: String = "",
    onBackButtonClick: () -> Unit = {},
    isVisibleSettingButton: Boolean = false,
) {
    Surface(
        modifier = Modifier
            .background(Color.White)
            .height(56.dp)
            .fillMaxWidth()
            .shadow(4.dp)
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.White)
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterStart)
                    .size(24.dp)
                    .noRippleClickable {
                        onBackButtonClick.invoke()
                    },
                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_24),
                contentDescription = ""
            )

            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                fontSize = 20.sp,
                fontFamily = gamjaflowerFamily
            )

            if(isVisibleSettingButton){
                Icon(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .align(Alignment.CenterEnd)
                        .size(24.dp)
                        .noRippleClickable {
                            onBackButtonClick.invoke()
                        },
                    painter = painterResource(id = R.drawable.ic_baseline_settings_24),
                    contentDescription = ""
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewToolbar() {
    Toolbar(title = "제목")
}