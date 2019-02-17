package engine.collision;

import core.*;
import engine.collision.bounding.BoundingSphere;
import engine.collision.bounding.BoundingBox;
import engine.collision.bounding.BroadPhase;
import engine.collision.bounding.PrimitiveBounding;

public class ContactDetector {

    public static PrimitiveContact BoundingOnBounding(PrimitiveBounding bounding1, PrimitiveBounding bounding2) {
        if (bounding1 instanceof BoundingSphere) {
            if (bounding2 instanceof BoundingSphere)
                return SphereOnSphere((BoundingSphere) bounding1, (BoundingSphere) bounding2);
            else if (bounding2 instanceof BoundingBox)
                return SphereOnBox((BoundingSphere) bounding1, (BoundingBox) bounding2);
        } else if (bounding1 instanceof BoundingBox) {
            if (bounding2 instanceof BoundingSphere)
                return SphereOnBox((BoundingSphere) bounding2, (BoundingBox) bounding1);
            else if (bounding2 instanceof BoundingBox)
                return BoxOnBox((BoundingBox) bounding1, (BoundingBox) bounding2);
        }

        return null;
    }

    public static PrimitiveContact SphereOnSphere(BoundingSphere sphere1, BoundingSphere sphere2) {
        Point3 pos1 = sphere1.getPosition(), pos2 = sphere2.getPosition();
        Vector3 offset = Vector3.offset(pos2, pos1);
        float size = offset.getEuclideanMagnitude();
        float radiusSum = sphere1.getRadius()+sphere2.getRadius();

        if (size < 0 || size > radiusSum)
            return null;

        Point3 contactPoint = pos1.offset(offset.scaled(0.5f));
        Vector3 normal = offset.scaled(1 / size);
        float depth = radiusSum-size;

        return new PrimitiveContact(sphere1, sphere2, contactPoint, normal, depth);
    }

    public static PrimitiveContact SphereOnBox(BoundingSphere sphere, BoundingBox box) {
        Point3 spherePos = sphere.getPosition(), spherePosLocal = box.getTransformation().toLocal(spherePos);
        Vector3 halfSize = box.getHalfSize();
        float radius = sphere.getRadius(), sphereX = spherePosLocal.getX(), sphereY = spherePosLocal.getY(), sphereZ = spherePosLocal.getZ();

        if (Math.abs(spherePosLocal.getX()) - radius > halfSize.getY() || Math.abs(spherePosLocal.getY()) - radius > halfSize.getX() || Math.abs(spherePosLocal.getZ()) - radius > halfSize.getZ())
            return null;

        float closestX, closestY, closestZ;

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
        float distance = (Vector3.offset(spherePosLocal, closestLocal)).getEuclideanMagnitude();
        float depth = radius-distance;

        if (depth < 0)
            return null;

        Point3 contactPoint = box.getTransformation().toGlobal(closestLocal);
        Vector3 normal = Vector3.offset(contactPoint, spherePos);

        return new PrimitiveContact(sphere, box, contactPoint, normal, depth);
    }

    public static PrimitiveContact BoxOnPoint(BoundingBox box, Point3 point) {
        Point3 pointLocal = box.getTransformation().toLocal(point);
        Vector3 halfSize = box.getHalfSize();

        float xDepth = halfSize.getX() - Math.abs(pointLocal.getX());
        if (xDepth < 0)
            return null;

        float yDepth = halfSize.getY() - Math.abs(pointLocal.getY());
        if (yDepth < 0)
            return null;

        float zDepth = halfSize.getZ() - Math.abs(pointLocal.getZ());
        if (zDepth < 0)
            return null;

        Vector3 normal;
        float depth;
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

        return new PrimitiveContact(box, null, point, normal, depth);
    }

    public static PrimitiveContact BoxOnBox(BoundingBox box1, BoundingBox box2) {
        Vector3 offset = Vector3.offset(box1.getPosition(), box2.getPosition());
        Vector3[] box1axes = new Vector3[3], box2axes = new Vector3[3];

        int contactIndex = -1, contactAxis1Index = -1, contactAxis2Index = -1;
        float depth, contactDepth = Float.POSITIVE_INFINITY;
        Vector3 contactAxis = new Vector3();

        for (int i = 0; i < 3; i++) {
            box1axes[i] = new Vector3(box1.getAxis(i));
            depth = boxesDepthOnAxis(box1, box2, box1axes[i], offset);
            if (depth < 0)
                return null;

            if (depth < contactDepth) {
                contactDepth = depth;
                contactAxis = box1axes[i];
                contactIndex = 1;
            }
        }

        for (int i = 0; i < 3; i++) {
            box2axes[i] = new Vector3(box2.getAxis(i));
            depth = boxesDepthOnAxis(box1, box2, box2axes[i], offset);
            if (depth < 0)
                return null;

            if (depth < contactDepth) {
                contactDepth = depth;
                contactAxis = box2axes[i];
                contactIndex = 2;
            }
        }

        float betterBoxContactIndex = contactIndex;

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                Vector3 axis = Vector3.cross(box1axes[i], box2axes[j]);
                depth = boxesDepthOnAxis(box1, box2, axis, offset);
                if (depth < 0)
                    return null;

                if (depth < contactDepth) {
                    contactDepth = depth;
                    contactAxis = axis;
                    contactAxis1Index = i;
                    contactAxis2Index = j;
                    contactIndex = 0;
                }
            }

