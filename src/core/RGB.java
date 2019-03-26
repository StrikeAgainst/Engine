package core;

import com.jogamp.opengl.GL2;

import java.util.Random;

public class RGB {

    double r, g, b;

    public RGB(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public RGB(RGB rgb) {
        this.r = rgb.getR();
        this.g = rgb.getG();
        this.b = rgb.getB();
    }

    public static RGB getRandom() {
        Random random = new Random();
        return new RGB(random.nextDouble(), random.nextDouble(), random.nextDouble());
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        if (r > 1)
            this.r = 1;
        else if (r < 0)
            this.r = 0;
        else
            this.r = r;
    }

    public double getG() {
        return g;
    }

    public void setG(double g) {
        if (g > 1)
            this.g = 1;
        else if (g < 0)
            this.g = 0;
        else
            this.g = g;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        if (b > 1)
            this.b = 1;
        else if (b < 0)
            this.b = 0;
        else
            this.b = b;
    }

    public void scale(double s) {
        r *= s;
        g *= s;
        b *= s;
    }

    public void setForGL(GL2 gl) {
        gl.glColor3d(r, g, b);
    }

    public String toString() {
        return "Color:[r="+r+", g="+g+", b="+b+"]";
    }
}
