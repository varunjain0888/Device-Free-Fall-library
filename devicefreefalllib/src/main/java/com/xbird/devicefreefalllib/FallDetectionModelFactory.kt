package com.xbird.devicefreefalllib

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xbird.devicefreefalllib.database.CounterViewModel

/**
 * Created by varun jain on 2/12/2020.
 */
@Suppress("UNCHECKED_CAST")
class FallDetectionModelFactory : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CounterViewModel() as T
    }
}