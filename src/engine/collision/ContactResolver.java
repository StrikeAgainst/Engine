package engine.collision;

import core.Matrix3x3;
import core.Vector3;
import engine.RigidObject;
import main.Renderer;

import java.util.ArrayList;

public class ContactResolver {

    public static boolean DISABLE_FRICTION = true;
    public static final boolean DEBUG_INTERPENETRATIONS = false, DEBUG_VELOCITYCHANGE = false;
    public static final double OVERROTATION_LIMIT = 0.2; //todo: adjust
    public static final double FRICTION_MIN_LIMIT = 0.1; //todo: adjust
    public static final double MIN_DEPTH = 0.001f; //todo: adjust
    public static final double MIN_DELTA_VELOCITY = 0.001f; //todo: adjust
    public static int MAX_INTERPENETRATION_ITERATIONS = 100; //todo: adjust
    public static int MAX_VELOCITYCHANGE_ITERATIONS = 20; //todo: adjust

    public static void resolveAll(ArrayList<ObjectContact> contacts, double tick) {
        if (DEBUG_INTERPENETRATIONS || DEBUG_VELOCITYCHANGE)
            System.out.println("contacts total: "+contacts.size());

        for (ObjectContact c : contacts) {
            c.calcInternals(tick);
            if (DEBUG_INTERPENETRATIONS || DEBUG_VELOCITYCHANGE)
                System.out.println(c);
        }

        resolveInterpenetrations(contacts);
        resolveVelocityChange(contacts);
    }

    public static void resolveInterpenetrations(ArrayList<ObjectContact> contacts) {
        if (DEBUG_INTERPENETRATIONS)
            System.out.println("interpenetration, frame " + Renderer.getFrame());

        int iteration = 0;
        while (iteration++ < MAX_INTERPENETRATION_ITERATIONS) {
            ObjectContact contact = null;

            for (ObjectContact c : contacts) {
                if (c.getDepth() <= MIN_DEPTH)
                    continue;

                if (contact == null || contact.getDepth() < c.getDepth())
                    contact = c;
            }

            if (contact == null)
                break;

            Vector3 normal = contact.getNormal();
            double depth = contact.getDepth();

            RigidObject[] objects = contact.getObjects();
            Vector3[] offsets = contact.getOffsets();
            int[] sign = new int[] {1, -1};

            Vector3[] inertiaTorque = new Vector3[2];
            double[] linearInertia = new double[2], angularInertia = new double[2];
            double totalInertia = 0;

            if (DEBUG_INTERPENETRATIONS) {
                System.out.println("iteration " + iteration);
                System.out.println("contact " + contact);
                System.out.println("depth " + depth);
            }

            for (int i = 0; i < objects.length; i++) {
                if (objects[i] == null)
                    continue;

                inertiaTorque[i] = Vector3.cross(offsets[i], normal);
                Vector3 angularInertiaComponent = objects[i].getInertiaTensor().productInverseGlobal(inertiaTorque[i]);
                angularInertiaComponent = Vector3.cross(angularInertiaComponent, offsets[i]);

                linearInertia[i] = objects[i].getInverseMass();
                angularInertia[i] = Vector3.dot(angularInertiaComponent, normal);

                totalInertia += angularInertia[i] + linearInertia[i];
            }

            double move = depth / totalInertia;
            double[] linearMove = new double[2], angularMove = new double[2];
            Vector3[] linearChange = new Vector3[2], angularChange = new Vector3[2];

            for (int i = 0; i < objects.length; i++) {
                if (objects[i] == null)
                    continue;

                linearMove[i] = sign[i] * move * linearInertia[i];
                angularMove[i] = sign[i] * move * angularInertia[i];

                Vector3 angularProjection = Vector3.sum(offsets[i], normal.scaled(-Vector3.dot(offsets[i], normal)));
                double limit = angularProjection.getEuclideanMagnitude() * OVERROTATION_LIMIT;
                if (Math.abs(angularMove[i]) > limit) {
                    double totalMove = linearMove[i] + angularMove[i];
                    if (angularMove[i] >= 0)
                        angularMove[i] = limit;
                    else
                        angularMove[i] = -limit;

                    linearMove[i] = totalMove - angularMove[i];
                }

                linearChange[i] = normal.scaled(linearMove[i]);

                angularChange[i] = new Vector3();
                if (angularMove[i] != 0) {
                    Vector3 impulsePerMove = objects[i].getInertiaTensor().productInverseGlobal(inertiaTorque[i]);
                    angularChange[i] = impulsePerMove.scaled(angularMove[i] / angularInertia[i]);
                }

                if (DEBUG_INTERPENETRATIONS) {
                    System.out.println("object "+i);
                    System.out.println("oldPosition " + objects[i].getPosition());
                    System.out.println("oldOrientation " + objects[i].getOrientation());
                    System.out.println("linearChange " + linearChange[i]);
                    System.out.println("angularChange " + angularChange[i]);
                }

                objects[i].add(linearChange[i], angularChange[i]);

                if (DEBUG_INTERPENETRATIONS) {
                    System.out.println("newPosition " + objects[i].getPosition());
                    System.out.println("newOrientation " + objects[i].getOrientation());
                }
            }

            for (ObjectContact c : contacts)
                c.updateDepth(objects, linearChange, angularChange);

            if (DEBUG_INTERPENETRATIONS) {
                System.out.println("newDepth " + contact.getDepth());
                System.out.println();
            }
        }
    }

