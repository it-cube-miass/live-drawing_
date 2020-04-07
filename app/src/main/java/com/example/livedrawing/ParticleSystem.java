package com.example.livedrawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.Random;

public class ParticleSystem {
    private float duration;

    private ArrayList<Particle> particles;
    private Random random = new Random();
    boolean isRunning = false;

    void init(int numParticles) {
        particles = new ArrayList<>();

        for (int i = 0; i < numParticles; i++) {
            float angle = random.nextInt(360);
            angle = angle * 3.14f / 180.f;

//            float speed = (random.nextFloat() / 10); // медленные частицы
            float speed = (random.nextInt(10) + 2); // быстрые частицы

            PointF direction = new PointF((float)Math.cos(angle) * speed, (float)Math.sin(angle) * speed);
            int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            particles.add(new Particle(direction, color));

        }
    }

    void update(long fps) {
        duration -= 1f/fps;

        for (Particle p : particles) {
            p.update(fps);
        }

        if (duration < 0) {
            isRunning = false;
        }
    }

    void emitParticles(PointF startPosition) {
        isRunning = true;
//        duration = 30f; // 30 секунд
        duration = 3f; // 3 секунды

        for (Particle p : particles) {
            p.setPosition(startPosition);
        }
    }

    void draw(Canvas canvas, Paint paint) {
        if (isRunning) {
            for (Particle p : particles) {
    //            paint.setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
                paint.setColor(p.getColor());

                // размер частиц
                float sizeX = 10;
                float sizeY = 10;

                float x = p.getPosition().x;
                float y = p.getPosition().y;
                canvas.drawRect(x, y, x + sizeX, y + sizeY, paint);
            }
        }
    }
}
