package engine.collision;

import core.*;
import engine.collision.bounding.*;

import java.util.ArrayList;

public class ContactDetector {

    public static ArrayList<ContactProperties> BoundingOnBounding(SimpleBounding bounding1, SimpleBounding bounding2) {
        if (bounding1 instanceof BoundingSphere) {
            if (bounding2 instanceof BoundingSphere)
                return SphereOnSphere((BoundingSphere) bounding1, (BoundingSphere) bounding2);
            else if (bounding2 instanceof BoundingBox)
                return SphereOnBox((BoundingSphere) bounding1, (BoundingBox) bounding2);
        } else if (bounding1 instanceof BoundingBox) {
            if (bounding2 instanceof BoundingSphere)
                return BoxOnSphere((BoundingBox) bounding1, (BoundingSphere) bounding2);
            else if (bounding2 instanceof BoundingBox)
                return BoxOnBox((BoundingBox) bounding1, (BoundingBox) bounding2);
        }

        return new ArrayList<>();
    }

    public static ArrayList<ContactProperties> BoundingOnHalfSpace(SimpleBounding bounding, BoundingHalfSpace halfSpace) {
        if (bounding instanceof BoundingSphere)
            return SphereOnHalfSpace((BoundingSphere) bounding, halfSpace);
        else if (bounding instanceof BoundingBox)
            return BoxOnHalfSpace((BoundingBox) bounding, halfSpace);

        return new ArrayList<>();
    }

    public static ArrayList<Point3> LineOnBounding(Point3 origin, Vector3 line, SimpleBounding bounding) {
        if (bounding instanceof BoundingSphere)
            return LineOnSphere(origin, line, (BoundingSphere) bounding);
        else if (bounding instanceof BoundingBox)
            return LineOnBox(origin, line, (BoundingBox) bounding);

        return new ArrayList<>();
    }

    public static ArrayList<ContactProperties> SphereOnSphere(BoundingSphere sphere1, BoundingSphere sphere2) {
        ArrayList<ContactProperties> contacts = new ArrayList<>();

        Point3 pos1 = sphere1.getPosition(), pos2 = sphere2.getPosition();
        Vector3 offset = Vector3.offset(pos2, pos1);
        double size = offset.getEuclideanMagnitude();
        double depth = sphere1.getRadius() + sphere2.getRadius() - size;

        if (depth < 0)
            return contacts;

        Point3 contactPoint = pos1.offset(offset.scaled(1-sphere2.getRadius()/size));
        Vector3 normal = offset.scaled(1 / size);

        contacts.add(new ContactProperties(contactPoint, normal, depth));
        return contacts;
    }

    public static ArrayList<ContactProperties> SphereOnBox(BoundingSphere sphere, BoundingBox box) {
        ArrayList<ContactProperties> contacts = BoxOnSphere(box, sphere);
        for (ContactProperties c : contacts)
            c.getNormal().revert();

        return contacts;
    }

    public static ArrayList<ContactProperties> BoxOnSphere(BoundingBox box, BoundingSphere sphere) {
        ArrayList<ContactProperties> contacts = new ArrayList<>();

        Point3 spherePos = sphere.getPosition(), spherePosLocal = box.getTransformation().toLocal(spherePos);
        Vector3 halfSize = box.getHalfSize();
        double radius = sphere.getRadius(), sphereX = spherePosLocal.getX(), sphereY = spherePosLocal.getY(), sphereZ = spherePosLocal.getZ();

        if (Math.abs(spherePosLocal.getX()) - radius > halfSize.getX() || Math.abs(spherePosLocal.getY()) - radius > halfSize.getY() || Math.abs(spherePosLocal.getZ()) - radius > halfSize.getZ())
            return contacts;

        double closestX, closestY, closestZ;

        if (sphereX > halfSize.getX())
            closestX = halfSize.getX();
        else if (sphereX < -halfSize.getX())
            closestX = -halfSize.getX();
        else
            closestX = sphereX;

        if (sphereY > halfSize.getY())
            closestY = halfSize.getY();
        else if (sphereY < -halfSize.getY())
            closestY = -halfSize.getY();
        else
            closestY = sphereY;

        if (sphereZ > halfSize.getZ())
            closestZ = halfSize.getZ();
        else if (sphereZ < -halfSize.getZ())
            closestZ = -halfSize.getZ();
        else
            closestZ = sphereZ;

        Point3 closestLocal = new Point3(closestX, closestY, closestZ);
        double depth = radius - Vector3.offset(spherePosLocal, closestLocal).getEuclideanMagnitude();

        if (depth < 0)
            return contacts;

        Point3 contactPoint = box.getTransformation().toGlobal(closestLocal);
        Vector3 normal = Vector3.offset(spherePos, contactPoint);

        contacts.add(new ContactProperties(contactPoint, normal, depth));
        return contacts;
    }

