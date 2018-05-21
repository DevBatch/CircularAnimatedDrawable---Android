package com.db.circularanimateddrawable;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.db.circularanimateddrawable.interfaces.OnAnimationEndListener;
import com.db.circularanimateddrawable.widgets.CircularProgressButton;

public class MainActivity extends AppCompatActivity {
    CircularProgressButton circularProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circularProgressButton = findViewById(R.id.progressButton);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // circularProgressButton.revertAnimation();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        circularProgressButton.revertAnimation(new OnAnimationEndListener() {

                            @Override
                            public void onAnimationEnd() {
                                Toast.makeText(getBaseContext(), "Animation End", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }, 15000);
                circularProgressButton.startAnimation();
            }
        });
    }
}
