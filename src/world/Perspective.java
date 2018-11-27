package world;

import com.jogamp.opengl.glu.GLU;
import engine.Player;

public enum Perspective {
    FirstPerson {
        public Perspective next() {
            return Perspective.ThirdPerson;
        }

        public void setCamera(GLU glu, Player player) {
            player.setInvisible();
            getAngleProperties(player);

            camX = player.getCameraX();
            camY = player.getCameraY();
            camZ = player.getCameraZ();
            lookX = camX+sinZ*cosY;
            lookY = camY+sinZ*sinY;
            lookZ = camZ-cosZ;
            zAngle = player.getZAngle();
            uSinZ = Math.sin(Math.toRadians(zAngle+90));
            uCosZ = Math.cos(Math.toRadians(zAngle+90));

            glu.gluLookAt(camX, camY, camZ, lookX, lookY, lookZ, uSinZ*cosY, uSinZ*sinY, -uCosZ);
        }
    }, ThirdPerson {
        public Perspective next() {
            return Perspective.FirstPerson;
        }

        public void setCamera(GLU glu, Player player) {
            player.setVisible();
            getAngleProperties(player);

            lookX = player.getCameraX();
            lookY = player.getCameraY();
            lookZ = player.getCameraZ();
            camX = lookX-sinZ*cosY*distance;
            camY = lookY-sinZ*sinY*distance;
            camZ = (player.isCameraZLocked()?Math.max(0.1f, lookZ+cosZ*distance):lookZ+cosZ*distance);
            zAngle = player.getZAngle();
            uSinZ = Math.sin(Math.toRadians(zAngle+90));
            uCosZ = Math.cos(Math.toRadians(zAngle+90));

            glu.gluLookAt(camX, camY, camZ, lookX, lookY, lookZ, uSinZ*cosY, uSinZ*sinY, -uCosZ);
        }
    }, Bird {
        public Perspective next() {
            return Perspective.FirstPerson;
        }

        public void setCamera(GLU glu, Player player) {
            player.setVisible();
            getAngleProperties(player);
            /*
            lookX = boardSize*panelSize/2;
            lookY = boardSize*panelSize/2;
            lookZ = 0;
            camX = lookX+Math.sin(zRad)*boardSize/2;
            camY = lookY;
            camZ = lookZ+Math.cos(zRad)*boardSize/2;
            uSinZ = 0;
            uCosZ = -1;
            */
            glu.gluLookAt(camX, camY, camZ, lookX, lookY, lookZ, uSinZ*cosY, uSinZ*sinY, -uCosZ);
        }
    };

    public static Perspective spectator = Perspective.FirstPerson;
    protected double camX, camY, camZ, lookX, lookY, lookZ, distance = 0.6f;
    protected double zRad = Math.toRadians(15), sinY, sinZ, cosY, cosZ, uSinZ, uCosZ;
    protected float zAngle;

    protected void getAngleProperties(Player player) {
        double[] angleProperties = player.getAngleProperties();
        sinY = angleProperties[0];
        sinZ = angleProperties[1];
        cosY = angleProperties[2];
        cosZ = angleProperties[3];
    }

    public abstract Perspective next();
    public abstract void setCamera(GLU glu, Player player);
}