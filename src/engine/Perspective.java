package engine;

import com.jogamp.opengl.glu.GLU;
import core.Point3;
import core.Vector3;

public enum Perspective {
    FirstPerson (false) {
        public Perspective next() {
            return Perspective.ThirdPerson;
        }

        public void set(GLU glu, Player player) {
            player.setInvisible();
            Vector3 look = player.getLookingVector();

            Point3 camPoint = player.getCameraPosition();
            Point3 lookPoint = camPoint.offset(look);
            Vector3 up = player.getUpVector();

            lookAt(glu, camPoint, lookPoint, up);
        }
    }, ThirdPerson (true) {
        public Perspective next() {
            return Perspective.FirstPerson;
        }

        public void set(GLU glu, Player player) {
            player.setVisible();
            Vector3 look = player.getLookingVector();
            look.scale(distance);

            Point3 lookPoint = player.getCameraPosition();
            Point3 camPoint = lookPoint.offset(look.getReverse());
            Vector3 up = player.getUpVector();

            lookAt(glu, camPoint, lookPoint, up);
        }
    }, Fixed (true) {
        public Perspective next() {
            return Perspective.FirstPerson;
        }

        public void set(GLU glu, Player player) {
            player.setVisible();
            /*
            double zRad = Math.PI/6;
            lookX = boardSize*panelSize/2;
            lookY = boardSize*panelSize/2;
            lookZ = 0;
            camX = lookX+Math.sin(zRad)*boardSize/2;
            camY = lookY;
            camZ = lookZ+Math.cos(zRad)*boardSize/2;
            uSinZ = 0;
            uCosZ = -1;

            lookAt(glu, cam, look, up);
            */
        }
    };

    public static Perspective spectator = Perspective.FirstPerson;
    protected float distance = 0.6f;
    protected boolean playerObjectVisible;

    Perspective (boolean playerObjectVisible) {
        this.playerObjectVisible = playerObjectVisible;
    }

    public void lookAt(GLU glu, Point3 cam, Point3 look, Vector3 up) {
        glu.gluLookAt(cam.getX(), cam.getY(), cam.getZ(), look.getX(), look.getY(), look.getZ(), up.getX(), up.getY(), up.getZ());
    }

    public abstract Perspective next();
    public abstract void set(GLU glu, Player player);
}