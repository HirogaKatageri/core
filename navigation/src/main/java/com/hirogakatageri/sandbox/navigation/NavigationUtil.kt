package com.hirogakatageri.sandbox.navigation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.gaelmarhic.quadrant.QuadrantConstants

object NavigationUtil {

    const val RC_PROFILE = 100

    const val USERNAME = "USERNAME"
    const val UID = "UID"

    fun createProfileIntent(
        context: Context,
        bundle: Bundle
    ): Intent = Intent(
        context,
        Class.forName(QuadrantConstants.PROFILE_ACTIVITY_MAIN)
    ).apply {
        putExtras(bundle)
    }

}