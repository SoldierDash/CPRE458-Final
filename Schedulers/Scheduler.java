package Schedulers;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public abstract class Scheduler {
	
	protected class PeriodicTask implements Comparable<PeriodicTask> {
		
		private String name;
		private int computationTime;
		private int period;
		private int timeRemaining;

		/*
		private int arrival_time = 0;
		private int start_time = 0;

		private int total_response_time = 0;
		private int total_execution_time = 0;
		private int total_runs = 0;
		*/

		public PeriodicTask(String name, int computationTime, int period) {
			this.name = name;
			this.computationTime = computationTime;
			this.period = period;
			this.timeRemaining = computationTime;
		} 
		
		public String getName() {
			return this.name;
		}
		
		public int getComputationTime() {
			return this.computationTime;
		}
		
		public int getPeriod() {
			return this.period;
		}
		
		public int getTimeRemaining() {
			return this.timeRemaining;
		}

		public void setTimeRemaining(int timeRemaining) {
			//if(timeRemaining != this.timeRemaining && arrival_time == 0)
			//	start_time = time;
			this.timeRemaining = timeRemaining;
		}
		
		public void refresh() {
			/*arrival_time = time;
			start_time = 0;
			total_runs++;*/
			this.timeRemaining = this.computationTime;

		}

		/*

		public double getAvgResponseTime() {

		}

		public double getAvgExecutionTime() {

		}

		public double getAvgCompletionTime() {

		}
		*/

		@Override
		public int compareTo(PeriodicTask arg0) {
			return this.period - arg0.period;
		}
		
	}
	
	protected PriorityQueue<PeriodicTask> periodicTasks;
	protected LinkedList<PeriodicTask> refreshList;
	protected AperiodicTaskQueue aperiodicTasks;
	protected boolean initialized;
	protected int time;
	protected int lcmOfPeriodicTasks;
	protected boolean scheduleable;
	protected List<String> completion_list;
	
	public Scheduler() {
		this.periodicTasks = new PriorityQueue<PeriodicTask>();
		this.refreshList = new LinkedList<PeriodicTask>();
		this.aperiodicTasks = new AperiodicTaskQueue();
		this.initialized = false;
		this.time = 0;
		this.lcmOfPeriodicTasks = 0;
		this.scheduleable = false;
	}
	
	public void addPeriodicTask(String name, int computationTime, int period) {
		if (this.initialized == true) {
			throw new IllegalStateException("Tasks cannot be added once the scheduler has been initialized.");
		}
		this.periodicTasks.add(new PeriodicTask(name, computationTime, period));
	}
	
	public void addAperiodicTask(String name, int arrivalTime, int computationTime) {
		if (this.initialized == true) {
			throw new IllegalStateException("Tasks cannot be added once the scheduler has been initialized.");
		}
		this.aperiodicTasks.addTask(name, arrivalTime, computationTime);
	}
	
	public void initialize() {
		this.initialized = true;
		this.aperiodicTasks.initialize();
		this.lcmOfPeriodicTasks = lcmPeriodicTasks();
		this.scheduleable = isScheduleable();
	}
	
	public boolean isInitialized() {
		return this.initialized;
	}

	public void runToCompletion() {
		if(!initialized)
			initialize();

		completion_list = new LinkedList<String>();
		while (!this.isDone()) {
			completion_list.add(this.getNextTask());
		}
	}
	
	public abstract String getNextTask();

	public List<String> getOutput() {return completion_list;}
	
	public boolean isScheduleable() {
		if (this.initialized == false) {
			throw new IllegalStateException("Scheudlers must be initialized before they can be used.");
		}
		double sum = 0.0;
		for (PeriodicTask pt : periodicTasks) {
			sum += ((pt.getComputationTime() * 1.0) / pt.getPeriod());
		}
		
		if (periodicTasks.size() == 0) {
			return true;
		}
		
		double exponent = (1.0 / (periodicTasks.size()));
		double max = (periodicTasks.size()) * (Math.pow(2, exponent) - 1);
		return sum <= max;
	}
	
	/*
	 * Please note, the code used to calculate the LCM of the periodic tasks is taken from the following webpage:
	 * http://stackoverflow.com/questions/4201860/how-to-find-gcf-lcm-on-a-set-of-numbers
	 */
	public int lcmPeriodicTasks() {
		if (this.initialized == false) {
			throw new IllegalStateException("Scheudlers must be initialized before they can be used.");
		}
		if (periodicTasks.size() == 0) {
			return 0;
		}
		int lcm = 0;
		int[] input = new int[periodicTasks.size()];
		int i = 0;
		for (PeriodicTask pt : periodicTasks) {
			input[i++] = pt.getPeriod();
		}
		
		lcm = input[0];
		for (i = 0; i < input.length; i++) {
			lcm = lcm(lcm, input[i]);
		}
	
		return lcm;
	}
	
	private static int lcm(int a, int b) {
		return a * (b / gcd(a, b));
	}
	
	private static int gcd(int a, int b) {
		while (b > 0) {
			int temp = b;
			b = a % b;
			a = temp;
		}
		return a;
	}
	
	public boolean isDone() {
		if (this.initialized == false) {
			throw new IllegalStateException("Scheudlers must be initialized before they can be used.");
		}
		return (time >= lcmOfPeriodicTasks && aperiodicTasks.isDone());
	}
	
	public int getSimulationTime() {
		if (this.initialized == false) {
			throw new IllegalStateException("Scheudlers must be initialized before they can be used.");
		}
		return this.time;
	}
	
	public List<AperiodicTaskQueue.AperiodicTask> getAperiodicTasks() {
		return aperiodicTasks.getTasks();
	}

	public List<PeriodicTask> getPeriodicTasks() { return refreshList; }

	public int finishAperiodicTime() {
		int time = 0;
		for(AperiodicTaskQueue.AperiodicTask at: aperiodicTasks.getTasks())
			if(at.getTimeEnded() > time)
				time = at.getTimeEnded();
		return time;
	}

	public double avgAperiodicResponse() {
		double total = 0, size = aperiodicTasks.getTasks().size();
		if(size == 0)
			return 0;
		for(AperiodicTaskQueue.AperiodicTask at: aperiodicTasks.getTasks())
			total += at.getAvgResponseTime();
		return total/aperiodicTasks.getTasks().size();
	}

	public double avgAperiodicExecution() {
		double total = 0, size = aperiodicTasks.getTasks().size();
		if(size == 0)
			return 0;
		for(AperiodicTaskQueue.AperiodicTask at: aperiodicTasks.getTasks())
			total += at.getAvgExecutionTime();
		return total/aperiodicTasks.getTasks().size();
	}

	public double avgAperiodicCompletion() {
		double total = 0;
		for(AperiodicTaskQueue.AperiodicTask at: aperiodicTasks.getTasks())
			total += at.getAvgCompletionTime();
		return total/aperiodicTasks.getTasks().size();
	}

	public abstract String getName();
}
