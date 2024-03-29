package dev.hirogakatageri.sandbox.util

import android.provider.Settings
import androidx.fragment.app.Fragment

val Fragment.canDrawOverlays: Boolean get() = Settings.canDrawOverlays(requireContext())
