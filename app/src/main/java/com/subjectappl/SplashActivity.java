package com.subjectappl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends Activity {
    Intent mainActivityIntent, loginIntent = null;
    Activity activity;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mainActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
        loginIntent = new Intent(SplashActivity.this, LoginActivity.class);

        countDownTimer = new CountDownTimer(3500, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (SubjectApplication.data.loadData("user_login").equals("")) {
                    SplashActivity.this.startActivity(loginIntent);
                    SplashActivity.this.finish();
                    SplashActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    SplashActivity.this.startActivity(mainActivityIntent);
                    SplashActivity.this.finish();
                    SplashActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        };
        countDownTimer.start();
    }
}
