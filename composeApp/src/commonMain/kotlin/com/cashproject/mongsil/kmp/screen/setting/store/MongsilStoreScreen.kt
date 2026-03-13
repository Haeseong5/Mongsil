package com.cashproject.mongsil.kmp.screen.setting.store

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.kmp.designsystem.component.CommonToolbar
import com.cashproject.mongsil.kmp.designsystem.component.rememberSnackbarController
import com.cashproject.mongsil.kmp.screen.diarywrite.component.ShowRewardedAd

private const val PREMIUM_PRODUCT_ID = "premium_lifetime"
private const val WINTER_THEME_PRODUCT_ID = "theme_winter"
private const val ANIMAL_THEME_PRODUCT_ID = "theme_animal"

private data class ThemeStickerItem(
    val id: String,
    val title: String,
    val emoji: String,
    val accentColor: Color,
    val dialogMessage: String,
)

@Composable
fun MongsilStoreScreen(
    onBack: () -> Unit = {},
) {
    val snackbarController = rememberSnackbarController()
    val purchaseLauncher = rememberInAppPurchaseLauncher(
        onPurchaseSuccess = {
            snackbarController.showSnackbar("결제가 완료되었습니다")
        },
        onPurchaseCancelled = {},
        onError = { message ->
            snackbarController.showSnackbar(message)
        },
    )

    val themeItems = remember {
        listOf(
            ThemeStickerItem(
                id = WINTER_THEME_PRODUCT_ID,
                title = "겨울",
                emoji = "\u2603",
                accentColor = Color(0xFFF7EAD2),
                dialogMessage = "동영상 광고를 시청하면 12시간 동안\n테마(겨울) 스티커를 무료로 이용할 수 있어요.\n광고를 보시겠어요?",
            ),
            ThemeStickerItem(
                id = ANIMAL_THEME_PRODUCT_ID,
                title = "동물",
                emoji = "\uD83D\uDC3E",
                accentColor = Color(0xFFE4F2D4),
                dialogMessage = "동영상 광고를 시청하면 12시간 동안\n테마(동물) 스티커를 무료로 이용할 수 있어요.\n광고를 보시겠어요?",
            ),
        )
    }

    var dialogTheme by remember { mutableStateOf<ThemeStickerItem?>(null) }
    var showRewardedAd by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .statusBarsPadding(),
    ) {
        CommonToolbar(
            color = Color(0xFFF7F7F7),
            onBack = onBack,
            title = "꼬박 스토어",
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 40.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                PremiumPassCard(
                    onPurchaseClick = { purchaseLauncher.launch(PREMIUM_PRODUCT_ID) },
                )
            }

            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp),
                    color = Color(0xFFE4E4E4),
                )
            }

            item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(maxLineSpan) }) {
                Text(
                    text = "테마 스티커",
                    color = Color(0xFF2A2A2A),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.W600,
                )
            }

            items(themeItems, key = { it.id }) { item ->
                ThemeStickerCard(
                    item = item,
                    onWatchAdClick = { dialogTheme = item },
                    onPurchaseClick = { purchaseLauncher.launch(item.id) },
                )
            }
        }
    }

    dialogTheme?.let { item ->
        AlertDialog(
            onDismissRequest = { dialogTheme = null },
            containerColor = Color.White,
            shape = RoundedCornerShape(28.dp),
            text = {
                Text(
                    text = item.dialogMessage,
                    color = Color(0xFF3B3B3B),
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    lineHeight = 30.sp,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dialogTheme = null
                        showRewardedAd = true
                    },
                ) {
                    Text(
                        text = "광고 보기",
                        color = Color(0xFF2A2A2A),
                        fontSize = 16.sp,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { dialogTheme = null }) {
                    Text(
                        text = "취소",
                        color = Color(0xFF7B7B7B),
                        fontSize = 16.sp,
                    )
                }
            },
        )
    }

    if (showRewardedAd) {
        ShowRewardedAd(
            onRewarded = {
                showRewardedAd = false
                snackbarController.showSnackbar("테마 스티커 사용이 가능합니다")
            },
            onDismissed = {
                showRewardedAd = false
            },
        )
    }
}

@Composable
private fun PremiumPassCard(
    onPurchaseClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 3.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            Text(
                text = "프리미엄 이용권",
                color = Color(0xFF2A2A2A),
                fontSize = 28.sp,
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(bottom = 28.dp),
            )

            PremiumFeatureRow(emoji = "\u2600\uFE0F", title = "한 번 결제하면 평생 이용 가능해요")
            Spacer(modifier = Modifier.height(18.dp))
            PremiumFeatureRow(emoji = "\u2602\uFE0F", title = "광고가 나오지 않아요")
            Spacer(modifier = Modifier.height(18.dp))
            PremiumFeatureRow(
                emoji = "\u2615",
                title = "프리미엄 기능 사용이 가능해요",
                bullets = listOf("일상 스티커 사용 가능", "사진 5장까지 추가 가능"),
            )

            Button(
                onClick = onPurchaseClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp)
                    .height(64.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1F1F23),
                    contentColor = Color.White,
                ),
            ) {
                Text(
                    text = "\u20A94,400",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.W700,
                )
            }
        }
    }
}

@Composable
private fun PremiumFeatureRow(
    emoji: String,
    title: String,
    bullets: List<String> = emptyList(),
) {
    Row(verticalAlignment = Alignment.Top) {
        Text(
            text = emoji,
            fontSize = 38.sp,
            modifier = Modifier.padding(top = 2.dp),
        )
        Spacer(modifier = Modifier.width(18.dp))
        Column {
            Text(
                text = title,
                color = Color(0xFF3A3A3A),
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
            )
            bullets.forEach { bullet ->
                Text(
                    text = "\u2022 $bullet",
                    color = Color(0xFF8D8D8D),
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
        }
    }
}

@Composable
private fun ThemeStickerCard(
    item: ThemeStickerItem,
    onWatchAdClick: () -> Unit,
    onPurchaseClick: () -> Unit,
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(132.dp)
                    .clip(CircleShape)
                    .background(item.accentColor),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = item.emoji,
                    fontSize = 56.sp,
                )
            }

            Text(
                text = item.title,
                color = Color(0xFF2C2C2C),
                fontSize = 22.sp,
                fontWeight = FontWeight.W500,
                modifier = Modifier.padding(top = 20.dp, bottom = 18.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                VideoAdButton(
                    modifier = Modifier.weight(1f),
                    onClick = onWatchAdClick,
                )
                PurchaseButton(
                    modifier = Modifier.weight(1.6f),
                    onClick = onPurchaseClick,
                )
            }
        }
    }
}

@Composable
private fun VideoAdButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .clip(CircleShape)
            .border(1.dp, Color(0xFFD9D9D9), CircleShape)
            .background(Color.White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "\u25B6\uFE0F",
            fontSize = 18.sp,
        )
    }
}

@Composable
private fun PurchaseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .height(56.dp)
            .clip(CircleShape)
            .background(Color(0xFF1F1F23))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "구매하기",
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.W600,
        )
    }
}
