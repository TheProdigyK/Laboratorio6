package com.example.laboratorio6;

public class RectanguloChoque {
    public float x, y, w, h;

    public RectanguloChoque(float a, float b, float c, float d) {
        x = a;
        y = b;
        w = c;
        h = w;
    }

    public boolean enRectangulo(float xp, float yp) {
        return (x <= xp && xp <= x + w) && (y <= yp && yp <= y + h);
    }
}
