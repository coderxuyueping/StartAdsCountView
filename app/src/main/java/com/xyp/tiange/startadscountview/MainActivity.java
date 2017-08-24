package com.xyp.tiange.startadscountview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private StartAdsCountView countView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countView = (StartAdsCountView)findViewById(R.id.count_view);
    }

    public void start(View v){
        countView.startAni();
    }

}
