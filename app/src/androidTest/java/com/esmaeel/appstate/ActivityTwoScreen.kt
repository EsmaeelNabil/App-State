package com.esmaeel.appstate

import com.agoda.kakao.screen.Screen
import com.agoda.kakao.text.KButton
import com.agoda.kakao.text.KTextView

class ActivityTwoScreen : Screen<ActivityTwoScreen>() {
    val previousButton = KButton { withId(R.id.previousButton) }
    val networkStateText = KTextView { withId(R.id.currentNetworkState) }
    val currentActivityText = KTextView { withId(R.id.currentActivityText) }
}