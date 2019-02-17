package core;

public class RGB {

    float r, g, b;

    public RGB(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public RGB(RGB rgb) {
        this.r = rgb.getR();
        this.g = rgb.getG();
        this.b = rgb.getB();
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        if (r > 1f)
            this.r = 1f;
        else if (r < 0)
            this.r = 0;
        else
            this.r = r;
    }

    public float getG() {
        return g;
    }

    public void setG(float g) {
        if (g > 1f)
            this.g = 1f;
        else if (g < 0)
            this.g = 0;
        else
            this.g = g;
    }

    public float getB() {
        return b;
    }

    public void setB(float b) {
        if (b > 1f)
            this.b = 1f;
        else if (b < 0)
            this.b = 0;
        else
            this.b = b;
    }

    public void scale(float s) {
        r *= s;
        g *= s;
        b *= s;
    }
}
