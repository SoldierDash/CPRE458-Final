import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

public class Simulator extends JFrame implements ActionListener {
	
	private class PeriodicTask {
		
		private int id;
		private String name;
		private int computation;
		private int period;
		
		public PeriodicTask(int id, int computation, int period) {
			this.id = id;
			this.computation = computation;
			this.period = period;
		}
		
		public int getID() {
			return this.id;
		}
		
		public void setID(int id) {
			this.id = id;
		}
		
		public String getName() {
			return "PT " + this.id;
		}
		
		public int getComputation() {
			return this.computation;
		}
		
		public int getPeriod() {
			return this.period;
		}
		
		public String toString() {
			return "PT " + this.id + " (Computation Time: " + this.computation + ", Period: " + this.period + ")";
		}
		
	}
	
	private class AperiodicTask {
		
		private int id;
		private String name;
		private int start;
		private int computation;
		
		public AperiodicTask(int id, int start, int computationTime) {
			this.id = id;
			this.start = start;
			this.computation = computation;
		}
		
		public int getID() {
			return this.id;
		}
		
		public void setID(int id) {
			this.id = id;
		}
		
		public String getName() {
			return "AT " + this.id;
		}
		
		public int getStart() {
			return this.start;
		}
		
		public int getComputation() {
			return this.computation;
		}
		
		public String toString() {
			return "AT " + this.id + " (Start Time: " + this.start + ", Computation Time: " + this.computation + ")";
		}
	}
	
	/*
	 * Colors taken from: http://stackoverflow.com/a/4382138
	 */
	private static Color[] COLORS = {
	    new Color(0xFFFFB300), //Vivid Yellow
	    new Color(0xFF803E75), //Strong Purple
	    new Color(0xFFFF6800), //Vivid Orange
	    new Color(0xFFA6BDD7), //Very Light Blue
	    new Color(0xFFC10020), //Vivid Red
	    new Color(0xFFCEA262), //Grayish Yellow
	    new Color(0xFF817066), //Medium Gray
	    new Color(0xFF007D34), //Vivid Green
	    new Color(0xFFF6768E), //Strong Purplish Pink
	    new Color(0xFF00538A), //Strong Blue
	    new Color(0xFFFF7A5C), //Strong Yellowish Pink
	    new Color(0xFF53377A), //Strong Violet
	    new Color(0xFFFF8E00), //Vivid Orange Yellow
	    new Color(0xFFB32851), //Strong Purplish Red
	    new Color(0xFFF4C800), //Vivid Greenish Yellow
	    new Color(0xFF7F180D), //Strong Reddish Brown
	    new Color(0xFF93AA00), //Vivid Yellowish Green
	    new Color(0xFF593315), //Deep Yellowish Brown
	    new Color(0xFFF13A13), //Vivid Reddish Orange
	    new Color(0xFF232C16), //Dark Olive Green
	};
	
	private JSpinner serverComputationTimeSpinner;
	private JSpinner serverPeriodSpinner;
	private JButton addPeriodicTaskButton;
	private JButton removePeriodicTaskButton;
	private JButton addAperiodicTaskButton;
	private JButton removeAperiodicTaskButton;
	private JButton startButton;
	private JButton resetButton;
	private JPanel schedulePanel;
	private JScrollPane scheduleScrollPane;
	
	private Scheduler scheduler;
	private DefaultListModel<PeriodicTask> periodicTasks;
	private JList<PeriodicTask> periodicTasksList;
	private DefaultListModel<AperiodicTask> aperiodicTasks;
	private JList<AperiodicTask> aperiodicTasksList;
	private HashMap<String, Color> taskColors;
	private int nextColorIndex;
	
