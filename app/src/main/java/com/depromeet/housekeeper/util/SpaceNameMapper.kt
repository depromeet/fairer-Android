package com.depromeet.housekeeper.util

import com.depromeet.housekeeper.model.enums.SpaceName

fun spaceNameMapper(engSpace: String): String {
    return when (engSpace) {
        "ENTRANCE" -> SpaceName.ENTRANCE.korSpace
        "LIVINGROOM" -> SpaceName.LIVINGROOM.korSpace
        "BATHROOM" -> SpaceName.BATHROOM.korSpace
        "OUTSIDE" -> SpaceName.OUTSIDE.korSpace
        "ROOM" -> SpaceName.ROOM.korSpace
        "KITCHEN" -> SpaceName.KITCHEN.korSpace
        "ETC" -> SpaceName.ETC.korSpace
        else -> SpaceName.ROOM.korSpace
    }
}