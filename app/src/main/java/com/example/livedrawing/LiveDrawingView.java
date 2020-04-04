package com.example.livedrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class LiveDrawingView extends SurfaceView implements Runnable {
    private final boolean DEBUGGING = true;
    private SurfaceHolder holder;
    private Canvas canvas;
    private Paint paint;

    private long FPS;
    private final int MILLIS_IN_SECONDS = 1000;

    private int screenX;
    private int screenY;
    private int fontSize;
    private int marginSize;

    private Thread thread = null;
    private volatile boolean drawing;
    private boolean paused = true;

    private RectF resetButton;
    private RectF togglePauseButton;

    public LiveDrawingView(Context context, int x, int y) {
        super(context);
        screenX = x;
        screenY = y;
        fontSize = screenX / 20;
        marginSize = screenX / 75;
        holder = getHolder();
        paint = new Paint();

        resetButton = new RectF(0, 0, 100, 100);
        togglePauseButton = new RectF(0, 150, 100, 250);
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);
            paint.setTextSize(fontSize);

            canvas.drawRect(resetButton, paint);
            canvas.drawRect(togglePauseButton, paint);
            if (DEBUGGING) {
                printDebugginText();
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void printDebugginText() {
        int debugSize = fontSize;
        int debugStart = 150;
        paint.setTextSize(debugSize);
        canvas.drawText("FPS: " + FPS, 10, debugStart + debugSize, paint);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }

    @Override
    public void run() {
        while (drawing) {
            long frameStartTime = System.currentTimeMillis();
            if (paused) {
                update();
            }
            draw();

            long timeThisFrame = System.currentTimeMillis() - frameStartTime;
            if (timeThisFrame > 0) {
                FPS = MILLIS_IN_SECONDS / timeThisFrame;
            }
        }
    }

    private void update() {
    }

    public void pause() {
        drawing = false;
        try {
            thread.join();
        } catch (Exception e) {
            Log.e("Error:","joining thread");
        }
    }

    public void resume() {
        drawing = true;
        thread = new Thread(this);
        thread.start();
    }
}