    public static ArrayList<ContactProperties> BoxOnPoint(BoundingBox box, Point3 point) {
        ArrayList<ContactProperties> contacts = new ArrayList<>();

        Point3 pointLocal = box.getTransformation().toLocal(point);
        Vector3 halfSize = box.getHalfSize();

        double xDepth = halfSize.getX() - Math.abs(pointLocal.getX());
        if (xDepth < 0)
            return contacts;

        double yDepth = halfSize.getY() - Math.abs(pointLocal.getY());
        if (yDepth < 0)
            return contacts;

        double zDepth = halfSize.getZ() - Math.abs(pointLocal.getZ());
        if (zDepth < 0)
            return contacts;

        Vector3 normal;
        double depth;
        boolean revert;
        if (xDepth < yDepth && xDepth < zDepth) {
            depth = xDepth;
            normal = new Vector3(box.getAxis(0));
            revert = (pointLocal.getX() < 0);
        } else if (yDepth <= xDepth && yDepth < zDepth) {
            depth = yDepth;
            normal = new Vector3(box.getAxis(1));
            revert = (pointLocal.getY() < 0);
        } else {
            depth = zDepth;
            normal = new Vector3(box.getAxis(2));
            revert = (pointLocal.getZ() < 0);
        }

        if (revert)
            normal.revert();

        contacts.add(new ContactProperties(point, normal, depth));
        return contacts;
    }

    public static ArrayList<ContactProperties> BoxOnBox(BoundingBox box1, BoundingBox box2) {
        ArrayList<ContactProperties> contacts = new ArrayList<>();

        Vector3 offset = Vector3.offset(box1.getPosition(), box2.getPosition());
        Vector3[] box1axes = new Vector3[3], box2axes = new Vector3[3];

        int contactIndex = -1, contactAxis1Index = -1, contactAxis2Index = -1;
        double depth, contactDepth = Double.POSITIVE_INFINITY;
        Vector3 contactAxis = new Vector3();

        for (int i = 0; i < 3; i++) {
            box1axes[i] = new Vector3(box1.getAxis(i));
            depth = boxesDepthOnAxis(box1, box2, box1axes[i], offset);
            if (depth < 0)
                return contacts;

            if (depth < contactDepth) {
                contactDepth = depth;
                contactAxis = new Vector3(box1axes[i]);
                contactIndex = 1;
            }
        }

        for (int i = 0; i < 3; i++) {
            box2axes[i] = new Vector3(box2.getAxis(i));
            depth = boxesDepthOnAxis(box1, box2, box2axes[i], offset);
            if (depth < 0)
                return contacts;

            if (depth < contactDepth) {
                contactDepth = depth;
                contactAxis = new Vector3(box2axes[i]);
                contactIndex = 2;
            }
        }

        int betterBoxContactIndex = contactIndex;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                Vector3 axis = Vector3.cross(box1axes[i], box2axes[j]);
                depth = boxesDepthOnAxis(box1, box2, axis, offset);
                if (depth < 0)
                    return contacts;

                if (depth < contactDepth) {
                    contactDepth = depth;
                    contactAxis = new Vector3(axis);
                    contactAxis1Index = i;
                    contactAxis2Index = j;
                    contactIndex = 0;
                }
            }

