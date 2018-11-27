package world;

public class ONB3D {

    private Vector3D x, y;

    public ONB3D(Vector3D x, Vector3D y) throws ONBException {
        if (Vector3D.dot(x, y) != 0)
            throw new ONBException("Vectors x and y are not orthogonal!");

        this.x = x.normalize();
        this.y = y.normalize();
    }

    public Vector3D[] getVectors() {
        return new Vector3D[] {x, y, Vector3D.cross(x, y)};
    }
}
