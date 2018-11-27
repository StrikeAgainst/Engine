package engine;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.util.Animator;

public class Main extends JFrame implements ActionListener, KeyListener {

	private Animator animator = null;
	private Renderer renderer = null;
	private Scenario scenario = Scenario.values()[0];
	private JComboBox<Scenario> scenario_picker;
	private JButton load;
	
	public Main() {
		super("Engine");

		JPanel control = new JPanel(new FlowLayout(FlowLayout.LEFT));

		scenario_picker = new JComboBox<>(Scenario.values());
		scenario_picker.addActionListener(this);
		control.add(scenario_picker);

		load = new JButton("Load");
		load.addActionListener(this);
		control.add(load);

		this.add(control, BorderLayout.SOUTH);

		this.setVisible(true);
		this.setSize(800, 600);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});
	}
	
	public static void main(String[] args) {
		new Main();
	}

	public void load(Scenario scenario) {
		GLCapabilities cap = new GLCapabilities(null);
		cap.setDoubleBuffered(true);
		Engine engine = new Engine(scenario);
		renderer = new Renderer(cap, engine, scenario);
		renderer.addKeyListener(this);
		scenario.init();

		animator = new Animator(renderer);
		animator.setUpdateFPSFrames(120, System.out);

		this.add(renderer, BorderLayout.CENTER);
		this.setVisible(true);
		animator.start();
		renderer.requestFocus();
	}

	public void stop() {
		if (animator != null)
			animator.stop();

		if (renderer != null)
			this.remove(renderer);

		renderer = null;
	}

	public void exit() {
		stop();
		System.exit(0);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == load) {
			stop();
			load(scenario);
		} else if (e.getSource() == scenario_picker)
			scenario = (Scenario) scenario_picker.getSelectedItem();
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_Q: {
				exit();
			}
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
