package com.ashiswin.morbidity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ashiswin.morbidity.utils.LayoutUtils;

public class LaunchScreen extends AppCompatActivity {
    private static final int GET_STARTED_INTENT = 0;

    ImageView imgLogo;
    Button btnGetStarted;
    TextView txtTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        imgLogo = findViewById(R.id.imgLogo);
        btnGetStarted = findViewById(R.id.btnGetStarted);
        txtTitle = findViewById(R.id.txtTitle);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgLogo.animate().scaleX(0.5f).scaleY(0.5f).translationY(-LayoutUtils.convertDip2Pixels(LaunchScreen.this, 192));
                btnGetStarted.setVisibility(View.VISIBLE);
                txtTitle.setVisibility(View.VISIBLE);
                btnGetStarted.animate().alpha(1).setDuration(500);
                txtTitle.animate().translationY(-LayoutUtils.convertDip2Pixels(LaunchScreen.this, 240)).setDuration(0);
                txtTitle.animate().alpha(1).setDuration(500);
            }
        }, 1000);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getStartedIntent = new Intent(LaunchScreen.this, GetStartedActivity.class);
                startActivityForResult(getStartedIntent, GET_STARTED_INTENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GET_STARTED_INTENT && resultCode == RESULT_OK) {
            Intent homeIntent = new Intent(LaunchScreen.this, HomeActivity.class);
            startActivity(homeIntent);
            finish();
        }
    }
}
