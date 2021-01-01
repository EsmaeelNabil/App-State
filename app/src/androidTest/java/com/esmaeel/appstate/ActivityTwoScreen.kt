package com.esmaeel.appstate

import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView

class ActivityTwoScreen : Screen<ActivityTwoScreen>() {
    val currentActivityTextView = KTextView { withId(R.id.currentActivityText) }
    val currentNetworkStateTextView = KTextView { withId(R.id.currentNetworkState) }
    val previousButton = KButton { withId(R.id.previousButton) }
}