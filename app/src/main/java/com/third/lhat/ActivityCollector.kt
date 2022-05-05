package com.third.lhat

import android.app.Activity

object ActivityCollector {

    private val activities = ArrayList<Activity>()

    fun addActivity(activity: Activity) = activities.add(activity)

    fun removeActivity(activity: Activity) = activities.remove(activity)

    fun removeAll() = activities.forEach {
        if (!it.isFinishing) {
            it.finish()
        }
        activities.clear()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

}