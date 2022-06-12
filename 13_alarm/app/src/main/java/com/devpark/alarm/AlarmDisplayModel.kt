package com.devpark.alarm

/**
 * 코틀린 데이타 모델은 생산자 안에 인자들이 들어와야 한다.
 */
data class AlarmDisplayModel(
    val hour: Int,
    val minute: Int,
    var onOff: Boolean
) {

    val timeText: String
        get() {
            val h = "%02d".format(if (hour < 12) hour else hour - 12)
            val m = "%02d".format(minute)

            return "$h:$m"
        }

    val ampmText: String
        get() {
            return if (hour < 12) "AM" else "PM"
        }

    val onOffText: String
        get() {
            return if(onOff) "알림 끄기" else "알림 켜기"
        }

    fun makeDataForDB(): String {
        return "$hour:$minute"
    }
}
