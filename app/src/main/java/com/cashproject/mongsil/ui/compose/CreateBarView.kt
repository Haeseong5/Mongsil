package com.cashproject.mongsil.ui.compose

import android.content.Context
import android.util.AttributeSet
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AbstractComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.R


class AppBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AbstractComposeView(context, attrs) {

    var title by mutableStateOf("")
    var onClickCamera by mutableStateOf({})

    @Composable
    override fun Content() {
        CreateBar(
            title = title,
            onClickSettings = onClickCamera,
        )
    }
}

@Preview(
    showBackground = true,
    name = "방송 가능"
)
@Composable
private fun CreateBarOn() {
    CreateBar(
        title = "demo title",
    )
}

@Preview(
    showBackground = true,
    name = "방송 불가능"
)
@Composable
private fun CreateBarOff() {
    CreateBar(
        title = "demo title",
    )
}

@Composable
fun CreateBar(
    title: String,
    onClickSettings: () -> Unit = {},
) {

    Row(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(
                start = 10.dp,
            ),
            text = title,
            fontFamily = defaultFont,
            fontSize = 22.sp
        )
        Box(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_settings_24),
            modifier = Modifier
                .clickable { onClickSettings() }
                .padding(10.dp),
//            colorFilter = ColorFilter.tint(colorResource(id = R.color.graySolid72)),
//            painter = painterResource(id = R.drawable.ic_24_camera),
            contentDescription = null
        )
    }
}