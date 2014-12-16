package Schedulers;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class DeferrableScheduler {

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
	
	private PriorityQueue<PeriodicTask> periodicTasks;
	private LinkedList<PeriodicTask> refreshList;
	private PeriodicTask serverTask;
	private AperiodicTaskQueue aperiodicTasks;
	private int time;
	
	public DeferrableScheduler(int serverComputationTime, int serverPeriodTime) {
		this.periodicTasks = new PriorityQueue<PeriodicTask>();
		this.refreshList = new LinkedList<PeriodicTask>();
		this.serverTask = new PeriodicTask("SERVER", serverComputationTime, serverPeriodTime);
		this.aperiodicTasks = new AperiodicTaskQueue();
		this.time = 0;
		this.periodicTasks.add(serverTask);
	}
	
	public void addPeriodicTask(String name, int computationTime, int period) {
		this.periodicTasks.add(new PeriodicTask(name, computationTime, period));
	}
	
	public void addAperiodicTask(String name, int startTime, int computationTime) {
		this.aperiodicTasks.addTask(name, startTime, computationTime);
	}
	
	public String getNextTask() {
		refreshTasks();
		String nextTask = null;
		PeriodicTask pt = periodicTasks.peek();
		if (pt != null) {
			if (pt.getName().equals("SERVER")) {
				nextTask = this.aperiodicTasks.getTaskAtTime(time);
				if (nextTask == null) {
					periodicTasks.remove();
					PeriodicTask pt2 = periodicTasks.peek();
					if (pt2 != null) {
						nextTask = pt2.getName();
						updateTask(pt2);
					}
					periodicTasks.add(pt);
				} else {
					updateTask(pt);
				}
			} else {
				nextTask = pt.getName();
				updateTask(pt);
			}
		}
	
		this.time += 1;
		return (nextTask != null) ? nextTask : "";
	}
	
	private void updateTask(PeriodicTask pt) {
		if (pt.getTimeRemaining() > 1) {
			pt.setTimeRemaining(pt.getTimeRemaining() - 1);
		} else {
			this.refreshList.add(periodicTasks.remove());
		}
	}
	
	private void refreshTasks() {
		/* The Deferrable periodic task server might not have been added to the refresh list, so we manually udpate it. */
		if (this.time % serverTask.getPeriod() == 0) {
			serverTask.refresh();
		}
		Iterator<PeriodicTask> iterator = this.refreshList.iterator();
		while (iterator.hasNext()) {
			PeriodicTask pt = iterator.next();
			if (this.time % pt.getPeriod() == 0) {
				pt.refresh();
				iterator.remove();
				this.periodicTasks.add(pt);
			}
		}
	}
	
}
