package com.cashproject.mongsil.kmp.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.cashproject.mongsil.kmp.model.ImageResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun EmoticonImage(
    image: ImageResource,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    when (image) {
        is ImageResource.Url -> AsyncImage(
            model = image.url,
            contentDescription = contentDescription,
            modifier = modifier,
        )
        is ImageResource.Local -> Image(
            painter = painterResource(image.resource),
            contentDescription = contentDescription,
            modifier = modifier,
        )
    }
}
