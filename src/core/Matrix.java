package core;

import java.util.Arrays;

public abstract class Matrix {

    protected double[][] data;

    public double[][] getData() {
        return data;
    }

    public double[] getDataLinear(boolean byRows) {
        int index = 0, columns = data.length, rows = data[0].length;
        double[] arr = new double[columns*rows];

        if (byRows)
            for (int i = 0; i < rows; i++)
                for (int j = 0; j < columns; j++)
                    arr[index++] = data[j][i];
        else
            for (int i = 0; i < columns; i++)
                for (int j = 0; j < rows; j++)
                    arr[index++] = data[i][j];

        return arr;
    }

    public void scale(double s) {
        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < data[0].length; j++)
                data[i][j] *= s;
    }

    public double[] getColumn(int column) {
        return data[column];
    }

    public String toString() {
        return Arrays.deepToString(data);
    }
}
