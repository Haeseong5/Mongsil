package com.cashproject.mongsil.kmp.model

import org.jetbrains.compose.resources.DrawableResource

sealed interface ImageResource {
    data class Url(val url: String) : ImageResource
    data class Local(
        val resource: DrawableResource,
        val assetPath: String = "",
    ) : ImageResource
}
