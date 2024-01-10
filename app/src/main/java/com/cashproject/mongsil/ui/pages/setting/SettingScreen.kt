package com.cashproject.mongsil.ui.pages.setting

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.R
import com.cashproject.mongsil.extension.getStatusBarHeight
import com.cashproject.mongsil.extension.noRippleClickable
import com.cashproject.mongsil.ui.component.Toolbar
import com.cashproject.mongsil.ui.theme.primaryTextColor
import com.cashproject.mongsil.ui.theme.primaryTextStyle


@Composable
fun SettingScreen(
    onUiAction: (UiAction) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = getStatusBarHeight())
    ) {
        Column() {
            Toolbar(
                title = "설정",
                navigationIcon = Icons.Outlined.ArrowBack,
                navigationIconClicked = {
                    onUiAction.invoke(UiAction.Back)
                }
            )
            Column(
                modifier = Modifier
                    .padding(
                        horizontal = 20.dp
                    )
                    .fillMaxSize()
            ) {
                SettingList(
                    buttonList = buttonList,
                    onUiAction = onUiAction
                )
            }
        }
    }
}

@Composable
private fun SettingList(
    buttonList: List<SettingButton>,
    onUiAction: (UiAction) -> Unit = {},
) {
    LazyColumn(
        contentPadding = PaddingValues(top = 20.dp),
        content = {
            items(buttonList) {
                SettingItem(
                    modifier = Modifier.padding(bottom = 22.dp),
                    image = it.image,
                    text = stringResource(id = it.text),
                    isVisibleRightArrow = it.isVisibleRightArrow,
                    onClick = {
                        onUiAction.invoke(it.type)
                    }
                )
            }
        })
}

@Composable
private fun SettingItem(
    modifier: Modifier = Modifier,
    @DrawableRes image: Int,
    contentDescription: String = "",
    text: String = "",
    isVisibleRightArrow: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .noRippleClickable {
                onClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier.size(30.dp),
            painter = painterResource(id = image),
            colorFilter = if (image == R.drawable.emoticon_03) null else ColorFilter.tint(
                primaryTextColor
            ),
            contentDescription = contentDescription
        )
        Text(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f),
            text = text,
            fontSize = 20.sp,
            style = primaryTextStyle,
            color = primaryTextColor
        )
        if (isVisibleRightArrow) {
            Image(
                modifier = Modifier.align(Alignment.CenterVertically),
                painter = painterResource(id = R.drawable.ic_close_copy),
                colorFilter = ColorFilter.tint(primaryTextColor),
                contentDescription = ""
            )
        }
    }
}

sealed interface UiAction {
    data object Back : UiAction
    data class OnClickMenu(val type: SettingButtonType) : UiAction
}

enum class SettingButtonType {
    NOTIFICATION_SETTING,
    APP_INTRODUCTION,
    SUGGESTION,
    BACKUP,
    OTHER_APP_FROM_THE_DEVELOPER,
    APP_VERSION_CHECK,
    NONE
}

data class SettingButton(
    val image: Int,
    @StringRes val text: Int,
    val isVisibleRightArrow: Boolean = false,
    val type: UiAction = UiAction.OnClickMenu(SettingButtonType.NONE)
)

val buttonList = listOf(
    SettingButton(
        image = R.drawable.ic_notice,
        text = R.string.setting_notification_setting,
        isVisibleRightArrow = true,
        type = UiAction.OnClickMenu(SettingButtonType.NOTIFICATION_SETTING)
    ),
    SettingButton(
        image = R.drawable.emoticon_03,
        text = R.string.setting_app_introduction,
        type = UiAction.OnClickMenu(SettingButtonType.APP_INTRODUCTION)
    ),
    SettingButton(
        image = R.drawable.ic_question,
        text = R.string.setting_suggestion,
        type = UiAction.OnClickMenu(SettingButtonType.SUGGESTION)
    ),
//    SettingButton(
//        image = R.drawable.ic_backup,
//        text = R.string.setting_backup,
//        type = UiAction.OnClickMenu(SettingButtonType.BACKUP)
//    ),
    SettingButton(
        image = R.drawable.ic_moreapp,
        text = R.string.setting_other_app_from_the_developer,
        type = UiAction.OnClickMenu(SettingButtonType.OTHER_APP_FROM_THE_DEVELOPER)
    ),
    SettingButton(
        image = R.drawable.ic_review,
        text = R.string.setting_app_version_check,
        type = UiAction.OnClickMenu(SettingButtonType.APP_VERSION_CHECK)
    ),
)


@Preview(showBackground = true)
@Composable
private fun PreviewSettingScreen() {
    SettingScreen()
}

