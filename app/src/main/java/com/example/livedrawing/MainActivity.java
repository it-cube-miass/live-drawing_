package com.example.livedrawing;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;

public class MainActivity extends AppCompatActivity {
    private LiveDrawingView liveDrawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        liveDrawingView = new LiveDrawingView(this, size.x, size.y);

        setContentView(liveDrawingView);
        String s ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        liveDrawingView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        liveDrawingView.pause();
    }
}
