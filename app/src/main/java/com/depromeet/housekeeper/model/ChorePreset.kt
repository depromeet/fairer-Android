package com.depromeet.housekeeper.model

import kotlinx.serialization.Serializable

@Serializable
data class ChorePreset(
    val preset: List<ChoreList>
)