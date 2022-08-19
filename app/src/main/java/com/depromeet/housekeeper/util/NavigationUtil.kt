package com.depromeet.housekeeper.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavGraph

object NavigationUtil {
    fun NavController.navigateSafe(@IdRes resId: Int, args: Bundle? = null) {
        val destinationId = currentDestination?.getAction(resId)?.destinationId.orEmpty()
        currentDestination?.let { node ->
            val currentNode = when (node) {
                is NavGraph -> node
                else -> node.parent
            }
            if (destinationId != 0) {
                currentNode?.findNode(destinationId)?.let { navigate(resId, args) }
            }
        }
    }

    private fun Int?.orEmpty(default: Int = 0): Int {
        return this ?: default
    }
}