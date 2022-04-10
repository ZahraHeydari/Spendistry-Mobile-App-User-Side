package com.shashank.spendistry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.motion.widget.MotionLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class splashScreenActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);
        sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "not");
        boolean loggedIn = sharedPreferences.getBoolean("loggedIn", false);
        MotionLayout motionLayout = findViewById(R.id.motionLayout);
        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                Intent intent;
                if (loggedIn) {
                    intent = new Intent(splashScreenActivity.this, DashboardActivity.class);
                    intent.putExtra("email", email);
                } else {
                    intent = new Intent(splashScreenActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });
    }
}
