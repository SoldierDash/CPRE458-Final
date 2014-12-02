import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;

public class AperiodicTaskDialogue extends JDialog implements ActionListener {
	
	private Integer startTime;
	private Integer computationTime;
	
	private JSpinner aperiodicStartTimeSpinner;
	private JSpinner aperiodicComputationTimeSpinner;
	private JButton aperiodicAddTaskButton;
	
	public AperiodicTaskDialogue() {
		
		this.startTime = null;
		this.computationTime = null;
		
		setResizable(false);
		setSize(300, 200);
		setModal(true);
		setTitle("Create Periodic Task");
		getContentPane().setLayout(null);
		
		JPanel aperiodicStartTimePanel = new JPanel();
		aperiodicStartTimePanel.setBounds(22, 20, 250, 30);
		getContentPane().add(aperiodicStartTimePanel);
		aperiodicStartTimePanel.setLayout(null);
		
		JLabel aperiodicStartTimeLabel = new JLabel("Start Time:");
		aperiodicStartTimeLabel.setBounds(16, 8, 150, 14);
		aperiodicStartTimePanel.add(aperiodicStartTimeLabel);
		
		aperiodicStartTimeSpinner = new JSpinner();
		aperiodicStartTimeSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		aperiodicStartTimeSpinner.setBounds(182, 5, 50, 20);
		aperiodicStartTimePanel.add(aperiodicStartTimeSpinner);
		
		JPanel aperiodicComputationTimePanel = new JPanel();
		aperiodicComputationTimePanel.setBounds(22, 70, 250, 30);
		getContentPane().add(aperiodicComputationTimePanel);
		aperiodicComputationTimePanel.setLayout(null);
		
		JLabel aperiodicComputationTimeLabel = new JLabel("Computation Time:");
		aperiodicComputationTimeLabel.setBounds(16, 8, 150, 14);
		aperiodicComputationTimePanel.add(aperiodicComputationTimeLabel);
		
		aperiodicComputationTimeSpinner = new JSpinner();
		aperiodicComputationTimeSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		aperiodicComputationTimeSpinner.setBounds(182, 5, 50, 20);
		aperiodicComputationTimePanel.add(aperiodicComputationTimeSpinner);
		
		JPanel aperiodicButtonPanel = new JPanel();
		aperiodicButtonPanel.setBounds(22, 120, 250, 30);
		getContentPane().add(aperiodicButtonPanel);
		aperiodicButtonPanel.setLayout(null);
		
		aperiodicAddTaskButton = new JButton("Add Task");
		aperiodicAddTaskButton.setBounds(80, 3, 89, 23);
		aperiodicAddTaskButton.addActionListener(this);
		aperiodicButtonPanel.add(aperiodicAddTaskButton);
		
	}

	public Integer getStartTime() {
		return this.startTime;
	}
	
	public Integer getComputationTime() {
		return this.computationTime;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == aperiodicAddTaskButton) {
			this.startTime = (int) aperiodicStartTimeSpinner.getValue();
			this.computationTime = (int) aperiodicComputationTimeSpinner.getValue();
			this.setVisible(false);
		}
	}
}
