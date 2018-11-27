package world;

public abstract class Matrix3D {

    protected float[][] data;

    public float[][] getData() {
        return data;
    }

    public void stretch(float s) {
        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < data[0].length; j++)
                data[i][j] *= s;
    }

    public void shrink(float s) {
        stretch(1/s);
    }
}
