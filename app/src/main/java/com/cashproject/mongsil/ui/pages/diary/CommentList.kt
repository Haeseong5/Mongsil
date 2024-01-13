package com.cashproject.mongsil.ui.pages.diary

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cashproject.mongsil.R
import com.cashproject.mongsil.extension.DateFormat
import com.cashproject.mongsil.extension.toTextFormat
import com.cashproject.mongsil.ui.component.VerticalSpacer
import com.cashproject.mongsil.ui.model.Emoticon
import com.cashproject.mongsil.ui.theme.primaryTextStyle
import com.cashproject.mongsil.ui.theme.textShadow
import com.gigamole.composefadingedges.verticalFadingEdges


@OptIn(ExperimentalFoundationApi::class, ExperimentalLayoutApi::class)
@Composable
fun CommentList(
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    comments: List<Comment>,
    emoticons: List<Emoticon>,
    onLongClick: (Int) -> Unit = {},
) {
    val imeVisible = WindowInsets.isImeVisible

    LazyColumn(
        state = listState,
        reverseLayout = true,
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (comments.isNotEmpty() && !imeVisible) {
                    Modifier.verticalFadingEdges(
                        length = 8.dp
                    )
                } else Modifier
            ),
        content = {
            items(
                items = comments,
                key = { comment ->
                    comment.id
                }
            ) { comment ->
                Comment(
                    modifier = Modifier.animateItemPlacement(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing,
                        )
                    ),
                    comment = comment,
                    onLongClick = onLongClick,
                    emoticonImageUrl = emoticons.find { it.id == comment.emoticonId }?.imageUrl
                        ?: ""
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Comment(
    modifier: Modifier = Modifier,
    comment: Comment,
    emoticonImageUrl: String,
    onLongClick: (Int) -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .combinedClickable(
                interactionSource = MutableInteractionSource(),
                indication = null,
                onLongClick = {
                    onLongClick.invoke(comment.id)
                },
                onClick = {}
            )
    ) {
        AsyncImage(
            modifier = Modifier
                .size(46.dp)
                .padding(end = 8.dp),
            model = emoticonImageUrl,
            contentDescription = null
        )
        Column(
            modifier = Modifier.weight(1f, false)
        ) {
            Text(
                text = comment.time.toTextFormat(DateFormat.HourMinute),
                style = primaryTextStyle.copy(
                    shadow = textShadow,
                    color = colorResource(id = R.color.colorYellow),
                    fontSize = 20.sp
                ),
                letterSpacing = 1.5.sp
            )
            VerticalSpacer(dp = 2.dp)
            Text(
                text = comment.content,
                color = Color.White,
                style = primaryTextStyle.copy(
                    shadow = textShadow,
                    color = Color.White,
                    fontSize = 22.sp
                )
            )
        }
    }
}