        Point3 vertex;
        if (contactIndex == -1)
            return contacts;
        else if (contactIndex == 0) {
            if (contactAxis1Index == -1 || contactAxis2Index == -1)
                return contacts;

            Vector3 axis1 = box1axes[contactAxis1Index];
            Vector3 axis2 = box2axes[contactAxis2Index];

            if (Vector3.dot(contactAxis, offset) > 0)
                contactAxis.revert();

            double[] edgePoint1Array = box1.getHalfSize().toArray();
            double[] edgePoint2Array = box2.getHalfSize().toArray();
            for (int i = 0; i < 3; i++) {
                if (i == contactAxis1Index)
                    edgePoint1Array[i] = 0;
                else if (Vector3.dot(box1axes[i], contactAxis) > 0)
                    edgePoint1Array[i] = -edgePoint1Array[i];

                if (i == contactAxis2Index)
                    edgePoint2Array[i] = 0;
                else if (Vector3.dot(box2axes[i], contactAxis) < 0)
                    edgePoint2Array[i] = -edgePoint2Array[i];
            }

            Point3 edgePoint1 = box1.getTransformation().toGlobal(new Point3(edgePoint1Array));
            Point3 edgePoint2 = box2.getTransformation().toGlobal(new Point3(edgePoint2Array));

            Vector3 toSt = Vector3.offset(edgePoint2, edgePoint1);
            double dpStaOne = Vector3.dot(axis1, toSt);
            double dpStaTwo = Vector3.dot(axis2, toSt);
            double smOne = axis1.getSquareMagnitude();
            double smTwo = axis2.getSquareMagnitude();
            double dpOneTwo = Vector3.dot(axis1, axis2);

            double denom = smOne * smTwo - dpOneTwo * dpOneTwo;
            if (Math.abs(denom) < 0.0001)
                vertex = (betterBoxContactIndex == 1?edgePoint2:edgePoint1);
            else {
                double mua = (dpOneTwo * dpStaTwo - smTwo * dpStaOne) / denom;
                double mub = (smOne * dpStaTwo - dpOneTwo * dpStaOne) / denom;
                double oneAxis = box1.getHalfSize().toArray()[contactAxis1Index];
                double twoAxis = box2.getHalfSize().toArray()[contactAxis2Index];

                if (mua > oneAxis || mua < -oneAxis || mub > twoAxis || mub < -twoAxis)
                    vertex = (betterBoxContactIndex == 1 ? edgePoint2 : edgePoint1);
                else {
                    Vector3 cOne = Vector3.sum(new Vector3(edgePoint1), axis1.scaled(mua));
                    Vector3 cTwo = Vector3.sum(new Vector3(edgePoint2), axis2.scaled(mub));
                    vertex = new Point3(Vector3.sum(cOne, cTwo).scaled(0.5));
                }
            }
        } else {
            if (Vector3.dot(contactAxis, (betterBoxContactIndex == 1?offset:offset.getReverse())) > 0)
                contactAxis.revert();

            BoundingBox other = (betterBoxContactIndex == 1?box2:box1);
            double[] halfSize = other.getHalfSize().toArray();
            for (int i = 0; i < 3; i++)
                if (Vector3.dot(new Vector3(other.getAxis(i)), contactAxis) < 0)
                    halfSize[i] = -halfSize[i];

            if (betterBoxContactIndex == 2)
                contactAxis.revert();

            vertex = other.getTransformation().toGlobal(new Point3(halfSize));
        }

