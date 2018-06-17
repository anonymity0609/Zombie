package com.example.hospital.firstmenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.hospital.firstmenu.Server.DeleteData;
import com.example.hospital.firstmenu.Server.DeleteFriendData;
import com.example.hospital.firstmenu.Server.UpdateData;
import com.example.hospital.firstmenu.kakaoData.Request;
import com.kakao.kakaotalk.callback.TalkResponseCallback;
import com.kakao.kakaotalk.response.KakaoTalkProfile;
import com.kakao.kakaotalk.v2.KakaoTalkService;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    public String TAG = "Login " + getClass().getSimpleName();
    Update updateData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateData = new Update();
        updateData.refreshData();

        // 스테이지 1, 솔로 플레이 시작 버튼
        Button SoloBtn = (Button) findViewById(R.id.gameStartGtn);
        SoloBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToStage();
            }
        });

        // 랭킹 확인 버튼. RankingActivity로 이동함.
        Button rankBtn = (Button) findViewById(R.id.RankingBtn);
        rankBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkRanking();
            }
        });

        // 친구 관리 버튼. FriendActivity로 이동함
        Button friendBtn = (Button) findViewById(R.id.friendBtn);
        friendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adminFreindList();
            }
        });

        // 로그아웃 버튼. 로그인 화면으로 다시 돌아감.
        Button logOutBtn = (Button) findViewById(R.id.LogoutBtn);
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
        });

        // 게임 종료 버튼
        Button QuitGameBtn = (Button) findViewById(R.id.QuitGameBtn);
        QuitGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 데이터를 새로고침
        Button refreshData = (Button) findViewById(R.id.refreshData);
        refreshData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
    }

    public void Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setTitle("로그아웃")
                .setMessage("정말 로그아웃 하시겠습니까? 게임 데이터가 사라집니다")
                .setPositiveButton("예, 로그아웃 합니다.", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onClickLogout();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "종료하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void refresh() {
        updateData.refreshData();
    }

    // 카카오톡 정보를 불러오는 클래스
    public class Update extends Request {
        private String profileImgUrl = null;
        private String thumbnailUrl = null;

        public void refreshData() {
            requestProfile();
        }

        abstract class KakaoTalkResponseCallback<T> extends TalkResponseCallback<T> {
            @Override
            public void onNotKakaoTalkUser() {
                Log.e(TAG, "onNotKakaoTalkUser");
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.e(TAG, "onFailure");
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.e(TAG, "onSessionClosed");
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
                Log.e(TAG, "onNotSignedUp");
            }
        }

        // 카카오톡에 저장되어 있는 사용자의 nickName과 profile을 불러옴
        public void requestProfile() {
            KakaoTalkService.getInstance().requestProfile(new KakaoTalkResponseCallback<KakaoTalkProfile>() {
                @Override
                public void onSuccess(KakaoTalkProfile talkProfile) {
                    profileImgUrl = talkProfile.getProfileImageUrl();
                    thumbnailUrl = talkProfile.getThumbnailUrl();
                }
            });

            UserProfile userProfile = UserProfile.loadFromCache();
            String name = userProfile.getProperty("gameName");
            if (profileImgUrl == null) profileImgUrl = userProfile.getProfileImagePath();
            if (thumbnailUrl == null) thumbnailUrl = userProfile.getThumbnailImagePath();
            String score = userProfile.getProperty("highest_score");
            requestUpdateProfile(name, profileImgUrl, thumbnailUrl, Integer.parseInt(score));
        }

        @Override
        public void redirectLoginActivity() {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            finish();
            Log.d("userdata", "redirectLoginActivity");
        }
    }

    private void adminFreindList() {
        Intent intent = new Intent(this, FriendActivity.class);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        startActivity(intent);
    }

    public void goToStage() {
//        Intent intent = new Intent(MainActivity.this, UnityPlayerActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
//        startActivity(intent);
    }

    private void checkRanking() {
        Intent intent = new Intent(this, RankingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        startActivity(intent);
    }

    private void onClickLogout() {
        UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.d("onclickLogout", "onCompleteLogout");
                DeleteData task = new DeleteData();
                task.execute(UserProfile.loadFromCache().getNickname());
                updateData.redirectLoginActivity();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed(); 앱 종료를 Quit 버튼으로 확실하게 하려고 백 버튼 막기.
    }
}
