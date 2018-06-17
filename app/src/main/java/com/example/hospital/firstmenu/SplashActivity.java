package com.example.hospital.firstmenu;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.hospital.firstmenu.kakaoData.Request;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new CheckLogin().requestMe();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public class CheckLogin extends Request {
        @Override
        public void requestMeSuccess() {
            super.requestMeSuccess();
            redirectMainActivity();
        }

        @Override
        public void redirectLoginActivity() {
            super.redirectLoginActivity();
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            startActivity(intent);
            finish();
        }

        public void redirectMainActivity() {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            startActivity(intent);
            finish();
        }
    }
}
