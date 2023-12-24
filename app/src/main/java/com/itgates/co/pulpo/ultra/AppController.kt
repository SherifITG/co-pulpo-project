package com.itgates.co.pulpo.ultra

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.itgates.co.pulpo.ultra.roomDataBase.entity.EmbeddedEntity
import com.itgates.co.pulpo.ultra.roomDataBase.entity.masterData.Setting
import com.itgates.co.pulpo.ultra.roomDataBase.roomUtils.enums.SettingEnum
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class AppController: Application() {

    var currentActivity: Activity? = null

//    var isAppForeground = true
//    var mustBeNavigate = false
//    var locationInfo: LocationClient.LocationInfo = LocationClient.LocationInfo(null, "")

    override fun onCreate() {
        super.onCreate()
        CoroutineManager.init()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                currentActivity = activity
//                doRequiredNavigation()
            }

            override fun onActivityStarted(activity: Activity) {
                currentActivity = activity
                println(activity.javaClass.simpleName)
//                doRequiredNavigation()
            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = activity
//                isAppForeground = true
//                doRequiredNavigation()
            }

            override fun onActivityPaused(activity: Activity) {
                // Do nothing
//                isAppForeground = false
            }

            override fun onActivityStopped(activity: Activity) {
                // Do nothing
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
                // Do nothing
            }

            override fun onActivityDestroyed(activity: Activity) {
                // Do nothing
            }
        })
    }
//
//    private fun doRequiredNavigation() {
//        if (mustBeNavigate) {
//            mustBeNavigate = false
//            Utilities.handleLocationErrors(this.locationInfo, this)
//        }
//    }
}

object CoroutineManager {
    private lateinit var appScope: CoroutineScope

    fun init() {
        appScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    }

    fun getScope(): CoroutineScope {
        return appScope
    }
}
