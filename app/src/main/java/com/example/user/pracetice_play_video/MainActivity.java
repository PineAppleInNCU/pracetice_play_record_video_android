package com.example.user.pracetice_play_video;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button recording;
    private Button watch_video;
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        init();

    }
    private void init(){
        recording = (Button)findViewById(R.id.button);
        watch_video=(Button)findViewById(R.id.button2);

        recording.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, recording.class);
                startActivity(intent);
            }
        });
        watch_video.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, select_video.class);
                startActivity(intent);
            }
        });
    }
}
