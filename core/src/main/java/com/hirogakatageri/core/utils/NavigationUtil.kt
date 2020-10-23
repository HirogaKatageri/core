package com.hirogakatageri.core.utils

import android.content.Context
import android.content.Intent
import com.gaelmarhic.quadrant.QuadrantConstants

object NavigationUtil {

    const val RC_PROFILE = 100

    const val USERNAME = "USERNAME"
    const val UID = "UID"

    fun createProfileIntent(
        context: Context,
        username: String,
        uid: Int
    ): Intent = Intent(
        context,
        Class.forName(QuadrantConstants.PROFILE_ACTIVITY_MAIN)
    ).apply {
        putExtra(USERNAME, username)
        putExtra(UID, uid)
    }

}