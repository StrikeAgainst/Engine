package main;

import core.AxisAngle;
import core.RGB;
import core.Vector3;
import engine.*;
import engine.force.ForceRegistry;
import engine.force.Gravity;
import object.*;
import core.Point3;

import java.util.Random;

public enum Scenario {
    Maze {
        public void build() {
            Random random = new Random();

            int boardSize = 12;
            float panelSize = 0.5f;
            float x, y, half = panelSize/2, wallHeight = 0.8f;
            main.Maze m = main.Maze.createRandomMaze(boardSize);

            for (int i = 0; i < boardSize; i++) {
                x = i*panelSize;
                for (int j = 0; j < boardSize; j++) {
                    y = j*panelSize;

                    if (i == 0 && j == 0)
                        ObjectFactory.createTile(new Point3(x+half, y+half, 0.0f), panelSize, new RGB(0, 0, 0.5f));
                    else if (i == boardSize-1 && j == boardSize-1)
                        ObjectFactory.createTile(new Point3(x+half, y+half, 0.0f), panelSize, new RGB(0, 0.5f, 0));
                    else if (random.nextInt(boardSize) != 0)
                        ObjectFactory.createTile(new Point3(x+half, y+half, 0.0f), panelSize);

                    if (m.getNorth()[j+1][i+1] && (i == 0 || !m.getSouth()[j+1][i]))
                        ObjectFactory.createWall(new Point3(x, y+half, wallHeight/2), panelSize, wallHeight, false);
                    if (m.getSouth()[j+1][i+1])
                        ObjectFactory.createWall(new Point3(x+panelSize, y+half, wallHeight/2), panelSize, wallHeight, false);
                    if (m.getWest()[j+1][i+1] && (j == 0 || !m.getEast()[j][i+1]))
                        ObjectFactory.createWall(new Point3(x+half, y, wallHeight/2), panelSize, wallHeight, true);
                    if (m.getEast()[j+1][i+1])
                        ObjectFactory.createWall(new Point3(x+half, y+panelSize, wallHeight/2), panelSize, wallHeight, true);
                }
            }
        }

        protected PlayableObject getPlayerObject() {
            return ObjectFactory.createPawn(new Point3(Config.INIT_PLAYER_POS),0.4f,0.1f);
        }

        protected Perspective getPerspective() {
            return Perspective.FirstPerson;
        }
    }, Corner {
        public void build() {
            ObjectFactory.createTile(new Point3(0.25f, 0.25f, 0.0f), 0.5f, new RGB(0, 0, 0.5f));
            ObjectFactory.createWall(new Point3(0, 0.25f, 0.4f), 0.5f, 0.8f, false);
            ObjectFactory.createWall(new Point3(0.25f, 0, 0.4f), 0.5f, 0.8f, true);
        }

        protected PlayableObject getPlayerObject() {
            return ObjectFactory.createPawn(new Point3(Config.INIT_PLAYER_POS),0.4f,0.1f);
        }

        protected Perspective getPerspective() {
            return Perspective.ThirdPerson;
        }
    }, BoxContact {
        public void build() {
            ObjectFactory.createBox(new Point3(2f, 0, 0), new Vector3(1, 1, 1), 2)
                .setOrientation((new AxisAngle(new Vector3(1, 0, 0), Physics.R180/12)).toQuaternion());

            ObjectFactory.createBox(new Point3(2f, -1f, 0.9f), new Vector3(1, 1, 1), 2)
                .setOrientation((new AxisAngle(new Vector3(1, 1, 1), Physics.R180)).toQuaternion());
        }

        protected PlayableObject getPlayerObject() {
            return ObjectFactory.createPawn(new Point3(Config.INIT_PLAYER_POS),0.4f,0.1f);
        }

        protected Perspective getPerspective() {
            return Perspective.ThirdPerson;
        }
    }, Cyclone {
        public void build() {
            Ball ball = ObjectFactory.createBall(new Point3(2, 0, 1), 0.5f, 1f);

            Gravity gravity = new Gravity(PhysicalObject.getGravity());
            ForceRegistry.get().add(ball, gravity);

            //this.world = new World(objects, 20, 5);
        }

        protected PlayableObject getPlayerObject() {
            return null;
        }

        protected Perspective getPerspective() {
            return Perspective.FirstPerson;
        }
    };

    protected abstract void build();
    protected abstract PlayableObject getPlayerObject();
    protected abstract Perspective getPerspective();

    protected PlayableObject playerObject = null;

    Scenario () {}

    public void playerAttachObject(Player player) {
        if (this.playerObject == null)
            this.playerObject = getPlayerObject();

        player.attachPlayerObject(this.playerObject);
    }
}