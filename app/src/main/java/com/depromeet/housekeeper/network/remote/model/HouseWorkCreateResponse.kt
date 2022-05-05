package com.depromeet.housekeeper.network.remote.model

import com.depromeet.housekeeper.model.HouseWork
import kotlinx.serialization.Serializable

@Serializable
data class HouseWorkCreateResponse(
    val houseWorks: List<HouseWork>
)
