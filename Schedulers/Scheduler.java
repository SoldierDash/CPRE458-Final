package Schedulers;

import java.util.LinkedList;
import java.util.PriorityQueue;

public abstract class Scheduler {
	
	protected class PeriodicTask implements Comparable<PeriodicTask> {
		
		private String name;
		private int computationTime;
		private int period;
		private int timeRemaining;
		
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
			this.timeRemaining = timeRemaining;
		}
		
		public void refresh() {
			this.timeRemaining = this.computationTime;
		}

		@Override
		public int compareTo(PeriodicTask arg0) {
			return this.period - arg0.period;
		}
		
	}
	
	protected PriorityQueue<PeriodicTask> periodicTasks;
	protected LinkedList<PeriodicTask> refreshList;
	protected AperiodicTaskQueue aperiodicTasks;
	protected int time;
	protected int lcmOfPeriodicTasks;
	
	public Scheduler() {
		this.periodicTasks = new PriorityQueue<PeriodicTask>();
		this.refreshList = new LinkedList<PeriodicTask>();
		this.aperiodicTasks = new AperiodicTaskQueue();
		this.time = 0;
		this.lcmOfPeriodicTasks = 0;
	}
	
	public void addPeriodicTask(String name, int computationTime, int period) {
		this.periodicTasks.add(new PeriodicTask(name, computationTime, period));
		lcmOfPeriodicTasks = lcmPeriodicTasks();
	}
	
	public void addAperiodicTask(String name, int startTime, int computationTime) {
		this.aperiodicTasks.addTask(name, startTime, computationTime);
	}
	
	public abstract String getNextTask();
	
	public abstract boolean isScheduleable();
	
	/*
	 * Please note, the code used to calculate the LCM of the periodic tasks is taken from the following webpage:
	 * http://stackoverflow.com/questions/4201860/how-to-find-gcf-lcm-on-a-set-of-numbers
	 */
	public int lcmPeriodicTasks() {
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
		return (time > lcmOfPeriodicTasks && aperiodicTasks.isDone());
	}
	
	public int getSimulationTime() {
		return this.time;
	}
}
