package com.sd.www.animator;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickRocketDemo(View v)
    {
        startActivity(new Intent(this, RocketDemoActivity.class));
    }

    public void onClickCarDemo(View v)
    {
        startActivity(new Intent(this, CarDemoActivity.class));
    }

    public void onClickCubeDemo(View v)
    {
        startActivity(new Intent(this, CubeDemoActivity.class));
    }
}
