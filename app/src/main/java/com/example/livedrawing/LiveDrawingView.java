package com.example.livedrawing;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

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

    private ArrayList<ParticleSystem> particleSystems = new ArrayList<>();
    private int nextSystem = 0;
    private final int MAX_SYSTEM = 1000;
    private int particlesPerSystem = 100;

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

        for (int i = 0; i < MAX_SYSTEM; i++) {
            particleSystems.add(new ParticleSystem());
            particleSystems.get(i).init(particlesPerSystem);
        }
    }

    private void draw() {
        if (holder.getSurface().isValid()) {
            canvas = holder.lockCanvas();
            canvas.drawColor(Color.BLACK);
            paint.setColor(Color.WHITE);
            paint.setTextSize(fontSize);

            for (int i = 0; i < particleSystems.size(); i++) {
                particleSystems.get(i).draw(canvas, paint);
            }

            paint.setColor(Color.WHITE);
            canvas.drawRect(resetButton, paint);
            canvas.drawRect(togglePauseButton, paint);
            if (DEBUGGING) {
                printDebugginText();
            }
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void printDebugginText() {
        paint.setColor(Color.WHITE);
        int debugSize = fontSize;
        int startX = 150;
        int startY = marginSize;
        int y1 = startY + debugSize;
        int y2 = y1 + marginSize + debugSize;
        int y3 = y2 + marginSize + debugSize;
        paint.setTextSize(debugSize);
        canvas.drawText("FPS: " + FPS, startX, y1, paint);
        canvas.drawText("Systems: "+ nextSystem, startX, y2, paint);
        canvas.drawText("Particles: " + nextSystem * particlesPerSystem, startX, y3, paint);
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
        for (int i = 0; i < particleSystems.size(); i++) {
            if (particleSystems.get(i).isRunning) {
                particleSystems.get(i).update(FPS);
            }
        }
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_MOVE ) {
            particleSystems.get(nextSystem).emitParticles(new PointF(event.getX(), event.getY()));

            nextSystem++;
            if (nextSystem >= MAX_SYSTEM) {
                nextSystem = 0;
            }
        }

        if ((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN) {
            if (resetButton.contains(event.getX(), event.getY())) {
                nextSystem = 0;
            }

            if (togglePauseButton.contains(event.getX(), event.getY())) {
                paused = !paused;
            }
        }
        return true;
    }
}