        Point3 vertex;
        if (contactIndex == -1)
            return null;
        else if (contactIndex == 0) {
            //Engine.queueText("EdgeEdge");
            if (contactAxis1Index == -1 || contactAxis2Index == -1)
                return null;

            Vector3 axis1 = box1axes[contactAxis1Index];
            Vector3 axis2 = box2axes[contactAxis2Index];

            if (Vector3.dot(contactAxis, offset) > 0)
                contactAxis.revert();
            contactAxis.normalize();

            float[] edgePoint1Array = box1.getHalfSize().getArray();
            float[] edgePoint2Array = box2.getHalfSize().getArray();
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
            float dpStaOne = Vector3.dot(axis1, toSt);
            float dpStaTwo = Vector3.dot(axis2, toSt);
            float smOne = axis1.getSquareMagnitude();
            float smTwo = axis2.getSquareMagnitude();
            float dpOneTwo = Vector3.dot(axis1, axis2);

            float denom = smOne * smTwo - dpOneTwo * dpOneTwo;
            if (Math.abs(denom) < 0.0001f)
                vertex = (betterBoxContactIndex == 1?edgePoint1:edgePoint2);
            else {
                float mua = (dpOneTwo * dpStaTwo - smTwo * dpStaOne) / denom;
                float mub = (smOne * dpStaTwo - dpOneTwo * dpStaOne) / denom;

                if (mua > edgePoint1Array[contactAxis1Index] || mua < -edgePoint1Array[contactAxis1Index] || mub > edgePoint2Array[contactAxis2Index] || mub < -edgePoint2Array[contactAxis2Index])
                    vertex = (betterBoxContactIndex == 1?edgePoint1:edgePoint2);
                else {
                    Vector3 cOne = Vector3.sum(new Vector3(edgePoint1), axis1.scaled(mua));
                    Vector3 cTwo = Vector3.sum(new Vector3(edgePoint2), axis2.scaled(mub));
                    vertex = new Point3(Vector3.sum(cOne, cTwo).scaled(0.5f));
                }
            }
        } else {
            //Engine.queueText("PointFace");
            if (Vector3.dot(contactAxis, (betterBoxContactIndex == 1?offset:offset.getReverse())) > 0)
                contactAxis.revert();

            BoundingBox other = (betterBoxContactIndex == 1?box2:box1);
            float[] halfSize = other.getHalfSize().getArray();
            for (int i = 0; i < 3; i++)
                if (Vector3.dot(new Vector3(other.getAxis(i)), contactAxis) < 0)
                    halfSize[i] = -halfSize[i];

            vertex = other.getTransformation().toGlobal(new Point3(halfSize));
        }

        return new PrimitiveContact(box1, box2, vertex, contactAxis, contactDepth);
    }

    private static float boxesDepthOnAxis(BoundingBox box1, BoundingBox box2, Vector3 axis, Vector3 offset) {
        if (axis.getSquareMagnitude() < 0.0001)
            return Float.POSITIVE_INFINITY;
        axis.normalize();

        float box1Proj = box1.transformToAxis(axis);
        float box2Proj = box2.transformToAxis(axis);
        float distance = Math.abs(Vector3.dot(offset, axis));
        return box1Proj + box2Proj - distance;
    }

    public static BroadPhaseContact BroadPhaseOnBroadPhase(BroadPhase bp1, BroadPhase bp2) {
        float xDepth1 = bp1.getXUpperBound() - bp2.getXLowerBound();
        if (xDepth1 <= 0)
            return null;

        float xDepth2 = bp2.getXUpperBound() - bp1.getXLowerBound();
        if (xDepth2 <= 0)
            return null;

        float yDepth1 = bp1.getYUpperBound() - bp2.getYLowerBound();
        if (yDepth1 <= 0)
            return null;

        float yDepth2 = bp2.getYUpperBound() - bp1.getYLowerBound();
        if (yDepth2 <= 0)
            return null;

        float zDepth1 = bp1.getZUpperBound() - bp2.getZLowerBound();
        if (zDepth1 <= 0)
            return null;

        float zDepth2 = bp2.getZUpperBound() - bp1.getZLowerBound();
        if (zDepth2 <= 0)
            return null;

        return new BroadPhaseContact(bp1, bp2);
    }
}
