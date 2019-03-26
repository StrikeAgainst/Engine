package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Maze {
	
	private final int N;
    private boolean[][] north, south, west, east, visited;
    private Random random = new Random();
    
    private Maze(int size) {
        this.N = size;
        visited = new boolean[size+2][N+2];
        for (int i = 0; i < N+2; i++) {
            visited[i][0] = true;
            visited[i][N+1] = true;
            visited[0][i] = true;
            visited[N+1][i] = true;
        }

        north = new boolean[N+2][N+2];
        south = new boolean[N+2][N+2];
        west  = new boolean[N+2][N+2];
        east  = new boolean[N+2][N+2];
        for (int x = 0; x < N+2; x++) {
            for (int y = 0; y < N+2; y++) {
                north[x][y] = true;
                south[x][y] = true;
                west[x][y]  = true;
                east[x][y]  = true;
            }
        }
    }
    
    public Maze(int size, boolean[][] north, boolean[][] south, boolean[][] west, boolean[][] east) {
        this.N = size;
        this.north = north;
        this.south = south;
        this.west = west;
        this.east = east;
    }
    
    public static void main(String[] args) {
        Maze m = Maze.createRandomMaze(12);
        m.print();
    }
	
	public static Maze createRandomMaze(int size) {
		Maze m = new Maze(size);
		m.generate(1, 1);
		return m;
	}
	
	private void generate(int x, int y) {
        visited[x][y] = true;
        while (!visited[x][y+1] || !visited[x+1][y] || !visited[x][y-1] || !visited[x-1][y])
            while (true) {
                double r = random.nextInt(4);
                if (r == 0 && !visited[x][y+1]) {
                	south[x][y] = false;
                    north[x][y+1] = false;
                    generate(x, y+1);
                    break;
                } else if (r == 1 && !visited[x+1][y]) {
                    east[x][y] = false;
                    west[x+1][y] = false;
                    generate(x+1, y);
                    break;
                } else if (r == 2 && !visited[x][y-1]) {
                    north[x][y] = false;
                    south[x][y-1] = false;
                    generate(x, y-1);
                    break;
                } else if (r == 3 && !visited[x-1][y]) {
                    west[x][y] = false;
                    east[x-1][y] = false;
                    generate(x-1, y);
                    break;
                }
            }
    }
	
	public int getSize() {
		return N;
	}
	
	public boolean[][] getNorth() {
		return north;
	}
	
	public boolean[][] getSouth() {
		return south;
	}
	
	public boolean[][] getWest() {
		return west;
	}
	
	public boolean[][] getEast() {
		return east;
	}
	
	public void print() {
		new MazePrint();
	}
	
	public class MazePrint extends JFrame {

		private static final long serialVersionUID = 1L;
		private final int scale = 30;
		private ImgCanvas canvas;
		private Graphics2D g;
		private Image canvasImage;
		
		public MazePrint() {
	        canvas = new ImgCanvas();
			canvas.setPreferredSize(new Dimension((N+2)*scale, (N+2)*scale));
			
			this.add(canvas, BorderLayout.CENTER);

			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e) {
					dispose();
					System.exit(0);
				}
			});
			pack();
			Dimension d = canvas.getSize();
			canvasImage = canvas.createImage(d.width, d.height);
			g = (Graphics2D) canvasImage.getGraphics();
			this.setLocation(new Point(800, 0));
			setVisible(true);
			paint();
	    }
		
		public void paint() {
			g.setColor(Color.RED);
			g.fillOval(N*scale, N*scale, scale, scale);
			g.fillOval(scale, scale, scale, scale);
			g.setColor(Color.BLACK);
	        for (int x = 1; x <= N; x++) {
	        	for (int y = 1; y <= N; y++) {
					if (north[x][y]) g.drawLine(x*scale, y*scale, (x+1)*scale, y*scale);
					if (south[x][y]) g.drawLine(x*scale, (y+1)*scale, (x+1)*scale, (y+1)*scale);
					if (west[x][y]) g.drawLine(x*scale, y*scale, x*scale, (y+1)*scale);
					if (east[x][y]) g.drawLine((x+1)*scale, y*scale, (x+1)*scale, (y+1)*scale);
	            }
	        }
			repaint();
		}
	
		public class ImgCanvas extends JPanel {
			private static final long serialVersionUID = 1L;
	
			public void paint(Graphics g) {
				g.drawImage(canvasImage, 0, 0, null);
			}
		}
	}

}
