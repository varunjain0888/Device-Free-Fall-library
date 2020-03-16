package com.xbird.devicefreefalllib.database

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.xbird.devicefreefalllib.database.Count
import com.xbird.devicefreefalllib.database.XbirdDatabase
import com.xbird.devicefreefalllib.executors.AppExecutors

class CounterViewModel : ViewModel() {

    val countRepo = MutableLiveData<Long>()
    fun getCount(context:Context){
        var count :Long?=0
        AppExecutors.getInstance().diskIO().execute {
            count = XbirdDatabase.getDatabase(context).count().getSum()
            countRepo.postValue(count)
        }
    }
   /* fun insertCount(context:Context){

    }*/
}