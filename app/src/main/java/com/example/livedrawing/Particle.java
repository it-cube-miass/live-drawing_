package com.example.livedrawing;

import android.graphics.PointF;

public class Particle {
    private PointF velocity;
    private PointF position;
    private int color;

    Particle(PointF direction, int color) {
        velocity = new PointF();
        position = new PointF();

        velocity.x = direction.x;
        velocity.y = direction.y;
        this.color = color;
    }

    void update(float fps) {
        position.x += velocity.x;
        position.y += velocity.y;
    }

    void setPosition(PointF position) {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    PointF getPosition()  {
        return position;
    }

    int getColor() {
        return color;
    }
}
