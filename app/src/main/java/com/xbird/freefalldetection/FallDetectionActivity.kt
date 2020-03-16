package com.xbird.freefalldetection

import android.os.Bundle
import androidx.lifecycle.Observer
import com.xbird.devicefreefalllib.*
import kotlinx.android.synthetic.main.activity_fall_detection.*


class FallDetectionActivity : DetectionActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fall_detection)
        countViewModel.countRepo.observe(this, Observer {
            tvCount.text = "Total Fall Count "+it
        })
    }
}