    public static void resolveVelocityChange(ArrayList<ObjectContact> contacts) {
        if (DEBUG_VELOCITYCHANGE)
            System.out.println("velocity change, frame " + Renderer.getFrame());

        int iteration = 0;
        while (iteration++ < MAX_VELOCITYCHANGE_ITERATIONS) {
            ObjectContact contact = null;

            for (ObjectContact c : contacts) {
                if (c.getDeltaVelocity() <= MIN_DELTA_VELOCITY)
                    continue;

                if (contact == null || contact.getDeltaVelocity() < c.getDeltaVelocity())
                    contact = c;
            }

            if (contact == null)
                break;

            Vector3 normal = contact.getNormal();
            RigidObject[] objects = contact.getObjects();
            Vector3[] offsets = contact.getOffsets();
            int[] sign = new int[] {1, -1};

            double deltaVelocity = contact.getDeltaVelocity();
            double friction = contact.getFriction();
            Vector3 impulseContact;

            if (DEBUG_VELOCITYCHANGE) {
                System.out.println("iteration " + iteration);
                System.out.println("contact " + contact);
                System.out.println("closingVelocity " + contact.getClosingVelocity());
            }

            if (friction < FRICTION_MIN_LIMIT || DISABLE_FRICTION) { //without friction
                double deltaImpulseVelocity = 0;

                for (int i = 0; i < objects.length; i++) {
                    if (objects[i] == null)
                        continue;

                    Vector3 unitImpulseTorque = Vector3.cross(offsets[i], normal);
                    Vector3 unitImpulseRotation = objects[i].getInertiaTensor().productInverseGlobal(unitImpulseTorque);
                    Vector3 unitImpulseVelocity = Vector3.cross(unitImpulseRotation, offsets[i]);

                    deltaImpulseVelocity += objects[i].getInverseMass() + Vector3.dot(unitImpulseVelocity, normal);
                }

                impulseContact = new Vector3(deltaVelocity / deltaImpulseVelocity, 0, 0);

            } else { //with friction
                double inverseMass = 0;
                Vector3 closingVelocity = contact.getClosingVelocity();
                Matrix3x3 deltaImpulseVelocity = Matrix3x3.getEmpty();

                for (int i = 0; i < objects.length; i++) {
                    if (objects[i] == null)
                        continue;

                    Matrix3x3 impulseToTorque = Matrix3x3.getSkewSymmetric(offsets[i]);

                    Matrix3x3 unitImpulseTorque = new Matrix3x3(impulseToTorque);
                    Matrix3x3 unitImpulseRotation = objects[i].getInertiaTensor().productInverseGlobal(unitImpulseTorque); //fehler!
                    Matrix3x3 unitImpulseVelocity = impulseToTorque.product(unitImpulseRotation);
                    unitImpulseVelocity.scale(-1);

                    deltaImpulseVelocity.add(unitImpulseVelocity);

                    inverseMass += objects[i].getInverseMass();
                }

                Matrix3x3 deltaImpulseVelocityContact = new Matrix3x3(contact.getToLocal());
                deltaImpulseVelocityContact.multiply(deltaImpulseVelocity);
                deltaImpulseVelocityContact.multiply(contact.getToGlobal());
                if (DEBUG_VELOCITYCHANGE)
                    System.out.println("deltaImpulseVelocityContact "+deltaImpulseVelocityContact);

                Matrix3x3 inverseMassIdentity = Matrix3x3.getIdentity();
                inverseMassIdentity.scale(inverseMass);
                deltaImpulseVelocityContact.add(inverseMassIdentity);

                if (DEBUG_VELOCITYCHANGE) {
                    System.out.println("inverseMassIdentity " + inverseMassIdentity);
                    System.out.println("deltaImpulseVelocityContact " + deltaImpulseVelocityContact);
                }

                Matrix3x3 impulseMatrix = deltaImpulseVelocityContact.getInverse();
                Vector3 velocityKill = new Vector3(deltaVelocity, -closingVelocity.getY(), -closingVelocity.getZ());
                impulseContact = impulseMatrix.product(velocityKill);

                if (DEBUG_VELOCITYCHANGE)
                    System.out.println("impulseMatrix "+impulseMatrix);

                double impulseContactX = impulseContact.getX(), impulseContactY = impulseContact.getY(), impulseContactZ = impulseContact.getZ();
                double planarImpulse = Math.sqrt(impulseContactY*impulseContactY + impulseContactZ*impulseContactZ);

                if (DEBUG_VELOCITYCHANGE) {
                    System.out.println("impulseContact static "+impulseContact);
                    System.out.println("impulse static "+contact.toGlobal(impulseContact));
                    System.out.println("planarImpulse "+planarImpulse);
                    System.out.println("impulseContactXfriction "+(impulseContactX * friction));
                }

                if (planarImpulse > impulseContactX * friction) {
                    double frictionPerPlanarImpulse = friction/planarImpulse;
                    impulseContactY *= frictionPerPlanarImpulse;
                    impulseContactZ *= frictionPerPlanarImpulse;

                    double[][] data = deltaImpulseVelocityContact.getData();
                    impulseContactX = data[0][0] + data[1][0]*impulseContactY + data[2][0]*impulseContactZ;
                    impulseContactX = deltaVelocity/impulseContactX;

                    impulseContact.set(impulseContactX, impulseContactY*impulseContactX, impulseContactZ*impulseContactX);
                }
            }

            Vector3 impulse = contact.toGlobal(impulseContact);
            Vector3[] linearChange = new Vector3[2], angularChange = new Vector3[2];

            for (int i = 0; i < objects.length; i++) {
                if (objects[i] == null)
                    continue;

                Vector3 impulsiveTorque = Vector3.cross(offsets[i], impulse).scaled(sign[i]);

                linearChange[i] = impulse.scaled(objects[i].getInverseMass()*sign[i]);
                angularChange[i] = objects[i].getInertiaTensor().productInverseGlobal(impulsiveTorque);

                if (DEBUG_VELOCITYCHANGE) {
                    System.out.println("object " + i);
                    System.out.println("offset " + offsets[i].toString());
                    System.out.println("impulse " + impulse.toString());
                    System.out.println("impulsiveTorque " + impulsiveTorque.toString());
                    System.out.println("oldVelocity " + objects[i].getVelocity());
                    System.out.println("oldRotation " + objects[i].getRotation());
                    System.out.println("linearChange " + linearChange[i]);
                    System.out.println("angularChange " + angularChange[i]);
                }

                objects[i].addVelocity(linearChange[i]);
                objects[i].addRotation(angularChange[i]);
                if (DEBUG_VELOCITYCHANGE) {
                    System.out.println("newVelocity " + objects[i].getVelocity());
                    System.out.println("newRotation " + objects[i].getRotation());
                }
            }

            for (ObjectContact c : contacts)
                c.updateClosingVelocity(objects, linearChange, angularChange);

            if (DEBUG_VELOCITYCHANGE) {
                System.out.println("newDeltaVelocity " + contact.getDeltaVelocity());
                System.out.println();
            }
        }
    }

    public static void setMaxIterations(int iterations) {
        MAX_INTERPENETRATION_ITERATIONS = iterations;
        MAX_VELOCITYCHANGE_ITERATIONS = iterations;
    }
}
