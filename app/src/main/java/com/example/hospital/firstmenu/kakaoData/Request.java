package com.example.hospital.firstmenu.kakaoData;

import android.content.Context;
import android.content.Intent;
import android.icu.lang.UProperty;
import android.util.Log;

import com.example.hospital.firstmenu.Server.PushData;
import com.example.hospital.firstmenu.Server.UpdateData;
import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.AuthService;
import com.kakao.auth.network.response.AccessTokenInfoResponse;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Admin on 2018-05-20.
 */

public class Request {
    public String TAG = "Request";

    // 앱에 로그인한 사용자의 정보를 불러옴
    public void requestMe() {
        UserManagement.getInstance().requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.e(TAG, "onFailure : " + message);
                redirectLoginActivity();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG, "onSessionClosed");
                redirectLoginActivity();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.i(TAG, "onSuccess : " + userProfile.toString());
                userProfile.saveUserToCache();
                requestMeSuccess();
            }

            @Override
            public void onNotSignedUp() {
                Log.d(TAG, "onNotSignedUp");
            }
        });
    }

    // 서비스와 서버에 저장되어 있는 정보를 카카오톡 정보로 업데이트
    public void requestUpdateProfile(String name, String profileImgUrl, String thumbnailUrl, int score) {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("nickname", name);
        properties.put("profile_image", profileImgUrl);
        properties.put("thumbnail_image", thumbnailUrl);
        properties.put("highest_score", Integer.toString(score));

        final String user_score = Integer.toString(score);
        final String user_url = profileImgUrl;

        UserManagement.getInstance().requestUpdateProfile(new ApiResponseCallback<Long>() {
            @Override
            public void onSuccess(Long userId) {
                UserProfile profile = UserProfile.loadFromCache();
                profile.updateUserProfile(properties);
                if (profile != null) {
                    profile.saveUserToCache();
                    // 서버에 갱신된 정보를 전달
                    UpdateData task = new UpdateData();
                    task.execute(user_score, user_url, Long.toString(userId));
                }
                String msg = "update user profile" + profile.toString();
                Log.i(TAG, msg);
            }

            @Override
            public void onNotSignedUp() {
                Log.e(TAG, "onNotSignedUp");
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.e(TAG, message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG, "onSessionClosed");
                redirectLoginActivity();
            }
        }, properties);
    }

    public void requestUpdateBasicProfile () {
        UserProfile userProfile = UserProfile.loadFromCache();
        final String profileImgUrl = userProfile.getProfileImagePath();
        if (profileImgUrl != null)
            return;

        final String basicProfile = "https://preview.ibb.co/dULpbT/image.png";
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("profile_image", basicProfile);
        properties.put("thumbnail_image", basicProfile);

        UserManagement.getInstance().requestUpdateProfile(new ApiResponseCallback<Long>() {
            @Override
            public void onSuccess(Long userId) {
                UserProfile profile = UserProfile.loadFromCache();
                profile.updateUserProfile(properties);
                if (profile != null) {
                    profile.saveUserToCache();
                    // 서버에 갱신된 정보를 전달
                    UpdateData task = new UpdateData();
                    task.execute(profile.getProperty("highest_score"), basicProfile, Long.toString(userId));
                }
                String msg = "update user profile " + profile.toString();
                Log.d(TAG, msg);
            }

            @Override
            public void onNotSignedUp() {
                Log.e(TAG, "onNotSignedUp");
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.e(TAG, message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG, "onSessionClosed");
                redirectLoginActivity();
            }
        }, properties);
    }

    public void requestUpdateScore(int score) {
        final Map<String, String> properties = new HashMap<String, String>();
        properties.put("highest_score", Integer.toString(score));

        final String user_score = Integer.toString(score);
        UserManagement.getInstance().requestUpdateProfile(new ApiResponseCallback<Long>() {
            @Override
            public void onSuccess(Long userId) {
                UserProfile profile = UserProfile.loadFromCache();
                profile.updateUserProfile(properties);
                if (profile != null) {
                    profile.saveUserToCache();
                    // 서버에 갱신된 정보를 전달
                    UpdateData task = new UpdateData();
                    task.execute(user_score, profile.getProfileImagePath(), Long.toString(userId));
                }
                String msg = "update user score " + profile.getProperty("highest_score");
                Log.d(TAG, msg);
            }

            @Override
            public void onNotSignedUp() {
                Log.e(TAG, "onNotSignedUp");
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Log.e(TAG, message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG, "onSessionClosed");
                redirectLoginActivity();
            }
        }, properties);
    }

    public void requestMeSuccess() {
    }

    public void redirectLoginActivity() {
    }
}
