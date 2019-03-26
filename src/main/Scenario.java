package main;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;
import core.Vector3;
import engine.*;
import engine.force.Drag;
import engine.force.ForceRegistry;
import engine.force.Gravity;
import object.*;
import core.Point3;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public enum Scenario implements KeyListener {
    Maze {
        private int boardSize = 8;
        private double panelSize = 1, wallHeight = 0.8, wallStrength = 0.1, tiltFactor = 0.5, tiltGravity;
        private Ball ball;
        private Gravity gravity = new Gravity();
        private Drag drag = new Drag();

        public void init() {
            ground = new Ground(new Vector3(0, 0, 1), 0);
            ground.setMaterial(Material.Metal);

            ball = ObjectFactory.createBall(new Point3(1.5, 1.5, 2), 0.4, 3, Material.Metal);
            ForceRegistry.get().add(ball, gravity);
            ForceRegistry.get().add(ball, drag);
            tiltGravity = tiltFactor * gravity.getGravity().getEuclideanMagnitude();

            double x, y, halfPanelSize = panelSize/2, halfWallHeight = wallHeight/2;
            main.Maze m = main.Maze.createRandomMaze(boardSize);
            boolean[][] south = m.getSouth(), east = m.getEast();

            for (int i = 0; i <= boardSize; i++) {
                x = i*panelSize;
                for (int j = 0; j <= boardSize; j++) {
                    y = j*panelSize;

                    if (south[j][i] && j > 0 && (i < boardSize || j < boardSize))
                        ObjectFactory.createPlatform(new Point3(x + panelSize, y + halfPanelSize, halfWallHeight), new Vector3(wallStrength, panelSize, wallHeight), Material.Metal);
                    if (east[j][i] && i > 0)
                        ObjectFactory.createPlatform(new Point3(x + halfPanelSize, y + panelSize, halfWallHeight), new Vector3(panelSize, wallStrength, wallHeight), Material.Metal);
                }
            }
        }
        public Player initPlayer() {
            double size = boardSize*panelSize, center = panelSize+size/2;
            Player player = new Player(perspective);
            player.setPosition(new Point3(center, center, size));
            player.setCameraYaw(Math.PI);
            player.setCameraPitch(Math.PI/2);
            return player;
        }

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP: {
                    gravity.getGravity().add(new Vector3(-tiltGravity, 0, 0));
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    gravity.getGravity().add(new Vector3(tiltGravity, 0, 0));
                    break;
                }
                case KeyEvent.VK_LEFT: {
                    gravity.getGravity().add(new Vector3(0, -tiltGravity, 0));
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    gravity.getGravity().add(new Vector3(0, tiltGravity, 0));
                    break;
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP: {
                    gravity.getGravity().setX(0);
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    gravity.getGravity().setX(0);
                    break;
                }
                case KeyEvent.VK_LEFT: {
                    gravity.getGravity().setY(0);
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    gravity.getGravity().setY(0);
                    break;
                }
            }
        }

        public void keyTyped(KeyEvent e) {}

        public String description() {
            return "Use the arrow keys to tilt gravity, and try to steer the ball out of the maze.";
        }
    }, Impact{
        private Ball comet;

        public void init() {
            ObjectFactory.createBox(new Point3(6, 1.8, 1.8), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, 0.6, 1.8), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, -0.6, 1.8), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, -1.8, 1.8), new Vector3(1, 1, 1), 5, Material.Metal);

            ObjectFactory.createBox(new Point3(6, 1.8, 0.6), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, 0.6, 0.6), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, -0.6, 0.6), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, -1.8, 0.6), new Vector3(1, 1, 1), 5, Material.Metal);

            ObjectFactory.createBox(new Point3(6, 1.8, -0.6), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, 0.6, -0.6), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, -0.6, -0.6), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, -1.8, -0.6), new Vector3(1, 1, 1), 5, Material.Metal);

            ObjectFactory.createBox(new Point3(6, 1.8, -1.8), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, 0.6, -1.8), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, -0.6, -1.8), new Vector3(1, 1, 1), 5, Material.Metal);
            ObjectFactory.createBox(new Point3(6, -1.8, -1.8), new Vector3(1, 1, 1), 5, Material.Metal);

            comet = ObjectFactory.createBall(new Point3(0, 0, 0), 1.5, 8, Material.Metal);
            comet.addVelocity(new Vector3(2, 0, 0));
        }

        public Player initPlayer() {
            Player player = new Player(perspective);
            player.setPosition(new Point3(-1, 4, 0.5));
            player.setCameraYaw(-Physics.R45);
            return player;
        }

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP: {
                    comet.applyForce(new Vector3(0, 0, 10));
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    comet.applyForce(new Vector3(0, 0, -10));
                    break;
                }
                case KeyEvent.VK_LEFT: {
                    comet.applyForce(new Vector3(0, 10, 0));
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    comet.applyForce(new Vector3(0, -10, 0));
                    break;
                }
            }
        }

        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}

        public String description() {
            return "Use the arrow keys to steer the ball slightly on its collision course with the boxes.";
        }
    }, BallPool {
        private double length = 3, width = 2, height = 1, thickness = 0.1, radius = 0.2;
        private double halfLength = length/2, halfWidth = width/2, halfHeight = height/2;
        private double maxInterval = 2, currentInterval = 0;
        private Gravity gravity = new Gravity();
        private Random random = new Random();

        public void init() {
            ground = new Ground(new Vector3(0, 0, 1), 0);
            ground.setMaterial(Material.Metal);

            ObjectFactory.createPlatform(new Point3(halfLength+thickness, 0, halfHeight), new Vector3(thickness, width+thickness, height), Material.Metal);
            ObjectFactory.createPlatform(new Point3(-halfLength-thickness, 0, halfHeight), new Vector3(thickness, width+thickness, height), Material.Metal);
            ObjectFactory.createPlatform(new Point3(0, halfWidth+thickness, halfHeight), new Vector3(length+thickness, thickness, height), Material.Metal);
            ObjectFactory.createPlatform(new Point3(0, -halfWidth-thickness, halfHeight), new Vector3(length+thickness, thickness, height), Material.Metal);
        }

        public Player initPlayer() {
            Player player = new Player(perspective);
            player.setPosition(new Point3(-halfLength, 0, height+1));
            player.setCameraPitch(Physics.R180/3);
            return player;
        }

        public void update(double tick) {
            currentInterval += tick;
            if (currentInterval >= maxInterval) {
                currentInterval = 0;

                double x = halfLength - random.nextDouble()*halfLength*2;
                double y = halfWidth - random.nextDouble()*halfWidth*2;

                Ball ball = ObjectFactory.createBall(new Point3(x, y, height+1), radius, 1, Material.Metal);
                ForceRegistry.get().add(ball, gravity);
            }
        }

        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}

        public String description() {
            return "A pool getting filled with balls.";
        }
    }, BoxTower {
        private Gravity gravity = new Gravity();

        public void init() {
            ground = new Ground(new Vector3(0, 0, 1), 0);
            ground.setMaterial(Material.Metal);

            Box box1 = ObjectFactory.createBox(new Point3(5, 0.4, 0.6), new Vector3(1, 1, 1), 5, Material.Metal);
            ForceRegistry.get().add(box1, gravity);

            Box box2 = ObjectFactory.createBox(new Point3(5, -0.4, 1.7), new Vector3(1, 1, 1), 5, Material.Metal);
            ForceRegistry.get().add(box2, gravity);

            Box box3 = ObjectFactory.createBox(new Point3(5, 0.4, 2.8), new Vector3(1, 1, 1), 5, Material.Metal);
            ForceRegistry.get().add(box3, gravity);

            Box box4 = ObjectFactory.createBox(new Point3(5, -0.4, 3.9), new Vector3(1, 1, 1), 5, Material.Metal);
            ForceRegistry.get().add(box4, gravity);

            Box box5 = ObjectFactory.createBox(new Point3(5, 0.4, 5), new Vector3(1, 1, 1), 5, Material.Metal);
            ForceRegistry.get().add(box5, gravity);

            Box box6 = ObjectFactory.createBox(new Point3(5, -0.4, 6.1), new Vector3(1, 1, 1), 5, Material.Metal);
            ForceRegistry.get().add(box6, gravity);
        }

        public Player initPlayer() {
            Player player = new Player(perspective);
            player.setCameraPitch(-Physics.R90/9);
            return player;
        }

        public void keyPressed(KeyEvent e) {}
        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}

        public String description() {
            return "A tower of boxes crashing down.";
        }
    }, DEBUG {
        private Box box1, box2;
        private Ball ball1, ball2;
        private PhysicalObject monitored;
        protected Gravity gravity = new Gravity();

        public void init() {
            ground = new Ground(new Vector3(0, 0, 1), 0);
            ground.setMaterial(Material.Metal);

            box1 = ObjectFactory.createBox(new Point3(3, 0.8, 3.0), new Vector3(1, 1, 1), 5, Material.Metal);
            ForceRegistry.get().add(box1, gravity);

            box2 = ObjectFactory.createBox(new Point3(3, 0, 5.0), new Vector3(1, 1, 1), 5, Material.Metal);
            ForceRegistry.get().add(box2, gravity);

            monitored = box1;
        }

        public ArrayList<String> queueText() {
            ArrayList<String> strings = new ArrayList<>();

            strings.add(monitored.toString());
            strings.add("Rotation: "+monitored.getRotation().toString());

            //strings.addAll(Arrays.asList(ContactContainer.get().toStringArray()));
            return strings;
        }

        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP: {
                    monitored.applyForce(new Vector3(0, 0, 200));
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    monitored.applyForce(new Vector3(0, 0, -200));
                    break;
                }
                case KeyEvent.VK_LEFT: {
                    monitored.addRotation(new Vector3(0, 0, 1));
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    monitored.addRotation(new Vector3(0, 0, -1));
                    break;
                }
                case KeyEvent.VK_NUMPAD5: {
                    monitored.stop();
                    break;
                }
                case KeyEvent.VK_NUMPAD2: {
                    monitored.addRotation(new Vector3(0, -1, 0));
                    break;
                }
                case KeyEvent.VK_NUMPAD4: {
                    monitored.addRotation(new Vector3(-1, 0, 0));
                    break;
                }
                case KeyEvent.VK_NUMPAD6: {
                    monitored.addRotation(new Vector3(1, 0, 0));
                    break;
                }
                case KeyEvent.VK_NUMPAD8: {
                    monitored.addRotation(new Vector3(0, 1, 0));
                    break;
                }
                case KeyEvent.VK_U: {
                    monitored.applyForce(new Vector3(0, 0, 100), new Point3(3, 0, 0));
                    break;
                }
                case KeyEvent.VK_H: {
                    monitored.applyForce(new Vector3(0, 0, 100), new Point3(4, -1, 0));
                    break;
                }
                case KeyEvent.VK_J: {
                    monitored.applyForce(new Vector3(0, 0, 100), new Point3(3, -2, 0));
                    break;
                }
                case KeyEvent.VK_K: {
                    monitored.applyForce(new Vector3(0, 0, 100), new Point3(2, -1, 0));
                    break;
                }
            }
        }

        public void keyReleased(KeyEvent e) {}
        public void keyTyped(KeyEvent e) {}

        public String description() {
            return "This is a debug scenario.";
        }
    };

    public abstract void init();

    protected Ground ground = null;
    protected Perspective perspective;

    Scenario () {}

    public Player initPlayer() {
        return new Player(perspective);
    }

    public Ground getGround() {
        return ground;
    }

    public String description() {
        return "";
    }

    public ArrayList<String> queueText() {
        return new ArrayList<>();
    }

    public void render(GL2 gl, GLUT glut) {}

    public void update(double tick) {}
}