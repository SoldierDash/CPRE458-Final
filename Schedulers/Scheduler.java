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
	
	public Scheduler(int serverComputationTime, int serverPeriodTime) {
		this.periodicTasks = new PriorityQueue<PeriodicTask>();
		this.refreshList = new LinkedList<PeriodicTask>();
		this.aperiodicTasks = new AperiodicTaskQueue();
		this.time = 0;
	}
	
	public void addPeriodicTask(String name, int computationTime, int period) {
		this.periodicTasks.add(new PeriodicTask(name, computationTime, period));
	}
	
	public void addAperiodicTask(String name, int startTime, int computationTime) {
		this.aperiodicTasks.addTask(name, startTime, computationTime);
	}
	
	public abstract String getNextTask();
}
