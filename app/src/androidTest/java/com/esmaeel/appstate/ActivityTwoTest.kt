package com.esmaeel.appstate

import com.esmaeel.statelib.appHasNetwork
import com.esmaeel.statelib.currentActivity
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.junit.Test


class ActivityTwoTest : TestCase() {

    val activityTwoScreen = ActivityTwoScreen()
    val activityOneScreen = ActivityOneScreen()


    @Test
    fun activity_two_tests() = run {

        activityTwoScreen {

            step("open activity two") {
                goto(ActivityTwo::class.java)
            }

            step("make sure current activity text is present and contains the name of the class") {
                currentActivityText.containsText("ActivityTwo")
                val currentName = currentActivity?.componentName?.className ?: ""
                val expected = "ActivityTwo"
                assertTrue(currentName.contains(expected, ignoreCase = true))
            }
        }
    }

    @Test
    fun activity_two_moving_to_activity_one_testing() = before {
        // opening the activity
        goto(ActivityTwo::class.java)
    }.after {}.run {

        step("performing a click on the previous button") {
            activityTwoScreen {
                previousButton.click()
            }
        }

        step("make sure current activity text is present and contains the name of the class `ActivityOne`") {
            activityOneScreen {
                currentActivityTextView.containsText("ActivityOne")
            }
        }

        step("making sure that currentActivity variable got updated to hold the ActivityOne ") {

            // generating the test data - opening the activity and changing the current value
            // current value
            val currentValue = currentActivity?.componentName?.className ?: ""

            // expected value
            val expected = "ActivityOne"

            // assertion
            assertTrue(currentValue.contains(expected, ignoreCase = true))
        }

    }

    @Test
    fun make_sure_app_has_network_gets_updated_in_true_scenario() =
        before {
            goto(ActivityOne::class.java)
            device.network.toggleWiFi(true)

        }.after {
            device.network.toggleWiFi(true)
        }.run {

            activityTwoScreen {
                step("click on the network state textView to get the current statues") {
                    networkStateText.click()

                    step("check for the current statue to be true") {
                        networkStateText.containsText("true")
                    }
                }

                step("check for the current statue variable to be true") {
                    assertTrue(appHasNetwork)
                }

            }
        }


    @Test
    fun make_sure_app_has_network_gets_updated_in_false_scenario() =
        before {
            goto(ActivityOne::class.java)
            // make sure that The Cellular data is OFF cus our appHasNetwork listens for all the networks
            device.network.toggleWiFi(false)

        }.after {
            device.network.toggleWiFi(false)
        }.run {

            activityOneScreen {
                step("click on the network state textView to get the current statues") {

                    currentNetworkStateTextView.click()

                    step("check for the current statue to be true") {
                        currentNetworkStateTextView.containsText("false")
                    }
                }

                step("check for the current statue variable to be true") {
                    assertFalse(appHasNetwork)
                }

            }
        }


    @Test
    fun make_sure_app_has_network_gets_updated_while_moving_to_another_activity() =
        before {
            goto(ActivityOne::class.java)
            // make sure that The Cellular data is OFF cus our appHasNetwork listens for all the networks
            device.network.toggleWiFi(false)

        }.after {
            device.network.toggleWiFi(true)
        }.run {


            activityOneScreen {

                step("check for the current statue variable to be false") {
                    assertFalse(appHasNetwork)
                }

                step("click on the network state textView to update the current status") {
                    currentNetworkStateTextView.click()
                }

                step("check for the current status to be false") {
                    currentNetworkStateTextView.containsText("false")
                }



                step("Move to another activity with wifi enabled") {

                    step("enable wifi") {
                        device.network.toggleWiFi(true)
                    }

                    step("moving to activityTwo") {
                        goto(ActivityTwo::class.java)
                    }

                    step("check for the current statue variable to be true") {
                        assertTrue(appHasNetwork)
                    }

                    step("check for the current statue to be true") {
                        currentNetworkStateTextView.containsText("true")
                    }

                }
            }
        }

}