        contacts.add(new ContactProperties(vertex, contactAxis, contactDepth));
        return contacts;
    }

    private static double boxesDepthOnAxis(BoundingBox box1, BoundingBox box2, Vector3 axis, Vector3 offset) {
        if (axis.getSquareMagnitude() < 0.0001)
            return Double.POSITIVE_INFINITY;
        axis.normalize();

        double box1Proj = box1.transformToAxis(axis);
        double box2Proj = box2.transformToAxis(axis);
        double distance = Math.abs(Vector3.dot(offset, axis));
        return box1Proj + box2Proj - distance;
    }

    public static ArrayList<ContactProperties> SphereOnHalfSpace(BoundingSphere sphere, BoundingHalfSpace halfSpace) {
        ArrayList<ContactProperties> contacts = new ArrayList<>();

        double depth = sphere.getRadius() + halfSpace.getOffset() - Vector3.dot(halfSpace.getNormal(), new Vector3(sphere.getPosition()));
        if (depth < 0)
            return contacts;

        Vector3 perpendicular = halfSpace.getNormal().scaled(sphere.getRadius() - depth);
        Point3 contactPoint = sphere.getPosition().offset(perpendicular.getReverse());

        contacts.add(new ContactProperties(contactPoint, halfSpace.getNormal(), depth));
        return contacts;
    }

    public static ArrayList<ContactProperties> BoxOnHalfSpace(BoundingBox box, BoundingHalfSpace halfSpace) {
        ArrayList<ContactProperties> contacts = new ArrayList<>();

        double axis = box.transformToAxis(halfSpace.getNormal());
        double boxDepth = axis + halfSpace.getOffset() - Vector3.dot(halfSpace.getNormal(), new Vector3(box.getPosition()));
        if (boxDepth < 0)
            return contacts;

        for (Point3 vertex : box.getVertices()) {
            double depth = halfSpace.getOffset() - Vector3.dot(new Vector3(vertex), halfSpace.getNormal());
            if (depth <= 0)
                continue;

            Point3 contactPoint = vertex.offset(halfSpace.getNormal().scaled(-depth));
            contacts.add(new ContactProperties(contactPoint, halfSpace.getNormal(), depth));
        }
        return contacts;
    }

    public static ArrayList<Point3> LineOnSphere(Point3 origin, Vector3 line, BoundingSphere sphere) {
        ArrayList<Point3> points = new ArrayList<>();

        Vector3 offset = Vector3.offset(sphere.getPosition(), origin), unitLine = line.getNormalized();
        double dot = Vector3.dot(unitLine, offset), underRoot = dot*dot - (Vector3.dot(offset, offset) - sphere.getRadius()*sphere.getRadius());
        if (underRoot < 0)
            return points;
        else if (underRoot == 0)
            points.add(origin.offset(unitLine.scaled(-dot)));
        else {
            double root = Math.sqrt(underRoot);
            points.add(origin.offset(unitLine.scaled(-dot+root)));
            points.add(origin.offset(unitLine.scaled(-dot-root)));
        }

        return points;
    }

    public static ArrayList<Point3> LineOnBox(Point3 origin, Vector3 line, BoundingBox box) {
        ArrayList<Point3> points = new ArrayList<>();

        Point3 originLocal = box.getTransformation().toLocal(origin), lineEndLocal = box.getTransformation().toLocal(origin.offset(line.getNormalized())), facePoint;
        Vector3 lineLocal = Vector3.offset(originLocal, lineEndLocal), halfSize = box.getHalfSize();

        double scale;
        int[] sign = new int[] {1, -1};

        for (int i = 0; i < 2; i++) {
            scale = lineLocal.getX() / (halfSize.getX()*sign[i] - originLocal.getX());
            if (scale > 0.0001) {
                facePoint = originLocal.offset(lineLocal.scaled(1 / scale));
                if (facePoint.getY() <= halfSize.getY() && facePoint.getY() >= -halfSize.getY() && facePoint.getZ() <= halfSize.getZ() && facePoint.getZ() >= -halfSize.getZ())
                    points.add(box.getTransformation().toGlobal(facePoint));
            }

            scale = lineLocal.getY() / (halfSize.getY()*sign[i] - originLocal.getY());
            if (scale > 0.0001) {
                facePoint = originLocal.offset(lineLocal.scaled(1 / scale));
                if (facePoint.getX() <= halfSize.getX() && facePoint.getX() >= -halfSize.getX() && facePoint.getZ() <= halfSize.getZ() && facePoint.getZ() >= -halfSize.getZ())
                    points.add(box.getTransformation().toGlobal(facePoint));
            }

            scale = lineLocal.getZ() / (halfSize.getZ()*sign[i] - originLocal.getZ());
            if (scale > 0.0001) {
                facePoint = originLocal.offset(lineLocal.scaled(1 / scale));
                if (facePoint.getX() <= halfSize.getX() && facePoint.getX() >= -halfSize.getX() && facePoint.getY() <= halfSize.getY() && facePoint.getY() >= -halfSize.getY())
                    points.add(box.getTransformation().toGlobal(facePoint));
            }
        }

        return points;
    }

    public static BroadPhaseContact BroadPhaseOnBroadPhase(BroadPhase bp1, BroadPhase bp2) {
        double xDepth1 = bp1.getXUpperBound() - bp2.getXLowerBound();
        if (xDepth1 <= 0)
            return null;

        double xDepth2 = bp2.getXUpperBound() - bp1.getXLowerBound();
        if (xDepth2 <= 0)
            return null;

        double yDepth1 = bp1.getYUpperBound() - bp2.getYLowerBound();
        if (yDepth1 <= 0)
            return null;

        double yDepth2 = bp2.getYUpperBound() - bp1.getYLowerBound();
        if (yDepth2 <= 0)
            return null;

        double zDepth1 = bp1.getZUpperBound() - bp2.getZLowerBound();
        if (zDepth1 <= 0)
            return null;

        double zDepth2 = bp2.getZUpperBound() - bp1.getZLowerBound();
        if (zDepth2 <= 0)
            return null;

        return new BroadPhaseContact(bp1, bp2);
    }
}
