package com.jiushig.otlocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.jiushig.location.entity.Location;
import com.jiushig.location.location.LocationBuilder;
import com.jiushig.location.ui.MapActivity;
import com.jiushig.location.ui.SelectActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(v ->
                new LocationBuilder(this)
                        .setmaxErrorNum(3)
                        .setType(LocationBuilder.TYPE.GAO_DE)
                        .setCallbackSuccess(locationInfo -> Toast.makeText(MainActivity.this, locationInfo.toString(), Toast.LENGTH_LONG).show())
                        .setCallbackFail(msg -> Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show())
                        .start()
        );

        findViewById(R.id.button1).setOnClickListener(view -> {
            MapActivity.start(this, 30.63, 106.07);
        });

        findViewById(R.id.button2).setOnClickListener(view -> {
            SelectActivity.start(this,"不选择位置", 0xc5b278);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SelectActivity.REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Location location = (Location) data.getSerializableExtra("info");
                if (location != null)
                    Toast.makeText(MainActivity.this, location.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
