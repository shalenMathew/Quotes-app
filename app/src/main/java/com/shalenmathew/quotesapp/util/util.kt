package com.shalenmathew.quotesapp.util

import android.content.Context
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.work.WorkManager

fun checkWorkManagerStatus(context: Context,lifecycleOwner: LifecycleOwner) {

    val workManager = WorkManager.getInstance(context)

    workManager.getWorkInfosForUniqueWorkLiveData("quotes_widget_update").observe(lifecycleOwner) { workInfoList ->
        if (workInfoList.isNotEmpty()) {
            for (workInfo in workInfoList) {
                Log.d("WorkManagerStatus", "Work State: ${workInfo.state}")
            }
        } else {
            Log.d("WorkManagerStatus", "No Work Found")
        }
    }


    workManager.getWorkInfosForUniqueWorkLiveData("quotes_notification").observe(lifecycleOwner) { workInfoList ->
        if (workInfoList.isNotEmpty()) {
            for (workInfo in workInfoList) {
                Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "Work State: ${workInfo.state}")
            }
        } else {
            Log.d(Constants.WORK_MANAGER_STATUS_NOTIFY, "No Work Found")
        }
    }

}

