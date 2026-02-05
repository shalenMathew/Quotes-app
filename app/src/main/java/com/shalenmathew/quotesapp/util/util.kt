package com.shalenmathew.quotesapp.util

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.work.WorkManager
import com.shalenmathew.quotesapp.util.Constants.QUOTES_NOTIFICATION
import com.shalenmathew.quotesapp.util.Constants.QUOTES_WIDGET_UPDATE_NAME
import java.util.concurrent.TimeUnit

fun checkWorkManagerStatus(context: Context, lifecycleOwner: LifecycleOwner) {

    val workManager = WorkManager.getInstance(context)

    workManager.getWorkInfosForUniqueWorkLiveData(QUOTES_WIDGET_UPDATE_NAME)
        .observe(lifecycleOwner) { workInfoList ->
            if (workInfoList.isNotEmpty()) {
                for (workInfo in workInfoList) {
                    Log.d("WorkManagerStatus", "Work State: ${workInfo.state}")
                }
            } else {
                Log.d("WorkManagerStatus", "No Work Found")
            }
        }


    workManager.getWorkInfosForUniqueWorkLiveData(QUOTES_NOTIFICATION)
        .observe(lifecycleOwner) { workInfoList ->
            if (workInfoList.isNotEmpty()) {
                for (workInfo in workInfoList) {
                    Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Work State: ${workInfo.state}")
                }
            } else {
                Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "No Work Found")
            }
        }

}


fun getMillisFromNow(hour: Int): Long {
    return System.currentTimeMillis() + TimeUnit.HOURS.toMillis(hour.toLong())
}

fun convertDaysToHrs(days: Int): Int {
    return TimeUnit.DAYS.toHours(days.toLong()).toInt()
}

fun convertHrsToDays(hours: Int): Int {
    return TimeUnit.HOURS.toDays(hours.toLong()).toInt()
}