package engine;

import engine.particle.*;
import object.Pawn;
import object.Tile;
import object.Wall;
import world.Perspective;
import world.Point3D;

import java.util.ArrayList;
import java.util.Random;

public enum Scenario {
    Maze (Perspective.FirstPerson, false) {
        public void init() {
            Random random = new Random();
            float x, y, half = this.panelSize/2, wallHeight = 0.8f;
            engine.Maze m = engine.Maze.createRandomMaze(boardSize);
            for (int i = 0; i < boardSize; i++) {
                x = i*panelSize;
                for (int j = 0; j < boardSize; j++) {
                    y = j*panelSize;

                    if (i == 0 && j == 0)
                        new Tile(new Point3D(x+half, y+half, 0.0f), panelSize, 0, 0, 0.5f);
                    else if (i == boardSize-1 && j == boardSize-1)
                        new Tile(new Point3D(x+half, y+half, 0.0f), panelSize, 0, 0.5f, 0);
                    else if (random.nextInt(boardSize) != 0)
                        new Tile(new Point3D(x+half, y+half, 0.0f), panelSize);

                    if (m.getNorth()[j+1][i+1] && (i == 0 || !m.getSouth()[j+1][i]))
                        new Wall(new Point3D(x, y+half, wallHeight/2), panelSize, wallHeight, false);
                    if (m.getSouth()[j+1][i+1])
                        new Wall(new Point3D(x+panelSize, y+half, wallHeight/2), panelSize, wallHeight, false);
                    if (m.getWest()[j+1][i+1] && (j == 0 || !m.getEast()[j][i+1]))
                        new Wall(new Point3D(x+half, y, wallHeight/2), panelSize, wallHeight, true);
                    if (m.getEast()[j+1][i+1])
                        new Wall(new Point3D(x+half, y+panelSize, wallHeight/2), panelSize, wallHeight, true);
                }
            }
        }

        public Player initPlayer() {
            return new Player(new Pawn(new Point3D(Config.INIT_PLAYER_POS),0.4f,0.1f));
        }
    }, Corner (Perspective.ThirdPerson, false) {
        public void init() {
            new Tile(new Point3D(0.25f, 0.25f, 0.0f), panelSize, 0, 0, 0.5f);
            new Wall(new Point3D(0, 0.25f, 0.4f), panelSize, 0.8f, false);
            new Wall(new Point3D(0.25f, 0, 0.4f), panelSize, 0.8f, true);
        }

        public Player initPlayer() {
            return new Player(new Pawn(new Point3D(Config.INIT_PLAYER_POS),0.4f,0.1f));
        }
    }, Particles (Perspective.FirstPerson, true) {
        public void init() {
            Particle p = new Particle(new Point3D(1, 1, 0), 1f);

            ArrayList<Particle> particles = new ArrayList<>();
            particles.add(p);

            ParticleGravity gravity = new ParticleGravity(Particle.getGravity());
            ParticleForceRegistry.get().add(p, gravity);

            ParticleDrag drag = new ParticleDrag(1, 1);
            ParticleForceRegistry.get().add(p, drag);

            this.world = new ParticleWorld(particles, 20, 5);
        }

        public Player initPlayer() {
            return new Player();
        }
    };

    public abstract void init();
    public abstract Player initPlayer();

    protected final int boardSize = 12;
    protected final float panelSize = 0.5f;

    protected Player player = null;
    protected Perspective perspective;
    protected ParticleWorld world = null;

    Scenario (Perspective perspective, boolean particleEngine) {
        this.perspective = perspective;
    }

    public Player getPlayer() {
        if (player == null)
            player = initPlayer();
        return this.player;
    }

    public Perspective getPerspective() {
        return this.perspective;
    }

    public void nextPerspective() {
        this.perspective = this.perspective.next();
    }

    public ParticleWorld world() {
        return world;
    }
}