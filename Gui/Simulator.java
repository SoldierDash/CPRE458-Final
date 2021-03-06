package Gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import Schedulers.BackgroundScheduler;
import Schedulers.DeferrableScheduler;
import Schedulers.PollingScheduler;

public class Simulator extends JFrame implements ActionListener {
	
	private class PeriodicTask {
		
		private int id;
		private String name;
		private int computation;
		private int period;
		
		public PeriodicTask(int id, int computationTime, int period) {
			this.id = id;
			this.computation = computationTime;
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
			this.computation = computationTime;
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
	
	final JFileChooser fc;
	
	private ImagePanel imagePanel;
	private JSpinner serverComputationTimeSpinner;
	private JSpinner serverPeriodSpinner;
	private JSpinner maxSimulationTimeSpinner;
	private JButton addPeriodicTaskButton;
	private JButton removePeriodicTaskButton;
	private JButton addAperiodicTaskButton;
	private JButton removeAperiodicTaskButton;
	private JButton startButton;
	private JButton saveButton;
	private JButton resetButton;
	private JPanel schedulePanel;
	private JScrollPane scheduleScrollPane;
	
	private DefaultListModel<PeriodicTask> periodicTasks;
	private JList<PeriodicTask> periodicTasksList;
	private DefaultListModel<AperiodicTask> aperiodicTasks;
	private JList<AperiodicTask> aperiodicTasksList;
	private HashMap<String, Color> taskColors;
	private int nextColorIndex;
	
	public Simulator() {
		
		fc = new JFileChooser();
		
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
		serverSettingsPanel.setBounds(0, 0, 550, 50);
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
		
		JPanel simulationTimePanel = new JPanel();
		simulationTimePanel.setBounds(0, 300, 550, 50);
		controlPanel.add(simulationTimePanel);
		simulationTimePanel.setLayout(null);
		
		JLabel maxSimulationTime = new JLabel("Max Simulation Time:");
		maxSimulationTime.setHorizontalAlignment(SwingConstants.CENTER);
		maxSimulationTime.setBounds(116, 18, 150, 14);
		simulationTimePanel.add(maxSimulationTime);
		
		maxSimulationTimeSpinner = new JSpinner();
		maxSimulationTimeSpinner.setModel(new SpinnerNumberModel(new Integer(100), new Integer(1), null, new Integer(1)));
		maxSimulationTimeSpinner.setBounds(382, 15, 50, 20);
		simulationTimePanel.add(maxSimulationTimeSpinner);
		
		JPanel taskPanel = new JPanel();
		taskPanel.setBounds(0, 50, 550, 250);
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
		startStopPanel.setBounds(0, 350, 550, 50);
		controlPanel.add(startStopPanel);
		startStopPanel.setLayout(null);
		
		startButton = new JButton("Start");
		startButton.setBounds(142, 13, 90, 23);
		startButton.addActionListener(this);
		startStopPanel.add(startButton);
		
		saveButton = new JButton("Save");
		saveButton.setEnabled(false);
		saveButton.setBounds(230, 13, 90, 23);
		saveButton.addActionListener(this);
		startStopPanel.add(saveButton);
		
		resetButton = new JButton("Reset");
		resetButton.setEnabled(false);
		resetButton.setBounds(318, 13, 90, 23);
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
		} else if (arg0.getSource() == saveButton) {
			save();
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
	
	private void save() {
	
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File outputfile = fc.getSelectedFile();
			try {
				ImageIO.write(imagePanel.getImage(), "png", outputfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}
	
	private void reset() {
		setButtonsEnabled(true);
		saveButton.setEnabled(false);
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
		maxSimulationTimeSpinner.setEnabled(enabled);
		addPeriodicTaskButton.setEnabled(enabled);
		removePeriodicTaskButton.setEnabled(enabled);
		addAperiodicTaskButton.setEnabled(enabled);
		removeAperiodicTaskButton.setEnabled(enabled);
		startButton.setEnabled(enabled);
		saveButton.setEnabled(enabled);
		resetButton.setEnabled(enabled);
	}
	
	private void drawSchedule() {
		BackgroundScheduler bs = new BackgroundScheduler();
		PollingScheduler ps = new PollingScheduler((Integer) serverComputationTimeSpinner.getValue(), (Integer) serverPeriodSpinner.getValue());
		DeferrableScheduler ds = new DeferrableScheduler((Integer) serverComputationTimeSpinner.getValue(), (Integer) serverPeriodSpinner.getValue());
		
		for (int i = 0; i < periodicTasks.size(); i++) {
			PeriodicTask pt = periodicTasks.get(i);
			bs.addPeriodicTask(pt.getName(), pt.getComputation(), pt.getPeriod());
			ps.addPeriodicTask(pt.getName(), pt.getComputation(), pt.getPeriod());
			ds.addPeriodicTask(pt.getName(), pt.getComputation(), pt.getPeriod());
		}
		
		for (int i = 0; i < aperiodicTasks.size(); i++) {
			AperiodicTask at = aperiodicTasks.get(i);
			bs.addAperiodicTask(at.getName(), at.getStart(), at.getComputation());
			ps.addAperiodicTask(at.getName(), at.getStart(), at.getComputation());
			ds.addAperiodicTask(at.getName(), at.getStart(), at.getComputation());
		}
		
		bs.initialize();
		ps.initialize();
		ds.initialize();
		
		/* Check Schedulability here. */
		String message = "";
		boolean schedulable = true;
		if (bs.isScheduleable() == false) {
			message += "Task set is not schedulable using Background Scheduling Algorithm." + System.lineSeparator();
			schedulable = false;
		}
		if (ps.isScheduleable() == false) {
			message += "Task set is not schedulable using Polling Server Scheduling Algorithm." + System.lineSeparator();
			schedulable = false;
		}
		if (ps.isScheduleable() == false) {
			message += "Task set is not schedulable using Deferrable Server Scheduling Algorithm." + System.lineSeparator();
			schedulable = false;
		}
		if (schedulable == false) {
			JOptionPane.showMessageDialog(this, message, "Schedulability Error", JOptionPane.ERROR_MESSAGE);
			reset();
			return;
		}
		
		
		
		ArrayList<String> backgroundSchedule = new ArrayList<String>();
		ArrayList<String> pollingSchedule = new ArrayList<String>();
		ArrayList<String> deferrableSchedule = new ArrayList<String>();
		
		for (int i = 0; (bs.isDone() == false || ps.isDone() == false || ds.isDone() == false) && i < (Integer) maxSimulationTimeSpinner.getValue(); i++) {
			backgroundSchedule.add(bs.getNextTask());
			pollingSchedule.add(ps.getNextTask());
			deferrableSchedule.add(ds.getNextTask());
		}
		
		imagePanel = new ImagePanel(550, 300);
		scheduleScrollPane.setViewportView(imagePanel);
		
		Color color = null;
		Graphics2D g = (Graphics2D) imagePanel.getGraphics();
		
		int x = 5;
		int boxSize = 60;
		taskColors.put("", Color.WHITE);
		g.setColor(Color.BLACK);
		g.drawString("Background Server Schedule:", x, 45);
		g.drawString("Polling Server Schedule:", x, 145);
		g.drawString("Deferrable Server Schedule:", x, 245);
		x = 200;
		for (int i = 0; i < pollingSchedule.size(); i++) {
			/* Resize image if necessary */
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
		
			g = (Graphics2D) imagePanel.getGraphics();
			
			/* Draw background schedule */
			color = taskColors.get(backgroundSchedule.get(i));
			if (color == null) {
				color = COLORS[nextColorIndex++];
				taskColors.put(backgroundSchedule.get(i), color);
				if (nextColorIndex == 20) {
					nextColorIndex = 0;
				}
			}
			g.setColor(color);
			g.fillRect(x, 15, boxSize, boxSize);
			g.setColor(Color.BLACK);
			g.drawRect(x, 15, boxSize, boxSize);
			g.drawString(backgroundSchedule.get(i), x + 5, 45);
			g.drawString(Integer.toString(i), x, 90);
			
			/* Draw polling schedule */
			color = taskColors.get(pollingSchedule.get(i));
			if (color == null) {
				color = COLORS[nextColorIndex++];
				taskColors.put(pollingSchedule.get(i), color);
				if (nextColorIndex == 20) {
					nextColorIndex = 0;
				}
			}
			g.setColor(color);
			g.fillRect(x, 115, boxSize, boxSize);
			g.setColor(Color.BLACK);
			g.drawRect(x, 115, boxSize, boxSize);
			g.drawString(pollingSchedule.get(i), x + 5, 145);
			g.drawString(Integer.toString(i), x, 190);
			
			/* Draw deferrable schedule */
			color = taskColors.get(deferrableSchedule.get(i));
			if (color == null) {
				color = COLORS[nextColorIndex++];
				taskColors.put(deferrableSchedule.get(i), color);
				if (nextColorIndex == 20) {
					nextColorIndex = 0;
				}
			}
			g.setColor(color);
			g.fillRect(x, 215, boxSize, boxSize);
			g.setColor(Color.BLACK);
			g.drawRect(x, 215, boxSize, boxSize);
			g.drawString(deferrableSchedule.get(i), x + 5, 245);
			g.drawString(Integer.toString(i), x, 290);
			
			x += boxSize;
		}

		saveButton.setEnabled(true);
		resetButton.setEnabled(true);
	}
	
	public static void main(String[] args) {
		Simulator simulator = new Simulator();
		simulator.setSize(600, 800);
		simulator.setVisible(true);
	}
}
