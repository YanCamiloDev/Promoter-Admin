
package net.yan.kotlin.promoteradm

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.core.app.ActivityCompat


object Permissao {
    fun validarPermissoes(
        activity: Activity
    ): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(activity,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.CAMERA),
                1)
        }
        return true
    }
}