	public Simulator() {
		
		/* This will be replaced with different schedulers based upon the scheduling algorithm selected once they are implemented. */
		nextColorIndex = 0;
		taskColors = new HashMap<String, Color>();
		
		setResizable(false);
		getContentPane().setLayout(null);
		
		schedulePanel = new JPanel();
		schedulePanel.setBounds(22, 23, 550, 300);
		getContentPane().add(schedulePanel);
		schedulePanel.setLayout(null);
		
		scheduleScrollPane = new JScrollPane();
		scheduleScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scheduleScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scheduleScrollPane.setBounds(0, 0, 550, 300);
		schedulePanel.add(scheduleScrollPane);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setBounds(22, 346, 550, 400);
		getContentPane().add(controlPanel);
		controlPanel.setLayout(null);
		
		JPanel serverSettingsPanel = new JPanel();
		serverSettingsPanel.setBounds(0, 12, 550, 50);
		controlPanel.add(serverSettingsPanel);
		serverSettingsPanel.setLayout(null);
		
		JLabel serverComputationTimeLabel = new JLabel("Server Computation Time:");
		serverComputationTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		serverComputationTimeLabel.setBounds(40, 18, 150, 14);
		serverSettingsPanel.add(serverComputationTimeLabel);
		
		serverComputationTimeSpinner = new JSpinner();
		serverComputationTimeSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		serverComputationTimeSpinner.setBounds(230, 15, 50, 20);
		serverSettingsPanel.add(serverComputationTimeSpinner);
		
		JLabel serverPeriodLabel = new JLabel("Server Period:");
		serverPeriodLabel.setHorizontalAlignment(SwingConstants.CENTER);
		serverPeriodLabel.setBounds(320, 18, 100, 14);
		serverSettingsPanel.add(serverPeriodLabel);
		
		serverPeriodSpinner = new JSpinner();
		serverPeriodSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		serverPeriodSpinner.setBounds(460, 15, 50, 20);
		serverSettingsPanel.add(serverPeriodSpinner);
		
		JPanel taskPanel = new JPanel();
		taskPanel.setBounds(0, 74, 550, 250);
		controlPanel.add(taskPanel);
		taskPanel.setLayout(null);
		
		JPanel periodicTasksPanel = new JPanel();
		periodicTasksPanel.setBounds(33, 25, 225, 200);
		taskPanel.add(periodicTasksPanel);
		periodicTasksPanel.setLayout(null);
		
		JLabel periodicTasksLabel = new JLabel("Periodic Tasks");
		periodicTasksLabel.setBounds(62, 9, 100, 14);
		periodicTasksLabel.setHorizontalAlignment(SwingConstants.CENTER);
		periodicTasksPanel.add(periodicTasksLabel);
		
		ScrollPane periodicTasksScrollPane = new ScrollPane();
		periodicTasksScrollPane.setBounds(12, 32, 200, 125);
		periodicTasksPanel.add(periodicTasksScrollPane);
		
		periodicTasks = new DefaultListModel<PeriodicTask>();
		periodicTasksList = new JList<PeriodicTask>(periodicTasks);
		periodicTasksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		periodicTasksList.setBounds(111, 109, 1, 1);
		periodicTasksScrollPane.add(periodicTasksList);
		
		JPanel periodicAddRemoveButtonPanel = new JPanel();
		periodicAddRemoveButtonPanel.setBounds(12, 166, 200, 25);
		periodicTasksPanel.add(periodicAddRemoveButtonPanel);
		periodicAddRemoveButtonPanel.setLayout(null);
		
		addPeriodicTaskButton = new JButton("Add");
		addPeriodicTaskButton.setBounds(7, 1, 89, 23);
		addPeriodicTaskButton.addActionListener(this);
		periodicAddRemoveButtonPanel.add(addPeriodicTaskButton);
		
		removePeriodicTaskButton = new JButton("Remove");
		removePeriodicTaskButton.setBounds(103, 1, 89, 23);
		removePeriodicTaskButton.addActionListener(this);
		periodicAddRemoveButtonPanel.add(removePeriodicTaskButton);
		
		JPanel aperiodicTasksPanel = new JPanel();
		aperiodicTasksPanel.setBounds(291, 25, 225, 200);
		taskPanel.add(aperiodicTasksPanel);
		aperiodicTasksPanel.setLayout(null);
		
		JLabel aperiodicTasksLabel = new JLabel("Aperiodic Tasks");
		aperiodicTasksLabel.setBounds(62, 9, 100, 14);
		aperiodicTasksLabel.setHorizontalAlignment(SwingConstants.CENTER);
		aperiodicTasksPanel.add(aperiodicTasksLabel);
		
		ScrollPane aperiodicTasksScrollPane = new ScrollPane();
		aperiodicTasksScrollPane.setBounds(12, 32, 200, 125);
		aperiodicTasksPanel.add(aperiodicTasksScrollPane);
		
		aperiodicTasks = new DefaultListModel<AperiodicTask>();
		aperiodicTasksList = new JList<AperiodicTask>(aperiodicTasks);
		aperiodicTasksList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		aperiodicTasksList.setBounds(111, 109, 1, 1);
		aperiodicTasksScrollPane.add(aperiodicTasksList);
		
		JPanel aperiodicAddRemoveButtonPanel = new JPanel();
		aperiodicAddRemoveButtonPanel.setBounds(12, 166, 200, 25);
		aperiodicTasksPanel.add(aperiodicAddRemoveButtonPanel);
		aperiodicAddRemoveButtonPanel.setLayout(null);
		
		addAperiodicTaskButton = new JButton("Add");
		addAperiodicTaskButton.setBounds(7, 1, 89, 23);
		addAperiodicTaskButton.addActionListener(this);
		aperiodicAddRemoveButtonPanel.add(addAperiodicTaskButton);
		
		removeAperiodicTaskButton = new JButton("Remove");
		removeAperiodicTaskButton.setBounds(103, 1, 89, 23);
		removeAperiodicTaskButton.addActionListener(this);
		aperiodicAddRemoveButtonPanel.add(removeAperiodicTaskButton);
		
		JPanel startStopPanel = new JPanel();
		startStopPanel.setBounds(0, 336, 550, 50);
		controlPanel.add(startStopPanel);
		startStopPanel.setLayout(null);
		
		startButton = new JButton("Start");
		startButton.setBounds(185, 13, 90, 23);
		startButton.addActionListener(this);
		startStopPanel.add(startButton);
		
		resetButton = new JButton("Reset");
		resetButton.setEnabled(false);
		resetButton.setBounds(275, 13, 90, 23);
		resetButton.addActionListener(this);
		startStopPanel.add(resetButton);
		
		reset();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == addPeriodicTaskButton) {
			addPeriodicTaskButtonClicked();
		} else if (arg0.getSource() == removePeriodicTaskButton) {
			removePeriodicTaskButtonClicked();
		} else if (arg0.getSource() == addAperiodicTaskButton) {
			addAperiodicTaskButtonClicked();
		} else if (arg0.getSource() == removeAperiodicTaskButton) {
			removeAperiodicTaskButtonClicked();
		} else if (arg0.getSource() == startButton) {
			start();
		} else if (arg0.getSource() == resetButton) {
			reset();
		}
	}
	
	private void addPeriodicTaskButtonClicked() {
		PeriodicTaskDialogue ptd = new PeriodicTaskDialogue();
		ptd.setVisible(true);
		
		Integer computation = ptd.getComputationTime();
		Integer period = ptd.getPeriod();
		
		if (computation != null && period != null) {
			periodicTasks.addElement(new PeriodicTask(periodicTasks.size() + 1, computation, period));
		}
		
		ptd = null;
	}
	
	private void removePeriodicTaskButtonClicked() {
		int selected = periodicTasksList.getSelectedIndex();
		if (selected != -1) {
			periodicTasks.removeElementAt(selected);
			for (int i = 0; i < periodicTasks.getSize(); i++) {
				periodicTasks.get(i).setID(i + 1);
			}
		}
	}
	
	private void addAperiodicTaskButtonClicked() {
		AperiodicTaskDialogue atd = new AperiodicTaskDialogue();
		atd.setVisible(true);
		
		Integer start = atd.getStartTime();
		Integer computation = atd.getComputationTime();
		
		if (start != null && computation != null) {
			aperiodicTasks.addElement(new AperiodicTask(aperiodicTasks.size() + 1, start, computation));
		}
		
		atd = null;
	}
	
	private void removeAperiodicTaskButtonClicked() {
		int selected = aperiodicTasksList.getSelectedIndex();
		if (selected != -1) {
			aperiodicTasks.removeElementAt(selected);
			for (int i = 0; i < aperiodicTasks.getSize(); i++) {
				aperiodicTasks.get(i).setID(i + 1);
			}
		}
	}
	
	private void start() {
		
		if (periodicTasks.getSize() == 0 && aperiodicTasks.getSize() == 0) {
			return;
		}
		
		setButtonsEnabled(false);

		new Thread(new Runnable() {

			@Override
			public void run() {
				drawSchedule();
			}
			
		}).start();
		
	}
	
	private void reset() {
		setButtonsEnabled(true);
		resetButton.setEnabled(false);
		nextColorIndex = 0;
		taskColors.clear();
		scheduleScrollPane.setViewportView(new ImagePanel(550, 300));
		scheduleScrollPane.repaint();
	}
	
	/*
	 * Enables all buttons if the value passed in is true; otherwise disables all buttons.
	 */
	private void setButtonsEnabled(boolean enabled) {
		serverComputationTimeSpinner.setEnabled(enabled);
		serverPeriodSpinner.setEnabled(enabled);
		addPeriodicTaskButton.setEnabled(enabled);
		removePeriodicTaskButton.setEnabled(enabled);
		addAperiodicTaskButton.setEnabled(enabled);
		removeAperiodicTaskButton.setEnabled(enabled);
		startButton.setEnabled(enabled);
		resetButton.setEnabled(enabled);
	}
	
	/*
	 * Draws the schedule.
	 * Currently the code given is just a sample - it does not actually work yet.
	 */
	private void drawSchedule() {
		scheduler = new TestScheduler((int) serverComputationTimeSpinner.getValue(), (int) serverPeriodSpinner.getValue());
		for (int i = 0; i < periodicTasks.getSize(); i++) {
			scheduler.addPeriodicTask(periodicTasks.get(i).getName(), periodicTasks.get(i).getComputation(), periodicTasks.get(i).getPeriod());
		}
		
		for (int i = 0; i < aperiodicTasks.getSize(); i++) {
			scheduler.addAperiodicTask(aperiodicTasks.get(i).getName(), aperiodicTasks.get(i).getStart(), aperiodicTasks.get(i).getComputation());
		}
	
		
		scheduler.run(10);
		List<? extends Task> tasks = scheduler.getSchedule();
		
		ImagePanel imagePanel = new ImagePanel(550, 300);
		scheduleScrollPane.setViewportView(imagePanel);
		
		taskColors.put(null, Color.WHITE);
		int boxSize = 30;
		int x = 0;
		for (int i = 0; i < tasks.size(); i++) {
			if (x + boxSize > imagePanel.getImage().getWidth()) {
				int width = imagePanel.getImage().getWidth();
				int height = imagePanel.getImage().getHeight();
				ImagePanel temp = new ImagePanel(width + 10 * boxSize, height);
				Graphics2D g1 = (Graphics2D) temp.getImage().createGraphics();
				g1.drawImage(imagePanel.getImage(), 0, 0, null);
				imagePanel = temp;
				scheduleScrollPane.setViewportView(imagePanel);
				scheduleScrollPane.repaint();
			}
			
			Color color = taskColors.get(tasks.get(i).getName());
			if (color == null) {
				color = COLORS[nextColorIndex++];
				taskColors.put(tasks.get(i).getName(), color);
				if (nextColorIndex == 20) {
					nextColorIndex = 0;
				}
			}
			Graphics2D g = (Graphics2D) imagePanel.getGraphics();
			g.setColor(color);
			g.fillRect(x, 10, boxSize, boxSize);
			g.setColor(Color.BLACK);
			g.drawRect(x, 10, boxSize, boxSize);
			x += boxSize;
		}

		imagePanel.saveImage("test.png");
		resetButton.setEnabled(true);
	}
	
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		simulator.setSize(600, 800);
		simulator.setVisible(true);
	}
}
