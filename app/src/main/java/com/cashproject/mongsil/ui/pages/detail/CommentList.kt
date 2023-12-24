package com.cashproject.mongsil.ui.pages.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashproject.mongsil.R
import com.cashproject.mongsil.extension.DateFormat
import com.cashproject.mongsil.extension.printErrorLog
import com.cashproject.mongsil.extension.toTextFormat
import com.cashproject.mongsil.ui.component.VerticalSpacer
import com.gigamole.composefadingedges.verticalFadingEdges

@Composable
fun CommentList(
    modifier: Modifier = Modifier,
    comments: List<Comment>,
    onLongClick: (Int) -> Unit = {},
) {
    val listState = rememberLazyListState()

    LaunchedEffect(comments) {
        try {
            if (comments.isNotEmpty()) {
                listState.animateScrollToItem(0)
            }
        } catch (e: Exception) {
            e.printErrorLog()
        }
    }

    LazyColumn(
        state = listState,
        reverseLayout = true,
        modifier = modifier
            .fillMaxWidth()
            .verticalFadingEdges(
                length = 8.dp
            ),
        content = {
            items(comments) {
                Comment(
                    comment = it,
                    onLongClick = onLongClick,
                )
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Comment(
    comment: Comment,
    onLongClick: (Int) -> Unit = {},
) {
    Row(
        modifier = Modifier
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
        Image(
            modifier = Modifier
                .size(42.dp)
                .padding(end = 8.dp),
            painter = painterResource(id = comment.emoticon.icon),
            contentDescription = null
        )
        Column(
            modifier = Modifier.weight(1f, false)
        ) {
            Text(
                text = comment.time.toTextFormat(DateFormat.HourMinute),
                color = colorResource(id = R.color.colorYellow),
                fontSize = 16.sp
            )
            VerticalSpacer(dp = 8.dp)
            Text(
                text = comment.content,
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}