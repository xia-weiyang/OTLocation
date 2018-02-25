package com.jiushig.otlocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jiushig.location.location.LocationGaode;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(v->{
            new LocationGaode(this).setCallbackSuccess(locationInfo -> {
                Toast.makeText(MainActivity.this,locationInfo.toString(),Toast.LENGTH_LONG).show();
            }).setCallbackFail(msg -> {
                Toast.makeText(MainActivity.this,msg,Toast.LENGTH_LONG).show();
            }).start();
        });
    }
}
