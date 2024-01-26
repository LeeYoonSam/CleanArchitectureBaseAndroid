package com.ys.base.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val title: String,
    val content: String,
)
