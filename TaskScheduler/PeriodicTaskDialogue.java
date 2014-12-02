import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JButton;

public class PeriodicTaskDialogue extends JDialog implements ActionListener {
	
	private Integer computationTime;
	private Integer period;
	
	private JSpinner periodicComputationTimeSpinner;
	private JSpinner periodicPeriodSpinner;
	private JButton periodicAddTaskButton;
	
	public PeriodicTaskDialogue() {
		
		this.computationTime = null;
		this.period = null;
		
		setResizable(false);
		setSize(300, 200);
		setModal(true);
		setTitle("Create Periodic Task");
		getContentPane().setLayout(null);
		
		JPanel periodicComputationTimePanel = new JPanel();
		periodicComputationTimePanel.setBounds(22, 20, 250, 30);
		getContentPane().add(periodicComputationTimePanel);
		periodicComputationTimePanel.setLayout(null);
		
		JLabel periodicComputationTimeLabel = new JLabel("Computation Time:");
		periodicComputationTimeLabel.setBounds(16, 8, 150, 14);
		periodicComputationTimePanel.add(periodicComputationTimeLabel);
		
		periodicComputationTimeSpinner = new JSpinner();
		periodicComputationTimeSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		periodicComputationTimeSpinner.setBounds(182, 5, 50, 20);
		periodicComputationTimePanel.add(periodicComputationTimeSpinner);
		
		JPanel periodicPeriodPanel = new JPanel();
		periodicPeriodPanel.setBounds(22, 70, 250, 30);
		getContentPane().add(periodicPeriodPanel);
		periodicPeriodPanel.setLayout(null);
		
		JLabel periodicPeriodLabel = new JLabel("Period:");
		periodicPeriodLabel.setBounds(16, 8, 150, 14);
		periodicPeriodPanel.add(periodicPeriodLabel);
		
		periodicPeriodSpinner = new JSpinner();
		periodicPeriodSpinner.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		periodicPeriodSpinner.setBounds(182, 5, 50, 20);
		periodicPeriodPanel.add(periodicPeriodSpinner);
		
		JPanel periodicButtonPanel = new JPanel();
		periodicButtonPanel.setBounds(22, 120, 250, 30);
		getContentPane().add(periodicButtonPanel);
		periodicButtonPanel.setLayout(null);
		
		periodicAddTaskButton = new JButton("Add Task");
		periodicAddTaskButton.setBounds(80, 3, 89, 23);
		periodicAddTaskButton.addActionListener(this);
		periodicButtonPanel.add(periodicAddTaskButton);
		
	}

	public Integer getComputationTime() {
		return this.computationTime;
	}
	
	public Integer getPeriod() {
		return this.period;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == periodicAddTaskButton) {
			this.computationTime = (int) periodicComputationTimeSpinner.getValue();
			this.period = (int) periodicPeriodSpinner.getValue();
			this.setVisible(false);
		}
	}
}
