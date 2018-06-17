package com.example.hospital.firstmenu;

import android.app.AuthenticationRequiredException;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hospital.firstmenu.Server.PushData;
import com.example.hospital.firstmenu.Server.UpdateData;
import com.example.hospital.firstmenu.kakaoData.Request;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.response.model.User;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "Login Acting";
    private SessionCallback callback;
    LoginRequest loginRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

        if (getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            if (Session.getCurrentSession().isOpened()) { // 한 번더 세션을 체크해주었습니다.
                loginRequest = new LoginRequest();
                loginRequest.requestMe();
                loginRequest.requestUpdateScore(0);
                loginRequest.requestUpdateBasicProfile();
            }
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Log.e(TAG, "onSessionOpenFailed");
                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class LoginRequest extends Request {
        @Override
        public void requestMeSuccess() {
            super.requestMeSuccess();

            UserProfile userProfile = UserProfile.loadFromCache();
            String id = Long.toString(userProfile.getId());
            String name = userProfile.getNickname();
            String profileUrl = userProfile.getProfileImagePath();
            Log.i(TAG, "profile : " + profileUrl);

            PushData task = new PushData();
            task.execute(id, name, profileUrl);

            redirectMainActivity();
        }
    }

    public void redirectMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() { // 앱 종료
        finish();
    }

}
