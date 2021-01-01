package com.esmaeel.appstate

import android.Manifest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test


class ActivityOneTest : TestCase() {

    val activityOneScreen = ActivityOneScreen()

    @get:Rule
    val activityTestRule = ActivityTestRule(ActivityOne::class.java, true, true)

    @get:Rule
    val runtimePermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

    @Test
    fun make_sure_current_activity_gets_updated() = run {
        activityOneScreen {

            step("make sure activity name is present") {
                currentActivityTextView.containsText("ActivityOne")
            }

            step("verify moving to next activity updates currentActivity to the new one") {
                nextButton.click()
                currentActivityTextView.containsText("ActivityTwo")
            }

            step("verify moving to Previous activity updates currentActivity to the old one") {
                pressBack()
                currentActivityTextView.containsText("ActivityOne")
            }
        }
    }


    @Test
    fun make_sure_app_has_network_gets_updated() =
        before { device.network.enable() }.after { }.run {

            activityOneScreen {

//                step("turn on wifi") {
//                    device.network.enable()
//                }

                step("make sure network state equals true while wifi is on") {
                    currentNetworkStateTextView.containsText("true")
                }

                step("turn off wifi") {
                    device.network.disable()
                }

                step("make sure network state equals false while wifi is off") {
                    currentNetworkStateTextView.containsText("false")
                }
            }
        }
}