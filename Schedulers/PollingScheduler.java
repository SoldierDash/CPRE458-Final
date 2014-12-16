package Schedulers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class PollingScheduler {

	private class PeriodicTask implements Comparable<PeriodicTask> {
		
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
	
	private int serverComputationTime;
	private int serverPeriodTime;
	private PriorityQueue<PeriodicTask> periodicTasks;
	private LinkedList<PeriodicTask> refreshList;
	private AperiodicTaskQueue aperiodicTasks;
	private int time;
	
	public PollingScheduler(int serverComputationTime, int serverPeriodTime) {
		this.serverComputationTime = serverComputationTime;
		this.serverPeriodTime = serverPeriodTime;
		this.periodicTasks = new PriorityQueue<PeriodicTask>();
		this.refreshList = new LinkedList<PeriodicTask>();
		this.aperiodicTasks = new AperiodicTaskQueue();
		this.time = 0;
		periodicTasks.add(new PeriodicTask("SERVER", serverComputationTime, serverPeriodTime));
	}
	
	public void addPeriodicTask(String name, int computationTime, int period) {
		periodicTasks.add(new PeriodicTask(name, computationTime, period));
	}
	
	public void addAperiodicTask(String name, int startTime, int computationTime) {
		aperiodicTasks.addTask(name, startTime, computationTime);
	}
	
	public String getNextTask() {
		refreshTasks();
		String nextTask = null;
		PeriodicTask pt = periodicTasks.peek();
		if (pt != null) {
			if (pt.getName().equals("SERVER")) {
				nextTask = aperiodicTasks.getTaskAtTime(time);
				if (nextTask == null) {
					refreshList.add(periodicTasks.remove());
					pt = periodicTasks.peek();
					if (pt != null) {
						nextTask = pt.getName();
						updateTask(pt);
					}
				} else {
					updateTask(pt);
				}
			} else {
				nextTask = pt.getName();
				updateTask(pt);
			}
		}
		time += 1;
		return (nextTask != null) ? nextTask : "";
	}
	
	private void updateTask(PeriodicTask pt) {
		if (pt.getTimeRemaining() > 1) {
			pt.setTimeRemaining(pt.getTimeRemaining() - 1);
		} else {
			refreshList.add(periodicTasks.remove());
		}
	}
	
	private void refreshTasks() {
		Iterator<PeriodicTask> iterator = refreshList.iterator();
		while (iterator.hasNext()) {
			PeriodicTask pt = iterator.next();
			if (time % pt.getPeriod() == 0) {
				pt.refresh();
				iterator.remove();
				periodicTasks.add(pt);
			}
		}
	}
	
}
