package com.ys.base.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Document(
    val id: Long,
    val name: String,
    val description: String,
)
