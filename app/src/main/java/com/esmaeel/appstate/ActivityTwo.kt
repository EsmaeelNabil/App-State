package com.esmaeel.appstate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.esmaeel.appstate.databinding.ActivityTwoBinding
import com.esmaeel.statelib.appHasNetwork
import com.esmaeel.statelib.currentActivity

class ActivityTwo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(ActivityTwoBinding.inflate(layoutInflater)) {
            setContentView(this.root)

            currentActivityText.text = "${currentActivity?.componentName?.className}"

            currentNetworkState.text = "$appHasNetwork"

            currentNetworkState.setOnClickListener {
                currentNetworkState.text = "$appHasNetwork"
            }

            previousButton.setOnClickListener {
                startActivity(
                    Intent(
                        this@ActivityTwo,
                        ActivityOne::class.java
                    )
                )
                finish()
            }

        }
    }
}