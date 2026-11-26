package io.github.lucf15.adaptivenotes.platform.activity

import android.os.Build
import android.os.Bundle
import android.os.Process
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle

abstract class NotesBaseActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splash ->
                splash.iconView?.animate()?.alpha(0f)?.setDuration(90)?.start()
                splash.animate().alpha(0f).setDuration(140).withEndAction { splash.remove() }.start()
            }
        }
        enableEdgeToEdge(SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT), SystemBarStyle.light(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT))
        Log.d("FieldnotesLifecycle", "${javaClass.simpleName} create pid=${Process.myPid()} instance=${hashCode()} restored=${savedInstanceState != null}")
    }

    override fun onDestroy() {
        Log.d("FieldnotesLifecycle", "${javaClass.simpleName} destroy instance=${hashCode()} changing=$isChangingConfigurations")
        super.onDestroy()
    }
}
