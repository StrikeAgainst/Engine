package world;

import com.jogamp.opengl.glu.GLU;
import engine.Player;

public class Camera {

    private static Camera camera = null;
    private Perspective perspective;

    private Camera() {}

    public static Camera get() {
        if (camera == null)
            camera = new Camera();
        return camera;
    }

    public void setCamera(GLU glu, Player player) {
        perspective.setCamera(glu, player);
    }

    public void nextPerspective() {
        perspective = perspective.next();
    }

    public Perspective getPerspective() {
        return perspective;
    }

    public void setPerspective(Perspective perspective) {
        this.perspective = perspective;
    }

}
