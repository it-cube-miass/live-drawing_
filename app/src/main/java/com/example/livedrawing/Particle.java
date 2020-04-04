package com.example.livedrawing;

import android.graphics.PointF;

public class Particle {
    PointF velocity;
    PointF position;

    Particle(PointF direction) {
        velocity = new PointF();
        position = new PointF();

        velocity.x = direction.x;
        velocity.y = direction.y;
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
}
