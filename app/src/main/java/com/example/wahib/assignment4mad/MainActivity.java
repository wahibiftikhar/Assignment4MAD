package com.example.wahib.assignment4mad;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    Button btnStart, btnStop;
    TextView textView;
    EditText limit;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Event event) {
        int value = event.getPercentage();
        textView.setText(value + "%");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button) findViewById(R.id.startService);
        textView = (TextView) findViewById(R.id.percentage);
        limit = (EditText) findViewById(R.id.enterlimit);
        EventBus.getDefault().register(this);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int val = Integer.valueOf(limit.getText().toString());
                Intent intent = new Intent(MainActivity.this, MyService.class);
                intent.putExtra("limit", val);
                startService(intent);
            }
        });

        btnStop = (Button) findViewById(R.id.stopService);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, MyService.class);
                stopService(intent1);
            }
        });


    }
}