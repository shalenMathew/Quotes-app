package com.example.quotesapp.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.work.WorkManager
import com.example.quotesapp.presentation.MainActivity

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

