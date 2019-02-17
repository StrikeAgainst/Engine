package main;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.util.Animator;
import engine.Engine;
import engine.Player;

public class Main extends JFrame implements ActionListener, KeyListener, ChangeListener {

	private Animator animator = null;
	private Renderer renderer = null;
	private Scenario scenario, autoStartScenario = Scenario.BoxContact;
	private JComboBox<Scenario> scenario_picker;
	private JButton load_button;
	
	public Main() {
		super("Engine");

		JPanel control = new JPanel(new FlowLayout(FlowLayout.LEFT));

		Scenario[] scenarios = Scenario.values();
		scenario = scenarios[0];

		scenario_picker = new JComboBox<>(scenarios);
		scenario_picker.addActionListener(this);
		control.add(scenario_picker);

		load_button = new JButton("Load");
		load_button.addActionListener(this);
		control.add(load_button);

		JSlider speed_slider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
		speed_slider.addChangeListener(this);
		control.add(speed_slider);

		this.add(control, BorderLayout.SOUTH);

		this.setVisible(true);
		this.setSize(800, 600);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		if (autoStartScenario != null)
			load(autoStartScenario);
	}
	
	public static void main(String[] args) {
		new Main();
	}

	public void load(Scenario scenario) {
		GLCapabilities cap = new GLCapabilities(null);
		cap.setDoubleBuffered(true);

		Player player = new Player(scenario.getPerspective());
		scenario.playerAttachObject(player);

		Engine engine = new Engine(scenario, player);
		renderer = new Renderer(cap, engine, player);
		renderer.addKeyListener(this);
		scenario.build();

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
		if (e.getSource() == load_button) {
			stop();
			load(scenario);
		} else if (e.getSource() == scenario_picker)
			scenario = (Scenario) scenario_picker.getSelectedItem();
	}

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		int speed = source.getValue();
		if (speed == 0) {
			if (animator.isAnimating())
				animator.pause();
		} else {
			renderer.setSpeed((float) speed/100);
			if (animator.isPaused())
				animator.resume();
		}
	}

	public void togglePause() {
		if (animator.isAnimating())
			animator.pause();
		else if (animator.isPaused())
			animator.resume();
	}

	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_R: {
				stop();
				load(scenario);
				break;
			}
			case KeyEvent.VK_P: {
				togglePause();
				break;
			}
			case KeyEvent.VK_Q: {
				exit();
			}
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
}
