package com.esmaeel.appstate

import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView

class ActivityOneScreen : Screen<ActivityOneScreen>() {
    val currentActivityTextView = KTextView { withId(R.id.currentActivityText) }
    val currentNetworkStateTextView = KTextView { withId(R.id.currentNetworkState) }
    val nextButton = KButton { withId(R.id.nextButton) }
}