package com.devpark.firebasepush

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    /**
     * 다음과 같은 경우에 등록 토큰이 변경될 수 있습니다.
     * 새 기기에서 앱 복원
     * 사용자가 앱 삭제/재설치
     * 사용자가 앱 데이터 소거
     *
     * 토큰이 언제든지 변경 될 수 있기 때문에 실제 라이브 서비스를 운영할 때는
     * onNewToken를 오버라이딩 해서 이 토큰이 갱신 될 때마다 서버에다 해당 토큰을 갱신해 주어야 한다.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


    /**
     * Firebase에서 넘어오는 메시지 처리
     */
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

    }

}