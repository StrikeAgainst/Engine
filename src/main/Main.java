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
import engine.collision.ContactResolver;

public class Main extends JFrame implements ActionListener, KeyListener, ChangeListener, ItemListener {

	private static Scenario autoStartScenario = null;

	private Animator animator = null;
	private Renderer renderer = null;
	private Scenario scenario;
	private JComboBox<Scenario> scenario_picker;
	private JButton load_button;
	private JSlider speed_slider;
	private JSpinner contactIterations;
	private JCheckBox friction;
	private JLabel description;
	private double speed = 1;
	
	public Main() {
		super("Engine");

		JPanel text = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel control = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel south = new JPanel(new BorderLayout());

		description = new JLabel("Select a scenario and press 'Load'");
		text.add(description);

		Scenario[] scenarios = Scenario.values();
		scenario = scenarios[0];

		scenario_picker = new JComboBox<>(scenarios);
		scenario_picker.addActionListener(this);
		control.add(scenario_picker);

		load_button = new JButton("Load");
		load_button.addActionListener(this);
		control.add(load_button);

		speed_slider = new JSlider(JSlider.HORIZONTAL, 0, 200, 100);
		speed_slider.addChangeListener(this);
		control.add(speed_slider);

		contactIterations = new JSpinner(new SpinnerNumberModel(100, 1, 10000, 1));
		contactIterations.addChangeListener(this);
		control.add(contactIterations);

		friction = new JCheckBox("Friction");
		friction.addItemListener(this);
		control.add(friction);

		south.add(text, BorderLayout.NORTH);
		south.add(control, BorderLayout.SOUTH);
		this.add(south, BorderLayout.SOUTH);

		this.setVisible(true);
		this.setSize(800, 600);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				exit();
			}
		});

		if (autoStartScenario != null) {
			scenario = autoStartScenario;
			load();
		}
	}
	
	public static void main(String[] args) {
		new Main();
	}

	public void load() {
		GLCapabilities cap = new GLCapabilities(null);
		cap.setDoubleBuffered(true);

		Player player = scenario.initPlayer();
		Engine engine = new Engine(scenario, player);
		renderer = new Renderer(cap, engine, player);
		renderer.addKeyListener(this);

		animator = new Animator(renderer);
		animator.setUpdateFPSFrames(120, System.out);

		this.add(renderer, BorderLayout.CENTER);
		this.setVisible(true);
		animator.start();
		updateSpeed();
		renderer.requestFocus();
	}

	public void stop() {
		if (animator != null)
			animator.stop();

		if (renderer != null)
			this.remove(renderer);

		renderer = null;
	}

	public void reload() {
		stop();
		load();
	}

	public void exit() {
		stop();
		System.exit(0);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == load_button)
			reload();
		else if (e.getSource() == scenario_picker) {
			scenario = (Scenario) scenario_picker.getSelectedItem();
			if (scenario instanceof Scenario) {
				description.setText(scenario.description());
			}
		}
	}

	public void stateChanged(ChangeEvent e) {
		Object source = e.getSource();
		if (source == speed_slider) {
			speed = (double) speed_slider.getValue() / 100;
			updateSpeed();
		} else if (source == contactIterations) {
			ContactResolver.setMaxIterations((int) contactIterations.getValue());
		}
	}

	public void itemStateChanged(ItemEvent  e) {
		Object source = e.getSource();
		if (source == friction) {
			ContactResolver.DISABLE_FRICTION = (e.getStateChange() == ItemEvent.DESELECTED);
		}
	}

	public void updateSpeed() {
		if (renderer != null && animator != null) {
			if (speed == 0) {
				if (animator.isAnimating())
					animator.pause();
			} else {
				renderer.setSpeed(speed);
				if (animator.isPaused())
					animator.resume();
			}
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
				reload();
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
